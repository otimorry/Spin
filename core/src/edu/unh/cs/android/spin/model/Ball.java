package edu.unh.cs.android.spin.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import edu.unh.cs.android.spin.action.ActionThrow;
import edu.unh.cs.android.spin.controller.IEntity;

/**
 * Created by Olva on 4/8/15.
 *
 * Create a random ball object. Still in progress.
 */


public class Ball implements IEntity {

    //region Fields
    /* Global Variables */
    private Bucket bucket = null;
    private Texture img;
    private Colors color;
    private String name;
    private Vector2 speed;
    private double stepX, stepY;
    private int x, y;
    private double speedMult = 0.1;
    private boolean inPlay = false;
    private boolean isLastBall = false;
    public static final int BALLSIZE = 150;

    private final ArrayList<Ball> flyingBalls = new ArrayList<>();
    private final ArrayList<Ball> outBalls = new ArrayList<>();

    //endregion Fields

    //region Enum
    public enum Colors {
        /* use this format to add more colors */
        BLUE, RED, YELLOW, GREEN;

        /* change image of ball later
         * and don't forget to update Bucket class for
         * when we add more colors */
        public static Colors initBall( int num ) {
            switch( num ) {
                case 0:
                    return BLUE;
                case 1:
                    return RED;
                case 2:
                    return YELLOW;
                case 3:
                    return GREEN;
                default: return null;
            }
        }

    }
    //endregion Enum

    //region Constructor
    /* Creates a specific ball or a random-colored ball */
    public Ball( int id ) {
        color = Colors.initBall((id % Colors.values().length));
        init(color);
    }

    public Ball( Colors color ) {
        init(color);
    }
    //endregion Constructor

    //region Initialize
    /** - - - - - - - - Initialize - - - - - - - **/

    private void init( Colors color ) {
        setTexture(color);
    }

    /* sets the texture of the ball object */
    public void setTexture( Colors color ) {
        if( color == Colors.GREEN ) {
            img = new Texture( "GreenRing.gif" );
        } else if( color == Colors.BLUE ) {
            img = new Texture( "BlueRing.gif" );
        } else if( color == Colors.YELLOW ) {
            img = new Texture( "YellowRing.gif" );
        } else if( color == Colors.RED ) {
            img = new Texture( "RedRing.gif" );
        } else {
            img = null;
        }
    }

    public void setBucket( Bucket bucket ) {
        this.bucket = bucket;
    }
    //endregion Initialize

    //region Mutators
    /** - - - - - - - - Mutators - - - - - - - - **/

    /* sets ID of the ball object */
    public void setName( String name ) { this.name = name; }

    /* changes the speed field of the ball object */
    public void setSpeed( Vector2 speed ) { this.speed = speed; }

    /* sets the location of the ball object */
    public void setLocation( Vector2 loc ) {
        x = (int)loc.x;
        y = (int)loc.y;
    }

    /* updates addX and addY field */
    public void setSteps(double x, double y) {
        stepX = x * speedMult;
        stepY = y * speedMult;
    }

    public void setInPlay( boolean bool ) {
        inPlay = bool;
    }

    public void setLastBall( boolean bool ) {
        isLastBall = bool;
    }
    //endregion Mutators

    //region Logics
    /* calculates angle, distance, steps */
    public void calculateActionThrow( ActionThrow action ) {

            double initX = action.getTouchDownCoordinate().x;
            double initY = action.getTouchDownCoordinate().y;
            double endX = action.getTouchUpCoordinate().x;
            double endY = action.getTouchUpCoordinate().y;

            double diffX = endX - initX;
            double diffY = endY - initY;
            double distance = Math.sqrt( Math.pow(diffX,2) + Math.pow(diffY,2) );
            double angle = Math.asin(diffY / distance);

            double aX = distance * Math.cos(angle);
            double aY = distance * Math.sin(angle);

            if( this != null ) {
                /* first quadrant */
                if (diffX >= 0 && diffY < 0) {
                    aY = -aY;
                }
                /* second quadrant */
                else if (diffX < 0 && diffY < 0) {
                    aX = -aX;
                    aY = -aY;
                }
                /* third quadrant */
                else if (diffX < 0 && diffY >= 0) {
                    aX = -aX;
                    aY = -aY;
                }
                /* fourth quadrant */
                else {
                    aY = -aY;
                }

                setSteps(aX, aY);
            }
    }

    public boolean checkOutOfBounds( Ball ball ) {
        if( ball.getLocation().x >= Gdx.graphics.getWidth() || ball.getLocation().x <= 0 ||
                ball.getLocation().y >= Gdx.graphics.getHeight() || ball.getLocation().y <= 0 ) {
            return true;
        }
        return false;
    }

    public boolean checkCollision( Bucket bucket, Ball ball ) {
        if( bucket.getBounds().contains( ball.getLocation() ) &&
                bucket.getColor() == bucket.getBucketColor( ball.getColor() ) ) {
            bucket.setBucketState(true);
            return true;
        }
        return false;
    }
    //endregion Logics

    //region Interface Methods
    public void update( ) {
        x += stepX;
        y += stepY;

        if( checkOutOfBounds(this) ) {
            outBalls.add(this);
            this.setInPlay(false);
        }

        if( bucket != null ) {
            if( checkCollision(bucket,this)) {
                bucket.setBucketState(true);
                outBalls.add(this);
                this.setInPlay(false);
            }
        }
    }

    public void draw( Batch batch ) {
        // dummy erase later
        batch.draw( getImage(), 250, 250, Ball.BALLSIZE, Ball.BALLSIZE);
        if( isInPlay() ) {
            update( );
            batch.draw( getImage(), getXLocation(), getYLocation(), Ball.BALLSIZE, Ball.BALLSIZE);
        }
    }

    public void clean( ) {
        outBalls.clear();
    }
    //endregion Interface Methods

    //region Accessors
    /** - - - - - - - - Accessors - - - - - - - - **/
    /* returns the name of the ball */
    public String getName( ) { return name; }
    /* returns the id-color of the ball */
    public Colors getColor( ) { return color; }
    /* gets the speed of the object */
    public Vector2 getSpeed( ) { return speed; }
    /* returns the location of the ball object */
    public int getXLocation( ) { return x; }
    public int getYLocation( ) { return y; }
    public Vector2 getLocation( ) {
        Vector2 location = new Vector2(x,y);
        return location;
    }
    /* returns the ball image */
    public Texture getImage( ){ return img; }
    /* Has the ball been used */
    public boolean isInPlay() { return inPlay; }
    /* Is this the last ball */
    public boolean isLastBall() { return isLastBall; }
    /* steps */
    public Vector2 getSteps() {
        Vector2 steps = new Vector2( (float)stepX, (float)stepY );
        return steps;
    }
    //endregion Accessors

}
