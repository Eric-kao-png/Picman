package com.picman.model;

import com.picman.util.Constants;
import com.picman.util.Direction;
import com.picman.util.MovementHelper;

public class Pacman {
    private double centerX;
    private double centerY;
    private Direction direction;
    private Direction activeDirection;

    public Pacman() {
        reset();
    }

    public void reset() {
        centerX = MovementHelper.cellCenter(Constants.PACMAN_START_COL);
        centerY = MovementHelper.cellCenter(Constants.PACMAN_START_ROW);
        direction = null;
        activeDirection = null;
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

    public void setActiveDirection(Direction direction) {
        activeDirection = direction;
        if (direction == null) {
            this.direction = null;
        }
    }

    public void update(Maze maze) {
        if (activeDirection == null) {
            direction = null;
            return;
        }

        if (MovementHelper.canTurn(centerX, centerY, direction, activeDirection, maze)) {
            direction = activeDirection;
        }

        if (direction == null) {
            if (MovementHelper.canTurn(centerX, centerY, null, activeDirection, maze)) {
                direction = activeDirection;
            } else {
                return;
            }
        }

        double[] center = {centerX, centerY};
        MovementHelper.alignInCorridor(center, direction, Constants.PACMAN_SPEED);
        centerX = center[0];
        centerY = center[1];

        double nextX = centerX + direction.dx * Constants.PACMAN_SPEED;
        double nextY = centerY + direction.dy * Constants.PACMAN_SPEED;

        if (MovementHelper.canMoveTo(maze, centerX, centerY, direction, Constants.PACMAN_SPEED)) {
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
}
