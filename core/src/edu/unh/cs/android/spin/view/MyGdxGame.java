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
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Logger;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import edu.unh.cs.android.spin.action.ActionThrow;
import edu.unh.cs.android.spin.controller.BallController;
import edu.unh.cs.android.spin.controller.BucketController;
import edu.unh.cs.android.spin.controller.ControllerManager;
import edu.unh.cs.android.spin.controller.IController;
import edu.unh.cs.android.spin.controller.IEntity;
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
    private final Queue<Ball> gameBalls = new LinkedBlockingQueue<>();
    private final ArrayList<Ball> flyingBalls = new ArrayList<>();
    private final ArrayList<Ball> outBalls = new ArrayList<>();
    private final ArrayList<Label> bucketScores = new ArrayList<>();
    private final ArrayList<IEntity> entities = new ArrayList<>();
    private final ArrayList<Bucket> buckets = new ArrayList<>();

    private Random rng;
    private final int BALLNUM = 5;
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
        buckets.add(blueBucket);
        buckets.add(redBucket);

        /** Balls**/
        for( int i = 0; i < BALLNUM; i++ ) {
            Ball ball = new Ball(0);
            ball.setLocation(spawnPoints.peek().getSpawnPoint());
            if( ball.getColor() == Ball.Colors.BLUE ) ball.setBucket(blueBucket);
            if( ball.getColor() == Ball.Colors.RED ) ball.setBucket(redBucket);
            gameBalls.offer(ball);
            entities.add(ball);
        }
        // final ball - this is hackish //
        gameBalls.offer(new Ball(0));

        for( Bucket bucket : buckets ) {
            bucketScores.add(bucket.getBucketLabel());
            entities.add(bucket);
        }
        /* ControllerManager */

        controller.addController(new BallController(gameBalls.poll(),this));
        controller.addController(new BucketController(buckets.get(0)));
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

        controller.update();

        /* draw shit */
        //TODO: REFACTOR DRAW() IN ENTITY CLASSES

        /** We will use ShapeRenderer for now, final version will use
         *  Textures instead.
         */

        /** start of ShapeRenderer **/
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for( Bucket bucket : buckets ) {
            shapeRenderer.setColor( bucket.getColor() );
            shapeRenderer.rect( bucket.getLocation().x, bucket.getLocation().y,
                    Bucket.bucketSize, Bucket.bucketSize );
        }
        shapeRenderer.end();
        /** end of ShapeRenderer **/

        /** start batch **/
        batch.begin();

        for( IEntity entity : entities ) {
            entity.draw(batch);
            entity.clean();
        }

        for( Label score : bucketScores ) {
            score.draw( batch, 0.9f );
        }


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

    public ArrayList<Bucket> getBuckets( ) { return buckets; }
    public Queue<ActionThrow> getActionQueue() { return actionQueue; }
    public Queue<SpawnPoint> getSpawnPoints() { return spawnPoints; }
    public Queue<Ball> getGameBalls(){ return gameBalls; }
    public ArrayList<Ball> getFlyingBalls() {return flyingBalls; }
    public ArrayList<Ball> getOutBalls() {return outBalls; }
}
