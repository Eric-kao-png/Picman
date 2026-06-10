package com.picman.movement;

import com.picman.config.GameConfig;
import com.picman.maze.Walkability;
import com.picman.model.Maze;
import com.picman.model.entity.GridPosition;
import com.picman.util.Direction;

public final class GridMovement {
    private GridMovement() {
    }

    public static boolean canAdvance(Maze maze, int col, int row, Direction dir) {
        return canAdvance(maze, col, row, dir, false);
    }

    public static boolean canAdvance(Maze maze, int col, int row, Direction dir, boolean canBreakWalls) {
        if (dir == null) {
            return false;
        }
        boolean onValidCell = canBreakWalls
                ? Walkability.isOccupiableForPacman(maze, col, row)
                : Walkability.isOccupiableForGhost(maze, col, row);
        return onValidCell && SideTunnel.canStep(maze, col, row, dir, canBreakWalls);
    }

    public static boolean canTurn(GridPosition position, Direction current, Direction desired, Maze maze) {
        return canTurn(position, current, desired, maze, false);
    }

    public static boolean canTurn(
            GridPosition position,
            Direction current,
            Direction desired,
            Maze maze,
            boolean canBreakWalls) {
        if (desired == null) {
            return false;
        }
        int col = position.getCol();
        int row = position.getRow();
        if (!Walkability.isOccupiableForPacman(maze, col, row)
                || !SideTunnel.canStep(maze, col, row, desired, canBreakWalls)) {
            return false;
        }
        if (current == null || current == desired) {
            return true;
        }
        if (current.isHorizontal() == desired.isHorizontal()) {
            return true;
        }
        return GridMath.isPerpendicularAligned(position.getCenterX(), position.getCenterY(), current);
    }

    public static boolean movePacman(Maze maze, GridPosition position, Direction dir) {
        return movePacman(maze, position, dir, false);
    }

    public static boolean movePacman(Maze maze, GridPosition position, Direction dir, boolean canBreakWalls) {
        return move(maze, position, dir, GameConfig.PACMAN_SPEED, true, canBreakWalls, true);
    }

    public static boolean moveGhost(Maze maze, GridPosition position, Direction dir) {
        return move(maze, position, dir, GameConfig.GHOST_SPEED, false, false, false);
    }

    public static boolean moveGhostWithSpeed(Maze maze, GridPosition position, Direction dir, double speed) {
        return move(maze, position, dir, speed, false, false, false);
    }

    /** 將 Pac-Man 從實心牆格彈出至最近可走格。 */
    public static void ejectFromSolidCell(Maze maze, GridPosition position) {
        if (Walkability.isOccupiableForPacman(maze, position.getCol(), position.getRow())) {
            return;
        }
        snapToNearestWalkable(maze, position, true);
    }

    private static boolean move(
            Maze maze,
            GridPosition position,
            Direction dir,
            double speed,
            boolean alignInCorridor,
            boolean canBreakWalls,
            boolean pacman) {
        if (dir == null) {
            return false;
        }

        int col = position.getCol();
        int row = position.getRow();

        if (!isOccupiable(maze, col, row, pacman)) {
            snapToNearestWalkable(maze, position, pacman);
            return false;
        }

        if (SideTunnel.isWrapEdge(col, row, dir)) {
            return moveThroughWrapEdge(maze, position, dir, speed, alignInCorridor, pacman);
        }

        int[] target = SideTunnel.resolveTarget(maze, col, row, dir, canBreakWalls);
        if (target == null) {
            position.snapToCell(col, row);
            return false;
        }

        int nextCol = target[0];
        int nextRow = target[1];

        if (alignInCorridor) {
            alignInCorridor(position, dir, speed);
            col = position.getCol();
            row = position.getRow();
            if (!isOccupiable(maze, col, row, pacman)) {
                snapToNearestWalkable(maze, position, pacman);
                return false;
            }
            target = SideTunnel.resolveTarget(maze, col, row, dir, canBreakWalls);
            if (target == null) {
                position.snapToCell(col, row);
                return false;
            }
            nextCol = target[0];
            nextRow = target[1];
        }

        breakWallIfNeeded(maze, nextCol, nextRow, canBreakWalls);
        return advanceTowardTarget(maze, position, dir, speed, col, row, nextCol, nextRow, pacman);
    }

    private static void breakWallIfNeeded(Maze maze, int col, int row, boolean canBreakWalls) {
        if (canBreakWalls && maze.canBreakWall(col, row)) {
            maze.breakWall(col, row);
        }
    }

    private static boolean advanceTowardTarget(
            Maze maze,
            GridPosition position,
            Direction dir,
            double speed,
            int fromCol,
            int fromRow,
            int nextCol,
            int nextRow,
            boolean pacman) {
        double nextX = position.getCenterX() + dir.dx * speed;
        double nextY = position.getCenterY() + dir.dy * speed;
        nextX = capAlongAxis(nextX, dir.dx, nextCol);
        nextY = capAlongAxis(nextY, dir.dy, nextRow);

        if (!isOccupiable(maze, GridMath.cellIndex(nextX), GridMath.cellIndex(nextY), pacman)) {
            position.snapToCell(fromCol, fromRow);
            return false;
        }

        position.setCenter(nextX, nextY);
        return true;
    }

    private static boolean moveThroughWrapEdge(
            Maze maze,
            GridPosition position,
            Direction dir,
            double speed,
            boolean alignInCorridor,
            boolean pacman) {
        if (alignInCorridor && dir.isHorizontal()) {
            int row = position.getRow();
            position.setCenterY(nudgeToward(position.getCenterY(), GridMath.cellCenter(row), speed));
        }

        double nextX = position.getCenterX() + dir.dx * speed;
        position.setCenter(nextX, position.getCenterY());
        SideTunnel.wrapPositionHorizontally(position);

        return isOccupiable(maze, position.getCol(), position.getRow(), pacman);
    }

    private static boolean isOccupiable(Maze maze, int col, int row, boolean pacman) {
        return pacman
                ? Walkability.isOccupiableForPacman(maze, col, row)
                : Walkability.isOccupiableForGhost(maze, col, row);
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

    private static void snapToNearestWalkable(Maze maze, GridPosition position, boolean pacman) {
        int col = position.getCol();
        int row = position.getRow();
        if (isOccupiable(maze, col, row, pacman)) {
            position.snapToCell(col, row);
            return;
        }
        int maxRadius = Math.max(maze.getWidth(), maze.getHeight());
        for (int radius = 1; radius <= maxRadius; radius++) {
            for (int dc = -radius; dc <= radius; dc++) {
                for (int dr = -radius; dr <= radius; dr++) {
                    if (isOccupiable(maze, col + dc, row + dr, pacman)) {
                        position.snapToCell(col + dc, row + dr);
                        return;
                    }
                }
            }
        }
    }
}
