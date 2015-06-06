package edu.unh.cs.android.spin.controller;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Created by Olva on 5/31/15.
 */
public interface IEntity {
    void draw( Batch batch );
    void update( );
    void clean( );
}
