package edu.unh.cs.android.spin.controller;

import edu.unh.cs.android.spin.model.Bucket;

/**
 * Created by Olva on 5/24/15.
 */
public class BucketController implements IController {

    //region Fields
    private final Bucket bucket;
    //endregion Fields

    //region Constructor
    public BucketController(Bucket bucket) {
        this.bucket = bucket;
    }
    //endregion Constructor

    //region @Override Methods
    @Override
    public void update() {
        if( bucket.getBucketState() ) {
            bucket.incrementCount();
            bucket.setBucketState(false);
        }
    }
    //endregion @Override Methods
}
