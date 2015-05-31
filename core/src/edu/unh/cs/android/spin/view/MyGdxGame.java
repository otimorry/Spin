package edu.unh.cs.android.spin.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import edu.unh.cs.android.spin.action.ActionThrow;
import edu.unh.cs.android.spin.controller.BucketController;
import edu.unh.cs.android.spin.controller.ControllerManager;
import edu.unh.cs.android.spin.controller.IDrawable;
import edu.unh.cs.android.spin.controller.InputEventHandler;
import edu.unh.cs.android.spin.controller.InputGestureHandler;
import edu.unh.cs.android.spin.model.Ball;
import edu.unh.cs.android.spin.model.Bucket;
import edu.unh.cs.android.spin.model.SpawnPoint;

public class MyGdxGame extends ApplicationAdapter {
    //region Fields
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private final ControllerManager controller = new ControllerManager(new InputMultiplexer());
    private final Queue<ActionThrow> actionQueue = new LinkedBlockingQueue<>();
    private final Queue<SpawnPoint> spawnPoints = new LinkedBlockingQueue<>();
    private final ArrayList<Label> bucketScores = new ArrayList<>();
    private final ArrayList<IDrawable> drawables = new ArrayList<>();
    private Random rng;
    //endregion Fields

    //region create
    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        rng = new Random();
        font = new BitmapFont();

        ActionThrow initActionThrow = new ActionThrow();
        actionQueue.offer(initActionThrow);

        /** Spawn Point **/
        /* How many spawn points? */
        spawnPoints.offer(new SpawnPoint(new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2)));

        /** Buckets **/
        /* How many buckets? */
        final Bucket blueBucket = new Bucket( Ball.Colors.BLUE, new Vector2(0,0), font );
        final Bucket redBucket = new Bucket( Ball.Colors.RED, new Vector2(Gdx.graphics.getWidth() -
                Bucket.bucketSize,0 ), font );
        Bucket.buckets.add( blueBucket );
        Bucket.buckets.add( redBucket );

        /* ControllerManager */
        for( Bucket bucket : Bucket.buckets ) {
            controller.addController( new BucketController(bucket));
            bucketScores.add(bucket.getBucketLabel());
            drawables.add(bucket);
        }


        controller.addController(new InputEventHandler(actionQueue));
        controller.addController(new InputGestureHandler(actionQueue));

        Gdx.input.setInputProcessor(controller.getMultiplexer());

        // TODO Create an object store to keep track of the object on the field (e.g. flying balls)
    }
    //endregion create

    //region render
    @Override
    public void render() {
        //TODO: Refactor logic manipulation
        /* clear the screen */
        Gdx.gl.glClearColor(0.9f, 0.9f, 0.9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        /** We will use ShapeRenderer for now, final version will use
         *  Textures instead.
         */

        /** start of ShapeRenderer **/
        shapeRenderer.begin( ShapeRenderer.ShapeType.Filled );

        for( Bucket bucket : Bucket.buckets ) {
            shapeRenderer.setColor( bucket.getColor() );
            shapeRenderer.rect( bucket.getLocation().x, bucket.getLocation().y,
                    Bucket.bucketSize, Bucket.bucketSize );
        }

        shapeRenderer.end();
        /** end of ShapeRenderer **/


        /** start batch **/
        batch.begin();

        Ball ball = null;
        if( Ball.gameBalls.isEmpty() ) {
            ball = Ball.refillGameBall(spawnPoints);
            drawables.add(ball);
        } else {
            ball = Ball.gameBalls.peek();
        }

        if( ball != null ) {
            ball.calculateActionThrow(actionQueue);
        }

        /* draw shit */
        //TODO: REFACTOR DRAW() IN ENTITY CLASSES


        for( IDrawable drawable : drawables ) {
            drawable.draw(batch);
        }

        controller.update();

        /* Clear ArrayList */
        for( Ball outBall: Ball.outBalls ) {
            Ball.flyingBalls.remove(outBall);
            drawables.remove(outBall);
        }


        for( Label score : bucketScores ) {
            score.draw( batch, 0.9f );
        }

        Ball.outBalls.clear();

        batch.end();
        /** end batch **/
    }
    //endregion render

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}
