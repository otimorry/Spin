package edu.unh.cs.android.spin.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Olva on 4/8/15.
 *
 * Create a random ball object. Still in progress.
 */


public class Ball {

    /* Global Variables */
    private Colors color;
    private String name;
    private double addX, addY;
    private double speedMult = 0.1;
    private Vector2 location, speed;
    private Texture img;
    public static final int BALLSIZE = 150;

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

    /* Creates a specific ball or a random-colored ball */
    public Ball( int id ) {
        location = new Vector2();
        color = Colors.initBall((id % Colors.values().length));
        setTexture(color);
        location.x = 0; addX = 0;
        location.y = 0; addY = 0;
    }
    /** - - - - - - - - Setters - - - - - - - - **/

    /* sets ID of the ball object */
    public void setName( String name ) { this.name = name; }

    /* changes the speed field of the ball object */
    public void setSpeed( Vector2 speed ) { this.speed = speed; }

    /* sets the location of the ball object */
    public void setLocation( Vector2 loc ) {
        location.x = loc.x;
        location.y = loc.y;
    }

    /* updates the location of the ball object */
    public void update() {
        location.x += addX;
        location.y += addY;
    }

    /* updates addX and addY field */
    public void setAddXY(double x, double y) {
        addX = x * speedMult;
        addY = y * speedMult;
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

    /** - - - - - - - - Getters - - - - - - - - **/

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


}
