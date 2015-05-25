package edu.unh.cs.android.spin.controller;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

import java.util.Queue;

import edu.unh.cs.android.spin.action.Action;
import edu.unh.cs.android.spin.action.ActionThrow;

/**
 * @author Bence Cserna (csbence@gmail.com)
 */
public class InputEventHandler implements InputProcessor {
    private final Queue<ActionThrow> actionQueue;

    public InputEventHandler(Queue<ActionThrow> actionQueue) {
        this.actionQueue = actionQueue;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 touchDownCoordinate = new Vector2( screenX, screenY );
        actionQueue.peek().setTouchDownCoordinate( touchDownCoordinate );
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector2 touchUpCoordinate = new Vector2( screenX, screenY );
        actionQueue.peek().setTouchUpCoordinate( touchUpCoordinate );
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
