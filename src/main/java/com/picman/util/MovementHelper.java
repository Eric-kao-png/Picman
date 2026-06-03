package com.picman.util;

import com.picman.model.Maze;

public final class MovementHelper {
    private MovementHelper() {
    }

    public static boolean isPerpendicularAligned(double centerX, double centerY, Direction moveDir) {
        if (moveDir == Direction.LEFT || moveDir == Direction.RIGHT) {
            int row = cellIndex(centerY);
            double rowCenter = row * Constants.TILE_SIZE + Constants.TILE_SIZE / 2.0;
            return Math.abs(centerY - rowCenter) <= Constants.ALIGN_THRESHOLD;
        }
        if (moveDir == Direction.UP || moveDir == Direction.DOWN) {
            int col = cellIndex(centerX);
            double colCenter = col * Constants.TILE_SIZE + Constants.TILE_SIZE / 2.0;
            return Math.abs(centerX - colCenter) <= Constants.ALIGN_THRESHOLD;
        }
        return true;
    }

    public static boolean canTurn(double centerX, double centerY, Direction current, Direction desired, Maze maze) {
        if (desired == null) {
            return false;
        }
        int col = cellIndex(centerX);
        int row = cellIndex(centerY);
        if (!maze.isWalkable(col + desired.dx, row + desired.dy)) {
            return false;
        }
        if (current == null || current == desired) {
            return true;
        }
        boolean currentHorizontal = current == Direction.LEFT || current == Direction.RIGHT;
        boolean desiredHorizontal = desired == Direction.LEFT || desired == Direction.RIGHT;
        if (currentHorizontal == desiredHorizontal) {
            return true;
        }
        return isPerpendicularAligned(centerX, centerY, current);
    }

    public static void alignInCorridor(double[] center, Direction moveDir, double speed) {
        if (moveDir == Direction.LEFT || moveDir == Direction.RIGHT) {
            int row = cellIndex(center[1]);
            double targetY = row * Constants.TILE_SIZE + Constants.TILE_SIZE / 2.0;
            center[1] = nudgeToward(center[1], targetY, speed);
        } else if (moveDir == Direction.UP || moveDir == Direction.DOWN) {
            int col = cellIndex(center[0]);
            double targetX = col * Constants.TILE_SIZE + Constants.TILE_SIZE / 2.0;
            center[0] = nudgeToward(center[0], targetX, speed);
        }
    }

    public static boolean canMoveTo(Maze maze, double centerX, double centerY, Direction dir, double speed) {
        double nextX = centerX + dir.dx * speed;
        double nextY = centerY + dir.dy * speed;
        double probeDist = Constants.TILE_SIZE / 2.0 - 2;

        double probeX = nextX + dir.dx * probeDist;
        double probeY = nextY + dir.dy * probeDist;

        return maze.isWalkable(cellIndex(probeX), cellIndex(probeY));
    }

    public static void clampAtWall(double[] center, Direction dir) {
        int col = cellIndex(center[0]);
        int row = cellIndex(center[1]);
        double cellCenterX = col * Constants.TILE_SIZE + Constants.TILE_SIZE / 2.0;
        double cellCenterY = row * Constants.TILE_SIZE + Constants.TILE_SIZE / 2.0;

        if (dir == Direction.RIGHT) {
            center[0] = Math.min(center[0], cellCenterX);
            center[1] = cellCenterY;
        } else if (dir == Direction.LEFT) {
            center[0] = Math.max(center[0], cellCenterX);
            center[1] = cellCenterY;
        } else if (dir == Direction.DOWN) {
            center[1] = Math.min(center[1], cellCenterY);
            center[0] = cellCenterX;
        } else if (dir == Direction.UP) {
            center[1] = Math.max(center[1], cellCenterY);
            center[0] = cellCenterX;
        }
    }

    public static int cellIndex(double pixelCoord) {
        return (int) Math.floor(pixelCoord / Constants.TILE_SIZE);
    }

    public static double cellCenter(int index) {
        return index * Constants.TILE_SIZE + Constants.TILE_SIZE / 2.0;
    }

    private static double nudgeToward(double current, double target, double maxDelta) {
        double diff = target - current;
        if (Math.abs(diff) <= Constants.ALIGN_THRESHOLD) {
            return target;
        }
        if (Math.abs(diff) <= maxDelta) {
            return target;
        }
        return current + Math.signum(diff) * maxDelta;
    }
}
