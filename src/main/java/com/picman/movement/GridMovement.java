package com.picman.movement;

import com.picman.config.GameConfig;
import com.picman.model.Maze;
import com.picman.model.entity.GridPosition;
import com.picman.util.Direction;

public final class GridMovement {
    private GridMovement() {
    }

    public static boolean canAdvance(Maze maze, int col, int row, Direction dir) {
        if (dir == null) {
            return false;
        }
        return maze.isWalkable(col, row) && maze.isWalkable(col + dir.dx, row + dir.dy);
    }

    public static boolean canTurn(GridPosition position, Direction current, Direction desired, Maze maze) {
        if (desired == null) {
            return false;
        }
        int col = position.getCol();
        int row = position.getRow();
        if (!maze.isWalkable(col, row) || !maze.isWalkable(col + desired.dx, row + desired.dy)) {
            return false;
        }
        if (current == null || current == desired) {
            return true;
        }
        boolean currentHorizontal = current.isHorizontal();
        boolean desiredHorizontal = desired.isHorizontal();
        if (currentHorizontal == desiredHorizontal) {
            return true;
        }
        return GridMath.isPerpendicularAligned(position.getCenterX(), position.getCenterY(), current);
    }

    public static boolean movePacman(Maze maze, GridPosition position, Direction dir) {
        return move(maze, position, dir, GameConfig.PACMAN_SPEED, true);
    }

    public static boolean moveGhost(Maze maze, GridPosition position, Direction dir) {
        return move(maze, position, dir, GameConfig.GHOST_SPEED, false);
    }

    private static boolean move(Maze maze, GridPosition position, Direction dir, double speed, boolean alignInCorridor) {
        if (dir == null) {
            return false;
        }

        int col = position.getCol();
        int row = position.getRow();

        if (!maze.isWalkable(col, row)) {
            snapToNearestWalkable(maze, position);
            return false;
        }

        int nextCol = col + dir.dx;
        int nextRow = row + dir.dy;
        if (!maze.isWalkable(nextCol, nextRow)) {
            position.snapToCell(col, row);
            return false;
        }

        if (alignInCorridor) {
            alignInCorridor(position, dir, speed);
            col = position.getCol();
            row = position.getRow();
            if (!maze.isWalkable(col, row)) {
                snapToNearestWalkable(maze, position);
                return false;
            }
            nextCol = col + dir.dx;
            nextRow = row + dir.dy;
            if (!maze.isWalkable(nextCol, nextRow)) {
                position.snapToCell(col, row);
                return false;
            }
        }

        double nextX = position.getCenterX() + dir.dx * speed;
        double nextY = position.getCenterY() + dir.dy * speed;
        nextX = capAlongAxis(nextX, dir.dx, nextCol);
        nextY = capAlongAxis(nextY, dir.dy, nextRow);

        if (!maze.isWalkable(GridMath.cellIndex(nextX), GridMath.cellIndex(nextY))) {
            position.snapToCell(col, row);
            return false;
        }

        position.setCenter(nextX, nextY);
        return true;
    }

    private static void alignInCorridor(GridPosition position, Direction moveDir, double speed) {
        if (moveDir.isHorizontal()) {
            int row = position.getRow();
            position.setCenterY(nudgeToward(position.getCenterY(), GridMath.cellCenter(row), speed));
        } else if (moveDir.isVertical()) {
            int col = position.getCol();
            position.setCenterX(nudgeToward(position.getCenterX(), GridMath.cellCenter(col), speed));
        }
    }

    private static double capAlongAxis(double next, int delta, int targetCell) {
        if (delta == 0) {
            return next;
        }
        double targetCenter = GridMath.cellCenter(targetCell);
        return delta > 0 ? Math.min(next, targetCenter) : Math.max(next, targetCenter);
    }

    private static double nudgeToward(double current, double target, double maxDelta) {
        double diff = target - current;
        if (Math.abs(diff) <= GameConfig.ALIGN_THRESHOLD) {
            return target;
        }
        if (Math.abs(diff) <= maxDelta) {
            return target;
        }
        return current + Math.signum(diff) * maxDelta;
    }

    private static void snapToNearestWalkable(Maze maze, GridPosition position) {
        int col = position.getCol();
        int row = position.getRow();
        if (maze.isWalkable(col, row)) {
            position.snapToCell(col, row);
            return;
        }
        for (int dc = -1; dc <= 1; dc++) {
            for (int dr = -1; dr <= 1; dr++) {
                if (maze.isWalkable(col + dc, row + dr)) {
                    position.snapToCell(col + dc, row + dr);
                    return;
                }
            }
        }
    }
}
