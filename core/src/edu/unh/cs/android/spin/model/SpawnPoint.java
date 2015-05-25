package edu.unh.cs.android.spin.model;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Olva on 5/15/15.
 */
public class SpawnPoint {

    private Vector2 spawnPoint;

    public SpawnPoint( Vector2 spawnPoint ) {
        this.spawnPoint = spawnPoint;
    }

    /** - - - - - - - - Setters - - - - - - - - **/

    public void setSpawnPoint( Vector2 spawn ) { this.spawnPoint = spawn; }

    /** - - - - - - - - Getters - - - - - - - - **/

    public Vector2 getSpawnPoint( ) { return spawnPoint; };

}
