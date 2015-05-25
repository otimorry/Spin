package edu.unh.cs.android.spin.model;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Olva on 5/15/15.
 */
public class SpawnPoint {

    //region Fields
    private Vector2 spawnPoint;
    //endregion Fields

    //region Constructor
    public SpawnPoint( Vector2 spawnPoint ) {
        this.spawnPoint = spawnPoint;
    }
    //endregion Constructor

    //region Mutators
    /** - - - - - - - - Mutators - - - - - - - - **/

    public void setSpawnPoint( Vector2 spawn ) { this.spawnPoint = spawn; }

    //endregion Mutators

    //region Accessors
    /** - - - - - - - - Accessors - - - - - - - - **/

    public Vector2 getSpawnPoint( ) { return spawnPoint; };

    //endregion Accessors
}
