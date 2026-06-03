package com.picman.model;

import com.picman.util.Constants;
import com.picman.util.Direction;
import com.picman.util.MovementHelper;

import java.util.List;
import java.util.Random;

public class Ghost {
    private static final Random RANDOM = new Random();

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
        if (MovementHelper.isPerpendicularAligned(centerX, centerY, direction)) {
            chooseDirection(maze, pacman);
        }

        if (direction == null) {
            return;
        }

        double[] center = {centerX, centerY};
        MovementHelper.alignInCorridor(center, direction, Constants.GHOST_SPEED);
        centerX = center[0];
        centerY = center[1];

        double nextX = centerX + direction.dx * Constants.GHOST_SPEED;
        double nextY = centerY + direction.dy * Constants.GHOST_SPEED;

        if (MovementHelper.canMoveTo(maze, centerX, centerY, direction, Constants.GHOST_SPEED)) {
            centerX = nextX;
            centerY = nextY;
        } else {
            center[0] = centerX;
            center[1] = centerY;
            MovementHelper.clampAtWall(center, direction);
            centerX = center[0];
            centerY = center[1];
        }
    }

    public boolean collidesWith(Pacman pacman) {
        double dx = centerX - pacman.getCenterX();
        double dy = centerY - pacman.getCenterY();
        double hitRadius = Constants.TILE_SIZE * 0.75;
        return dx * dx + dy * dy <= hitRadius * hitRadius;
    }

    private void chooseDirection(Maze maze, Pacman pacman) {
        List<Direction> options = maze.getValidDirections(getCol(), getRow(), direction, true);
        if (options.isEmpty()) {
            return;
        }
        if (options.size() == 1) {
            direction = options.get(0);
            return;
        }

        int targetCol = pacman.getCol();
        int targetRow = pacman.getRow();

        Direction best = options.get(0);
        int bestDistance = manhattan(getCol() + best.dx, getRow() + best.dy, targetCol, targetRow);

        for (int i = 1; i < options.size(); i++) {
            Direction candidate = options.get(i);
            int distance = manhattan(getCol() + candidate.dx, getRow() + candidate.dy, targetCol, targetRow);
            if (distance < bestDistance) {
                bestDistance = distance;
                best = candidate;
            } else if (distance == bestDistance && RANDOM.nextBoolean()) {
                best = candidate;
            }
        }
        direction = best;
    }

    private static int manhattan(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }
}
