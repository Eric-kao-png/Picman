package com.picman.input;

import com.picman.util.Direction;

import java.util.HashSet;
import java.util.Set;

public class KeyboardInput {
    private final Set<Integer> pressedKeys = new HashSet<>();
    private Direction lastDirection;

    public void onKeyPressed(int keyCode) {
        Direction direction = DirectionKeyMap.fromKeyCode(keyCode);
        if (direction != null) {
            pressedKeys.add(keyCode);
            lastDirection = direction;
        }
    }

    public void onKeyReleased(int keyCode) {
        pressedKeys.remove(keyCode);
        if (DirectionKeyMap.fromKeyCode(keyCode) == lastDirection) {
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
        if (DirectionKeyMap.isPressed(lastDirection, pressedKeys)) {
            return lastDirection;
        }
        return DirectionKeyMap.firstPressed(pressedKeys);
    }
}
