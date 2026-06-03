package com.picman.input;

import com.picman.util.Direction;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class KeyboardInput {
    private final Set<Integer> pressedKeys = new HashSet<>();
    private Direction lastDirection;

    public void onKeyPressed(int keyCode) {
        Direction direction = toDirection(keyCode);
        if (direction != null) {
            pressedKeys.add(keyCode);
            lastDirection = direction;
        }
    }

    public void onKeyReleased(int keyCode) {
        pressedKeys.remove(keyCode);
        if (toDirection(keyCode) == lastDirection) {
            lastDirection = resolveActiveDirection();
        }
    }

    public void clear() {
        pressedKeys.clear();
        lastDirection = null;
    }

    public Direction getActiveDirection() {
        return resolveActiveDirection();
    }

    private Direction resolveActiveDirection() {
        if (isDirectionPressed(lastDirection)) {
            return lastDirection;
        }
        if (pressedKeys.contains(KeyEvent.VK_W)) {
            return Direction.UP;
        }
        if (pressedKeys.contains(KeyEvent.VK_S)) {
            return Direction.DOWN;
        }
        if (pressedKeys.contains(KeyEvent.VK_A)) {
            return Direction.LEFT;
        }
        if (pressedKeys.contains(KeyEvent.VK_D)) {
            return Direction.RIGHT;
        }
        return null;
    }

    private boolean isDirectionPressed(Direction direction) {
        if (direction == null) {
            return false;
        }
        return switch (direction) {
            case UP -> pressedKeys.contains(KeyEvent.VK_W);
            case DOWN -> pressedKeys.contains(KeyEvent.VK_S);
            case LEFT -> pressedKeys.contains(KeyEvent.VK_A);
            case RIGHT -> pressedKeys.contains(KeyEvent.VK_D);
        };
    }

    private static Direction toDirection(int keyCode) {
        return switch (keyCode) {
            case KeyEvent.VK_W -> Direction.UP;
            case KeyEvent.VK_S -> Direction.DOWN;
            case KeyEvent.VK_A -> Direction.LEFT;
            case KeyEvent.VK_D -> Direction.RIGHT;
            default -> null;
        };
    }
}
