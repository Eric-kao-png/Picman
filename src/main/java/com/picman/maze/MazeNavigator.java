package com.picman.maze;

import com.picman.model.Maze;
import com.picman.util.Direction;

import java.util.ArrayList;
import java.util.List;

/**
 * 迷宮導航：岔路判定與可走方向列舉。
 */
public final class MazeNavigator {
    private MazeNavigator() {
    }

    public static boolean isIntersection(Maze maze, int col, int row) {
        return countWalkableNeighbors(maze, col, row) > 2;
    }

    public static List<Direction> getValidDirections(
            Maze maze,
            int col,
            int row,
            Direction current,
            boolean excludeReverse) {
        List<Direction> options = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            if (excludeReverse && current != null && direction == current.opposite()) {
                continue;
            }
            if (maze.isWalkable(col + direction.dx, row + direction.dy)) {
                options.add(direction);
            }
        }

        if (options.isEmpty() && current != null) {
            Direction reverse = current.opposite();
            if (maze.isWalkable(col + reverse.dx, row + reverse.dy)) {
                options.add(reverse);
            }
        }
        return options;
    }

    private static int countWalkableNeighbors(Maze maze, int col, int row) {
        int count = 0;
        for (Direction direction : Direction.values()) {
            if (maze.isWalkable(col + direction.dx, row + direction.dy)) {
                count++;
            }
        }
        return count;
    }
}
