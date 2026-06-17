package com.picman.input;

import com.picman.util.Direction;

import java.awt.event.KeyEvent;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

/**
 * WASD 與 {@link Direction} 的對應與優先順序。
 */
public final class DirectionKeyMap {
    private static final Map<Integer, Direction> KEY_TO_DIRECTION = Map.of(
            KeyEvent.VK_W, Direction.UP,
            KeyEvent.VK_S, Direction.DOWN,
            KeyEvent.VK_A, Direction.LEFT,
            KeyEvent.VK_D, Direction.RIGHT);

    private static final Map<Direction, Integer> DIRECTION_TO_KEY = invert(KEY_TO_DIRECTION);

    private static final Direction[] RESOLVE_PRIORITY = {
            Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT
    };

    private DirectionKeyMap() {
    }

    public static Direction fromKeyCode(int keyCode) {
        return KEY_TO_DIRECTION.get(keyCode);
    }

    public static boolean isDirectionKey(int keyCode) {
        return KEY_TO_DIRECTION.containsKey(keyCode);
    }

    public static boolean isPressed(Direction direction, Set<Integer> pressedKeys) {
        if (direction == null) {
            return false;
        }
        Integer keyCode = DIRECTION_TO_KEY.get(direction);
        return pressedKeys.contains(keyCode);
    }

    public static Direction firstPressed(Set<Integer> pressedKeys) {
        for (Direction direction : RESOLVE_PRIORITY) {
            if (isPressed(direction, pressedKeys)) {
                return direction;
            }
        }
        return null;
    }

    private static Map<Direction, Integer> invert(Map<Integer, Direction> keyToDirection) {
        Map<Direction, Integer> result = new EnumMap<>(Direction.class);
        keyToDirection.forEach((key, direction) -> result.put(direction, key));
        return Map.copyOf(result);
    }
}
