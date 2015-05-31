package edu.unh.cs.android.spin.controller;

import edu.unh.cs.android.spin.model.Bucket;

/**
 * Created by Olva on 5/24/15.
 */
public class BucketController implements IController {

    private final Bucket bucket;

    public BucketController(Bucket bucket) {
        this.bucket = bucket;
    }

    @Override
    public void update() {
        if( bucket.getBucketState() ) {
            bucket.incrementBallCount();
            bucket.setBucketLabel();
            bucket.setBucketState(false);
        }
    }
}
