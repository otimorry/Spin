package edu.unh.cs.android.spin.controller;

import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

import java.util.Queue;

import edu.unh.cs.android.spin.action.Action;
import edu.unh.cs.android.spin.action.ActionThrow;

public class InputGestureHandler implements GestureDetector.GestureListener {
    //region Fields
    private final Queue<ActionThrow> actionQueue;
    //endregion Fields

    //region Constructor
    public InputGestureHandler(Queue<ActionThrow> actionQueue) {
        this.actionQueue = actionQueue;
    }
    //endregion Constructor

    //region @Override Methods
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        Vector2 flingVelocity = new Vector2(velocityX, velocityY);
        actionQueue.peek().setSpeed(flingVelocity);
        actionQueue.peek().setState(true);
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
    //endregion @Override Methods
}
