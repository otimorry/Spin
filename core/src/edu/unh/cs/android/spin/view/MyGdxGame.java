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

import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import edu.unh.cs.android.spin.action.ActionThrow;
import edu.unh.cs.android.spin.controller.BucketController;
import edu.unh.cs.android.spin.controller.ControllerManager;
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
    private Label scoreLabel;
    private int gameScore;
    private final ControllerManager controller = new ControllerManager();
    private final Queue<ActionThrow> actionQueue = new LinkedBlockingQueue<>();
    private final Queue<Ball> gameBalls = new LinkedBlockingQueue<>();
    private final Queue<SpawnPoint> spawnPoints = new LinkedBlockingQueue<>();
    private final ArrayList<Ball> flyingBalls = new ArrayList<>();
    private final ArrayList<Ball> outBalls = new ArrayList<>();
    private final ArrayList<Bucket> buckets = new ArrayList<>();
    private Random rng;
    //endregion Fields

    //region create
    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        rng = new Random();
        font = new BitmapFont();
        scoreLabel = new Label( "Score: 0", new Label.LabelStyle(font, Color.WHITE));


        ActionThrow initActionThrow = new ActionThrow();
        actionQueue.offer(initActionThrow);

        /** Spawn Point **/
        /* How many spawn points? */
        spawnPoints.offer(new SpawnPoint(new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2)));

        /** Buckets **/
        /* How many buckets? */
        final Bucket blueBucket = new Bucket( Ball.Colors.BLUE, new Vector2(0,0) );
        final Bucket redBucket = new Bucket( Ball.Colors.RED, new Vector2(Gdx.graphics.getWidth() -
                Bucket.bucketSize,0) );
        buckets.add( blueBucket );
        buckets.add( redBucket );

        /* ControllerManager */
        for( Bucket bucket : buckets ) {
            controller.addController( new BucketController(bucket));
        }

        /* GameController - needs to be refactored later */
        InputMultiplexer multiplexer = new InputMultiplexer();
        final InputProcessor inputGesture = new GestureDetector(new InputGestureHandler(actionQueue));
        final InputProcessor inputEvent = new InputEventHandler( actionQueue );

        multiplexer.addProcessor(inputEvent);
        multiplexer.addProcessor(inputGesture);

        Gdx.input.setInputProcessor(multiplexer);

        // TODO Create an object store to keep track of the object on the field (e.g. flying balls)
    }
    //endregion create

    //region render
    @Override
    public void render() {
        //TODO: Refactor all logic into one class -> GameLogic.
        /* clear the screen */
        Gdx.gl.glClearColor(0.9f, 0.9f, 0.9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        /** We will use ShapeRenderer for now, final version will use
         *  Textures instead.
         */

        /** start of ShapeRenderer **/
        shapeRenderer.begin( ShapeRenderer.ShapeType.Filled );

        for( Bucket bucket : buckets ) {
            shapeRenderer.setColor( bucket.getColor() );
            shapeRenderer.rect( bucket.getLocation().x, bucket.getLocation().y,
                    Bucket.bucketSize, Bucket.bucketSize );
        }

        shapeRenderer.end();
        /** end of ShapeRenderer **/


        /** start batch **/
        batch.begin();

        if( gameBalls.isEmpty() ) {
            int rand = rng.nextInt(100);
            Ball ball = new Ball(rand);
            /* Potentially change to spawnPoints.poll() after
             * to simulate multiple spawn points */
            ball.setLocation( spawnPoints.peek().getSpawnPoint() );
            ball.setName(Integer.toString(rand));
            gameBalls.offer(ball);
        } else {
            batch.draw(gameBalls.peek().getImage(),
                    gameBalls.peek().getLocation().x,
                    gameBalls.peek().getLocation().y,
                    gameBalls.peek().BALLSIZE, gameBalls.peek().BALLSIZE );
        }

        /* If actionThrow has been modified and is ready to be used */
        if ( actionQueue.peek().getState() ) {

            Ball ball = gameBalls.poll();
            ball.setSpeed(actionQueue.peek().getSpeed());
            //TODO: Fix the direction at which ball moves


            /** IMPORTANT REFACTOR THIS SHIT **/
            /* Potentially change to spawnPoints.poll() after
             * to simulate multiple spawn points */
            double initX = actionQueue.peek().getTouchDownCoordinate().x;
            double initY = actionQueue.peek().getTouchDownCoordinate().y;
            double endX = actionQueue.peek().getTouchUpCoordinate().x;
            double endY = actionQueue.peek().getTouchUpCoordinate().y;

            double diffX = endX - initX;
            double diffY = endY - initY;
            double distance = Math.sqrt( Math.pow(diffX,2) + Math.pow(diffY,2) );
            double angle = Math.asin(diffY / distance);

            double aX = distance * Math.cos( angle );
            double aY = distance * Math.sin(angle);


            System.out.println( "InitX: " + initX + " InitY: " + initY );
            System.out.println( "EndX: " + endX + " EndY: " + endY );
            System.out.println( "DiffX: " + diffX + " DiffY: " + diffY );
            System.out.println( "Angle: " + angle + " aX: " + aX + " aY: " + aY );


            /* Get the next ball in Sequence */
            if( ball != null ) {
                /* first quadrant */
                if( diffX >= 0 && diffY < 0 ) {
                    aY = -aY;
                }
                /* second quadrant */
                else if( diffX < 0 && diffY < 0 ) {
                    aX = -aX;   aY = -aY;
                }
                /* third quadrant */
                else if( diffX < 0 && diffY >= 0 ) {
                    aX = -aX;   aY = -aY;
                }
                /* fourth quadrant */
                else {
                    aY = -aY;
                }

                ball.setAddXY( aX, aY );
                flyingBalls.add(ball);
            }

            /* actionThrow has been used and is not ready */
            actionQueue.peek().setState(false);
        }

        /* draw shit */

        for( Ball ball : flyingBalls ) {
            ball.update();
            batch.draw(ball.getImage(), ball.getLocation().x, ball.getLocation().y,
                    ball.BALLSIZE, ball.BALLSIZE );

            if( ball.getLocation().x >= Gdx.graphics.getWidth() || ball.getLocation().x <= 0 ||
                    ball.getLocation().y >= Gdx.graphics.getHeight() || ball.getLocation().y <= 0 ) {
                System.out.println( "Ball-Out: " + ball.getName() );
                outBalls.add(ball);
            }

            /* Collision Detection - need to find a better way */
            for( Bucket bucket: buckets ) {
                if( bucket.getBounds().contains( ball.getLocation()) &&
                        bucket.getColor() == bucket.getBucketColor( ball.getColor() ) ) {
                    System.out.println( "Hit! " + ball.getColor() + " Count: " + bucket.getBallCount());
                    gameScore++;
                    bucket.setBucketState(true);
                    outBalls.add(ball);
                } else if ( bucket.getBounds().contains( ball.getLocation()) &&
                        bucket.getColor() != bucket.getBucketColor( ball.getColor() ) ) {
                    System.out.println("Miss! " + ball.getColor() + " Count: " +
                            bucket.getBallCount() );
                    outBalls.add(ball);
                }

            }
        }

        /* Clear ArrayList */
        for( Ball outBall: outBalls ) {
            flyingBalls.remove( outBall );
        }

        controller.update();
        scoreLabel.setText("Count: " + gameScore);
        scoreLabel.draw(batch, 1.0f);

        outBalls.clear();

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
