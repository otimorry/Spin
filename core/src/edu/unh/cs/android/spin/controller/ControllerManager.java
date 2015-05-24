package edu.unh.cs.android.spin.controller;

import java.util.ArrayList;

/**
 * Created by Olva on 5/24/15.
 */
public class ControllerManager {

    public ArrayList<IController> controllers;

    public ControllerManager( ) {
        controllers = new ArrayList<>();
    }

    /* - - - - - - - - Setters - - - - - - - - */

    public void addController( IController controller ) {
        controllers.add(controller);
    }

    public void removeController( IController controller ) {
        controllers.remove(controller);
    }

    /* - - - - - - - - Getters - - - - - - - - */

    public ArrayList<IController> getControllers() {
        return controllers;
    }

    public void update() {
        for( IController controller : controllers ) {
            controller.update();
        }
    }
}
