package com.picman.model;

import com.picman.util.Constants;
import com.picman.util.Direction;
import com.picman.util.MovementHelper;

import java.util.ArrayList;
import java.util.List;

public class Ghost {
    private final int spawnCol;
    private final int spawnRow;

    private double centerX;
    private double centerY;
    private Direction direction;

    public Ghost() {
        spawnCol = Constants.GHOST_START_COL;
        spawnRow = Constants.GHOST_START_ROW;
        reset();
    }

    public void reset() {
        centerX = MovementHelper.cellCenter(spawnCol);
        centerY = MovementHelper.cellCenter(spawnRow);
        direction = Direction.LEFT;
    }

    public int getCol() {
        return MovementHelper.cellIndex(centerX);
    }

    public int getRow() {
        return MovementHelper.cellIndex(centerY);
    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public double getDrawX() {
        return centerX - Constants.TILE_SIZE / 2.0;
    }

    public double getDrawY() {
        return centerY - Constants.TILE_SIZE / 2.0;
    }

    public void update(Maze maze, Pacman pacman) {
        int col = getCol();
        int row = getRow();

        if (!maze.isWalkable(col, row)) {
            double[] center = {centerX, centerY};
            MovementHelper.snapToCellCenter(center, spawnCol, spawnRow);
            centerX = center[0];
            centerY = center[1];
            direction = Direction.LEFT;
            return;
        }

        boolean atCenter = MovementHelper.isAtCellCenter(centerX, centerY);
        boolean blocked = direction != null && !MovementHelper.canAdvance(maze, col, row, direction);

        if (atCenter) {
            if (blocked || direction == null || isIntersection(maze, col, row)) {
                direction = pickDirection(maze, pacman, col, row);
            }
        }

        if (direction == null) {
            snapToGridCenter(col, row);
            return;
        }

        if (!MovementHelper.canAdvance(maze, col, row, direction)) {
            snapToGridCenter(col, row);
            direction = null;
            return;
        }

        double[] center = {centerX, centerY};
        boolean moved = MovementHelper.moveOnGridForGhost(maze, center, direction, Constants.GHOST_SPEED);
        centerX = center[0];
        centerY = center[1];

        if (!moved) {
            col = getCol();
            row = getRow();
            snapToGridCenter(col, row);
            direction = null;
        }
    }

    public boolean collidesWith(Pacman pacman) {
        double dx = centerX - pacman.getCenterX();
        double dy = centerY - pacman.getCenterY();
        double hitRadius = Constants.TILE_SIZE * 0.75;
        return dx * dx + dy * dy <= hitRadius * hitRadius;
    }

    private Direction pickDirection(Maze maze, Pacman pacman, int col, int row) {
        List<Direction> options = new ArrayList<>();
        for (Direction dir : Direction.values()) {
            if (maze.isWalkable(col + dir.dx, row + dir.dy)) {
                options.add(dir);
            }
        }

        if (options.isEmpty()) {
            return null;
        }

        if (options.size() == 1) {
            return options.get(0);
        }

        if (direction != null && options.contains(direction) && MovementHelper.canAdvance(maze, col, row, direction)) {
            if (!isIntersection(maze, col, row)) {
                return direction;
            }
        }

        int targetCol = pacman.getCol();
        int targetRow = pacman.getRow();

        List<Direction> bestOptions = new ArrayList<>();
        int bestDistance = Integer.MAX_VALUE;

        for (Direction candidate : options) {
            int distance = manhattan(col + candidate.dx, row + candidate.dy, targetCol, targetRow);
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

        if (direction != null && bestOptions.contains(direction)) {
            return direction;
        }

        if (bestOptions.size() == 1) {
            return bestOptions.get(0);
        }

        return bestOptions.get(0);
    }

    private boolean isIntersection(Maze maze, int col, int row) {
        int walkableNeighbors = 0;
        for (Direction dir : Direction.values()) {
            if (maze.isWalkable(col + dir.dx, row + dir.dy)) {
                walkableNeighbors++;
            }
        }
        return walkableNeighbors > 2;
    }

    private void snapToGridCenter(int col, int row) {
        double[] center = {centerX, centerY};
        MovementHelper.snapToCellCenter(center, col, row);
        centerX = center[0];
        centerY = center[1];
    }

    private static int manhattan(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }
}
