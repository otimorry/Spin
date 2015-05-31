package edu.unh.cs.android.spin.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import edu.unh.cs.android.spin.action.ActionThrow;
import edu.unh.cs.android.spin.controller.IDrawable;

/**
 * Created by Olva on 4/8/15.
 *
 * Create a random ball object. Still in progress.
 */


public class Ball implements IDrawable {

    //region Fields
    /* Global Variables */
    private Texture img;
    private Colors color;
    private String name;
    private Vector2 location, speed;
    private double addX, addY;
    private double speedMult = 0.1;
    private double distance, aX, aY, angle;
    private static final Random rng = new Random();
    public static final int BALLSIZE = 150;
    public static final ArrayList<Ball> flyingBalls = new ArrayList<>();
    public static final Queue<Ball> gameBalls = new LinkedBlockingQueue<>();
    public static final ArrayList<Ball> outBalls = new ArrayList<>();
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
        location = new Vector2();
        setTexture(color);
        location.x = 0; addX = 0;
        location.y = 0; addY = 0;
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
    //endregion Initialize

    //region Mutators
    /** - - - - - - - - Mutators - - - - - - - - **/

    /* sets ID of the ball object */
    public void setName( String name ) { this.name = name; }

    /* changes the speed field of the ball object */
    public void setSpeed( Vector2 speed ) { this.speed = speed; }

    /* sets the location of the ball object */
    public void setLocation( Vector2 loc ) {
        location.x = loc.x;
        location.y = loc.y;
    }

    /* updates addX and addY field */
    public void setAddXY(double x, double y) {
        addX = x * speedMult;
        addY = y * speedMult;
    }

    /* creates a ball if queue is empty */
    public static Ball refillGameBall( Queue<SpawnPoint> spawnPoints ) {
        int rand = rng.nextInt(100);
        Ball ball = new Ball(rand);
        /* Potentially change to spawnPoints.poll() after
         * to simulate multiple spawn points */
        ball.setLocation( spawnPoints.peek().getSpawnPoint() );
        ball.setName(Integer.toString(rand));
        gameBalls.offer(ball);
        return ball;
    }

    /* calculates angle, distance, steps */
    public void calculateActionThrow( Queue<ActionThrow> actionQueue ) {
        ActionThrow action = actionQueue.peek();
        if ( action.getState() ) {
            Ball ball = gameBalls.poll();
            ball.setSpeed(actionQueue.peek().getSpeed());

            double initX = action.getTouchDownCoordinate().x;
            double initY = action.getTouchDownCoordinate().y;
            double endX = action.getTouchUpCoordinate().x;
            double endY = action.getTouchUpCoordinate().y;

            double diffX = endX - initX;
            double diffY = endY - initY;
            distance = Math.sqrt( Math.pow(diffX,2) + Math.pow(diffY,2) );
            angle = Math.asin(diffY / distance);

            aX = distance * Math.cos(angle);
            aY = distance * Math.sin(angle);

            if( ball != null ) {
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

                ball.setAddXY(aX, aY);
                flyingBalls.add(ball);
            }
            actionQueue.peek().setState(false);
        }
    }

    /* updates the location of the ball object */
    public void update() {
        location.x += addX;
        location.y += addY;
    }

    public void draw( Batch batch ) {

        batch.draw( this.getImage(), this.getLocation().x, this.getLocation().y,
                    Ball.BALLSIZE, Ball.BALLSIZE);


        for( Ball b : Ball.flyingBalls ) {
            b.update();
            batch.draw(b.getImage(), b.getLocation().x, b.getLocation().y,
                    b.BALLSIZE, b.BALLSIZE );

            if( b.getLocation().x >= Gdx.graphics.getWidth() || b.getLocation().x <= 0 ||
                    b.getLocation().y >= Gdx.graphics.getHeight() || b.getLocation().y <= 0 ) {
                System.out.println( "Ball-Out: " + b.getName() );
                Ball.outBalls.add(b);
            }

            /* Collision Detection - need to find a better way */
            for( Bucket bucket: Bucket.buckets ) {
                if( bucket.getBounds().contains( b.getLocation()) &&
                        bucket.getColor() == bucket.getBucketColor( b.getColor() ) ) {
                    bucket.setBucketState(true);
                    Ball.outBalls.add(b);
                } else if ( bucket.getBounds().contains( b.getLocation()) &&
                        bucket.getColor() != bucket.getBucketColor( b.getColor() ) ) {
                    Ball.outBalls.add(b);
                }

            }
        }
    }
    //endregion Mutators

    //region Accessors
    /** - - - - - - - - Accessors - - - - - - - - **/

    /* returns the name of the ball */
    public String getName( ) { return name; }

    /* returns the id-color of the ball */
    public Colors getColor( ) { return color; }


    /* gets the speed of the object */
    public Vector2 getSpeed( ) { return speed; }
    /* returns the location of the ball object */
    public Vector2 getLocation( ) {
        return location;
    }

    /* returns the ball image */
    public Texture getImage( ){ return img; }


    /** - - - - - - - - Main - - - - - - - - **/

    public static void main( String[] args ) {
        Ball b1 = new Ball( 0 ); // put a random value here
        System.out.println( b1.color );
    }
    //endregion Accessors

}
