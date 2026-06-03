package com.picman.movement;

import com.picman.maze.MazeNavigator;
import com.picman.model.Maze;
import com.picman.util.Direction;

import java.util.List;

/**
 * 實體在格子中心是否需重新選向，以及是否可維持目前方向。
 */
public final class TurnPlanner {
    private TurnPlanner() {
    }

    public static boolean needsDirectionChoice(Maze maze, int col, int row, Direction current) {
        boolean blocked = current != null && !GridMovement.canAdvance(maze, col, row, current);
        return blocked || current == null || MazeNavigator.isIntersection(maze, col, row);
    }

    public static boolean canKeepCurrentDirection(
            Maze maze,
            int col,
            int row,
            Direction current,
            List<Direction> options) {
        return current != null
                && options.contains(current)
                && GridMovement.canAdvance(maze, col, row, current)
                && !MazeNavigator.isIntersection(maze, col, row);
    }
}
