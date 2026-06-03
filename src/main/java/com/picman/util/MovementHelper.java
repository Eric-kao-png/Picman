package com.picman.util;

import com.picman.model.Maze;

public final class MovementHelper {
    private MovementHelper() {
    }

    public static boolean isAtCellCenter(double centerX, double centerY) {
        int col = cellIndex(centerX);
        int row = cellIndex(centerY);
        return Math.abs(centerX - cellCenter(col)) <= Constants.ALIGN_THRESHOLD
                && Math.abs(centerY - cellCenter(row)) <= Constants.ALIGN_THRESHOLD;
    }

    public static boolean isPerpendicularAligned(double centerX, double centerY, Direction moveDir) {
        if (moveDir == Direction.LEFT || moveDir == Direction.RIGHT) {
            int row = cellIndex(centerY);
            return Math.abs(centerY - cellCenter(row)) <= Constants.ALIGN_THRESHOLD;
        }
        if (moveDir == Direction.UP || moveDir == Direction.DOWN) {
            int col = cellIndex(centerX);
            return Math.abs(centerX - cellCenter(col)) <= Constants.ALIGN_THRESHOLD;
        }
        return true;
    }

    public static boolean canTurn(double centerX, double centerY, Direction current, Direction desired, Maze maze) {
        if (desired == null) {
            return false;
        }
        int col = cellIndex(centerX);
        int row = cellIndex(centerY);
        if (!maze.isWalkable(col, row) || !maze.isWalkable(col + desired.dx, row + desired.dy)) {
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

    public static boolean canAdvance(Maze maze, int col, int row, Direction dir) {
        if (dir == null) {
            return false;
        }
        return maze.isWalkable(col, row) && maze.isWalkable(col + dir.dx, row + dir.dy);
    }

    /**
     * 沿目前方向平滑移動；僅在當前格與下一格皆為可走時才位移。
     *
     * @return 是否成功移動
     */
    public static boolean moveOnGrid(Maze maze, double[] center, Direction dir, double speed) {
        return moveOnGrid(maze, center, dir, speed, true);
    }

    /**
     * 幽靈用移動：不做走廊對齊微調，避免撞牆時來回抖動。
     */
    public static boolean moveOnGridForGhost(Maze maze, double[] center, Direction dir, double speed) {
        return moveOnGrid(maze, center, dir, speed, false);
    }

    private static boolean moveOnGrid(Maze maze, double[] center, Direction dir, double speed, boolean alignInCorridor) {
        if (dir == null) {
            return false;
        }

        int col = cellIndex(center[0]);
        int row = cellIndex(center[1]);

        if (!maze.isWalkable(col, row)) {
            snapToWalkableCenter(maze, center);
            return false;
        }

        int nextCol = col + dir.dx;
        int nextRow = row + dir.dy;
        if (!maze.isWalkable(nextCol, nextRow)) {
            snapToCellCenter(center, col, row);
            return false;
        }

        if (alignInCorridor) {
            alignInCorridor(center, dir, speed);
            col = cellIndex(center[0]);
            row = cellIndex(center[1]);

            if (!maze.isWalkable(col, row)) {
                snapToWalkableCenter(maze, center);
                return false;
            }

            nextCol = col + dir.dx;
            nextRow = row + dir.dy;
            if (!maze.isWalkable(nextCol, nextRow)) {
                snapToCellCenter(center, col, row);
                return false;
            }
        }

        double nextX = center[0] + dir.dx * speed;
        double nextY = center[1] + dir.dy * speed;

        nextX = capAlongAxis(nextX, dir.dx, col, nextCol);
        nextY = capAlongAxis(nextY, dir.dy, row, nextRow);

        int afterCol = cellIndex(nextX);
        int afterRow = cellIndex(nextY);
        if (!maze.isWalkable(afterCol, afterRow)) {
            snapToCellCenter(center, col, row);
            return false;
        }

        center[0] = nextX;
        center[1] = nextY;
        return true;
    }

    public static void snapToCellCenter(double[] center, int col, int row) {
        center[0] = cellCenter(col);
        center[1] = cellCenter(row);
    }

    public static int cellIndex(double pixelCoord) {
        return (int) Math.floor(pixelCoord / Constants.TILE_SIZE);
    }

    public static double cellCenter(int index) {
        return index * Constants.TILE_SIZE + Constants.TILE_SIZE / 2.0;
    }

    private static void snapToWalkableCenter(Maze maze, double[] center) {
        int col = cellIndex(center[0]);
        int row = cellIndex(center[1]);
        if (maze.isWalkable(col, row)) {
            snapToCellCenter(center, col, row);
            return;
        }
        for (int dc = -1; dc <= 1; dc++) {
            for (int dr = -1; dr <= 1; dr++) {
                if (maze.isWalkable(col + dc, row + dr)) {
                    snapToCellCenter(center, col + dc, row + dr);
                    return;
                }
            }
        }
    }

    private static double capAlongAxis(double next, int delta, int fromCell, int toCell) {
        if (delta == 0) {
            return next;
        }
        double targetCenter = cellCenter(toCell);
        if (delta > 0) {
            return Math.min(next, targetCenter);
        }
        return Math.max(next, targetCenter);
    }

    private static void alignInCorridor(double[] center, Direction moveDir, double speed) {
        if (moveDir == Direction.LEFT || moveDir == Direction.RIGHT) {
            int row = cellIndex(center[1]);
            double targetY = cellCenter(row);
            center[1] = nudgeToward(center[1], targetY, speed);
        } else if (moveDir == Direction.UP || moveDir == Direction.DOWN) {
            int col = cellIndex(center[0]);
            double targetX = cellCenter(col);
            center[0] = nudgeToward(center[0], targetX, speed);
        }
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
