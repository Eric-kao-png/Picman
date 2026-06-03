package com.picman.model.ai;

import com.picman.model.Maze;
import com.picman.model.entity.GridPosition;
import com.picman.movement.GridMath;
import com.picman.movement.GridMovement;
import com.picman.util.Direction;

import java.util.ArrayList;
import java.util.List;

public class GhostChaseAI {
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

        return selectClosestToTarget(col, row, options, target, currentDirection);
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

    private Direction selectClosestToTarget(
            int col,
            int row,
            List<Direction> options,
            GridPosition target,
            Direction currentDirection) {
        int targetCol = target.getCol();
        int targetRow = target.getRow();

        List<Direction> bestOptions = new ArrayList<>();
        int bestDistance = Integer.MAX_VALUE;

        for (Direction candidate : options) {
            int distance = GridMath.manhattan(
                    col + candidate.dx,
                    row + candidate.dy,
                    targetCol,
                    targetRow);
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
