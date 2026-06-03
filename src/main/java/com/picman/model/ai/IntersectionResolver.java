package com.picman.model.ai;

import com.picman.model.Maze;
import com.picman.movement.TurnPlanner;
import com.picman.util.Direction;

import java.util.List;

/**
 * 岔路前的共用前置判斷（無路、單選、直走）。
 */
final class IntersectionResolver {
    private IntersectionResolver() {
    }

    static Result resolve(
            Maze maze,
            int col,
            int row,
            Direction currentDirection) {
        List<Direction> options = maze.getValidDirections(col, row, currentDirection, true);
        if (options.isEmpty()) {
            return Result.none();
        }
        if (options.size() == 1) {
            return Result.decided(options.get(0));
        }
        if (TurnPlanner.canKeepCurrentDirection(maze, col, row, currentDirection, options)) {
            return Result.decided(currentDirection);
        }
        return Result.choose(options);
    }

    record Result(List<Direction> options, Direction direction, boolean needsRanking) {
        static Result none() {
            return new Result(List.of(), null, false);
        }

        static Result decided(Direction direction) {
            return new Result(List.of(), direction, false);
        }

        static Result choose(List<Direction> options) {
            return new Result(options, null, true);
        }
    }
}
