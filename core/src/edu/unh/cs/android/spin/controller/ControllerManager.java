package edu.unh.cs.android.spin.controller;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;

import java.util.ArrayList;

/**
 * Created by Olva on 5/24/15.
 */
public class ControllerManager extends InputMultiplexer {

    //region Fields
    public ArrayList<IController> controllers;
    public InputMultiplexer multiplexer;
    //endregion Fields

    //region Constructor
    public ControllerManager( InputMultiplexer multiplexer ) {
        this.multiplexer = multiplexer;
        controllers = new ArrayList<>();
    }
    //endregion Constructor

    //region Mutators
    /** - - - - - - - - Mutators - - - - - - - - **/

    public void addController( IController controller ) {
        controllers.add(controller);
        if( controller instanceof InputProcessor ) {
            multiplexer.addProcessor((InputProcessor)controller);
        } else if( controller instanceof GestureDetector.GestureListener ) {
            GestureDetector detector = new GestureDetector((GestureDetector.GestureListener)controller);
            multiplexer.addProcessor(detector);
        }
    }

    public void removeController( IController controller ) {
        controllers.remove(controller);
        if(controller instanceof InputProcessor) {
            multiplexer.removeProcessor((InputProcessor)controller);
        } else if(controller instanceof GestureDetector.GestureListener) {
            multiplexer.removeProcessor((GestureDetector)controller);
        }
    }
    //endregion Mutators

    //region Accessors
    /** - - - - - - - - Accessors - - - - - - - - **/

    /* returns a list of controllers */
    public ArrayList<IController> getControllers() {
        return controllers;
    }

    /* returns multiplexer */
    public InputMultiplexer getMultiplexer( ) {
        return multiplexer;
    }

    public void update() {
        for( IController controller : controllers ) {
            controller.update();
        }
    }
    //endregion Accessors
}
