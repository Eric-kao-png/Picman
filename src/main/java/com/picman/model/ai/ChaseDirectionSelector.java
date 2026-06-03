package com.picman.model.ai;

import com.picman.model.Maze;
import com.picman.model.entity.GridPosition;
import com.picman.movement.TurnPlanner;
import com.picman.util.Direction;

import java.util.ArrayList;
import java.util.List;

/**
 * 岔路時依「到目標的距離」挑方向；子類提供距離評估器。
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
        if (TurnPlanner.canKeepCurrentDirection(maze, col, row, currentDirection, options)) {
            return currentDirection;
        }
        return selectBestDirection(col, row, options, createEvaluator(maze, target), currentDirection);
    }

    protected abstract DistanceEvaluator createEvaluator(Maze maze, GridPosition target);

    @FunctionalInterface
    protected interface DistanceEvaluator {
        int steps(int fromCol, int fromRow);
    }

    private Direction selectBestDirection(
            int col,
            int row,
            List<Direction> options,
            DistanceEvaluator evaluator,
            Direction currentDirection) {
        List<Direction> bestOptions = new ArrayList<>();
        int bestDistance = Integer.MAX_VALUE;

        for (Direction candidate : options) {
            int distance = evaluator.steps(col + candidate.dx, row + candidate.dy);
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
