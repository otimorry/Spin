package edu.unh.cs.android.spin.controller;

import java.util.ArrayList;

/**
 * Created by Olva on 5/24/15.
 */
public class ControllerManager {

    //region Fields
    public ArrayList<IController> controllers;
    //endregion Fields

    //region Constructor
    public ControllerManager( ) {
        controllers = new ArrayList<>();
    }
    //endregion Constructor

    //region Mutators
    /** - - - - - - - - Mutators - - - - - - - - **/

    public void addController( IController controller ) {
        controllers.add(controller);
    }

    public void removeController( IController controller ) {
        controllers.remove(controller);
    }
    //endregion Mutators

    //region Accessors
    /** - - - - - - - - Accessors - - - - - - - - **/

    public ArrayList<IController> getControllers() {
        return controllers;
    }

    public void update() {
        for( IController controller : controllers ) {
            controller.update();
        }
    }
    //endregion Accessors
}
