package com.picman.model.ai;

import com.picman.model.Maze;
import com.picman.model.entity.GridPosition;
import com.picman.movement.GridMovement;
import com.picman.util.Direction;

import java.util.ArrayList;
import java.util.List;

/**
 * 岔路時依「到目標的距離」挑方向；子類決定距離為曼哈頓或路徑步數。
 */
abstract class ChaseDirectionSelector implements GhostAI {
    @Override
    public Direction chooseDirection(
            Maze maze,
            int col,
            int row,
            Direction currentDirection,
            GridPosition target) {
        List<Direction> options = maze.getValidDirections(col, row, currentDirection, true);
        if (options.isEmpty()) {
            return null;
        }
        if (options.size() == 1) {
            return options.get(0);
        }
        if (shouldKeepDirection(maze, col, row, currentDirection, options)) {
            return currentDirection;
        }
        return selectBestDirection(maze, col, row, options, target, currentDirection);
    }

    protected DistanceEvaluator createEvaluator(Maze maze, GridPosition target) {
        return (fromCol, fromRow) -> distanceToTarget(maze, fromCol, fromRow, target);
    }

    protected abstract int distanceToTarget(
            Maze maze,
            int fromCol,
            int fromRow,
            GridPosition target);

    @FunctionalInterface
    protected interface DistanceEvaluator {
        int steps(int fromCol, int fromRow);
    }

    private boolean shouldKeepDirection(
            Maze maze,
            int col,
            int row,
            Direction currentDirection,
            List<Direction> options) {
        return currentDirection != null
                && options.contains(currentDirection)
                && GridMovement.canAdvance(maze, col, row, currentDirection)
                && !maze.isIntersection(col, row);
    }

    private Direction selectBestDirection(
            Maze maze,
            int col,
            int row,
            List<Direction> options,
            GridPosition target,
            Direction currentDirection) {
        DistanceEvaluator evaluator = createEvaluator(maze, target);
        List<Direction> bestOptions = new ArrayList<>();
        int bestDistance = Integer.MAX_VALUE;

        for (Direction candidate : options) {
            int nextCol = col + candidate.dx;
            int nextRow = row + candidate.dy;
            int distance = evaluator.steps(nextCol, nextRow);
            if (distance < bestDistance) {
                bestDistance = distance;
                bestOptions.clear();
                bestOptions.add(candidate);
            } else if (distance == bestDistance) {
                bestOptions.add(candidate);
            }
        }

        if (bestOptions.isEmpty()) {
            return null;
        }
        if (currentDirection != null && bestOptions.contains(currentDirection)) {
            return currentDirection;
        }
        return bestOptions.get(0);
    }
}
