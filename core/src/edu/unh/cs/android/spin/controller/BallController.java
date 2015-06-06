package edu.unh.cs.android.spin.controller;

import com.badlogic.gdx.Gdx;

import edu.unh.cs.android.spin.action.ActionThrow;
import edu.unh.cs.android.spin.model.Ball;
import edu.unh.cs.android.spin.view.MyGdxGame;

/**
 * Created by Olva on 6/2/15.
 */
public class BallController implements IController {

    private Ball ball;
    private MyGdxGame game;

    public BallController( Ball ball, MyGdxGame game ) {
        this.ball = ball;
        this.game = game;
    }

    public void setMasterBall( Ball ball ) {
        this.ball = ball;
    }

    @Override
    public void update() {
        ActionThrow action = game.getActionQueue().peek();

        if( ball.isLastBall() )
            return;

        if( action.getState() && !ball.isInPlay() ) {
            ball.setSpeed(action.getSpeed());
            ball.calculateActionThrow( action);
            action.setState(false);
            ball.setInPlay(true);
        } else if( !game.getGameBalls().isEmpty() && ball.isInPlay() ) {
            setMasterBall(game.getGameBalls().poll());
            if( game.getGameBalls().isEmpty() ) {
                ball.setLastBall(true);
            }

        }
    }
}
