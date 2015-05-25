package edu.unh.cs.android.spin.action;

import com.badlogic.gdx.math.Vector2;

public class ActionThrow implements Action {

    private double angle;
    private Vector2 speed, touchDownCoordinate, touchUpCoordinate;
    private boolean state;

    public ActionThrow( ) {
        angle = 0.0;
        speed = touchDownCoordinate = touchUpCoordinate = null;
        state = false;
    }

    /** - - - - - - - - Setters - - - - - - - - **/

    /* records angle of ActionThrow */
    public void setAngle( double angle ) { this.angle = angle; }

    /* records Fling velocity */
    public void setSpeed( Vector2 speed ) { this.speed = speed; }

    /* records touchUp location */
    public void setTouchUpCoordinate( Vector2 touchUpCoordinate ) {
        this.touchUpCoordinate = touchUpCoordinate;
    }

    /* records touchDown location */
    public void setTouchDownCoordinate( Vector2 touchDownCoordinate ) {
        this.touchDownCoordinate = touchDownCoordinate;
    }

    /* sets the state of ActionThrow
    *  when its false actionThrow has not been used yet
    *  when its true render will use the data stored in
    *  this class then sets the state back to false
    **/
    public void setState( boolean bool ) { state = bool; }

    /** - - - - - - - - Getters - - - - - - - - **/

    /* get the angle of the ball */
    public double getAngle() { return angle; }

    /* gets the speed of the ball */
    public Vector2 getSpeed() { return speed; }

    /* checks if actionThrow has been used or not */
    public boolean getState( ) { return state; }

    /* gets touchUp location */
    public Vector2 getTouchUpCoordinate( ) { return touchUpCoordinate; }

    /* gets touchDown location */
    public Vector2 getTouchDownCoordinate() { return touchDownCoordinate; }
}
