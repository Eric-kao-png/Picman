package com.picman.model.ai;

import com.picman.config.GhostAIConfig;
import com.picman.model.Maze;
import com.picman.model.entity.Pacman;
import com.picman.movement.GridMath;
import com.picman.util.Direction;

/**
 * 各幽靈個性的追擊目標格計算。
 */
public final class ChaseTargetCalculators {
    private ChaseTargetCalculators() {
    }

    public static GridCell pacmanCell(Pacman pacman) {
        return GridCell.of(
                GridMath.cellIndex(pacman.getPosition().getCenterX()),
                GridMath.cellIndex(pacman.getPosition().getCenterY()));
    }

    public static GridCell pinkAmbush(Maze maze, Pacman pacman) {
        return cellAhead(maze, pacman, GhostAIConfig.PINK_LOOKAHEAD_TILES);
    }

    public static GridCell inkyVector(Maze maze, GhostAIContext context) {
        int[] pointA = cellAheadArray(maze, context.pacman(), GhostAIConfig.INKY_PACMAN_LOOKAHEAD_TILES);
        var red = context.redGhost();
        int chaseCol = clamp(2 * pointA[0] - red.getCol(), 0, maze.getWidth() - 1);
        int chaseRow = clamp(2 * pointA[1] - red.getRow(), 0, maze.getHeight() - 1);
        return GridCell.of(chaseCol, chaseRow);
    }

    public static GridCell cellAhead(Maze maze, Pacman pacman, int tilesAhead) {
        return GridCell.fromArray(cellAheadArray(maze, pacman, tilesAhead));
    }

    private static int[] cellAheadArray(Maze maze, Pacman pacman, int tilesAhead) {
        int col = pacman.getCol();
        int row = pacman.getRow();
        Direction facing = pacman.getFacingDirection();
        if (facing == null || tilesAhead <= 0) {
            return new int[]{col, row};
        }

        for (int step = 0; step < tilesAhead; step++) {
            int nextCol = col + facing.dx;
            int nextRow = row + facing.dy;
            if (!maze.isWalkable(nextCol, nextRow)) {
                break;
            }
            col = nextCol;
            row = nextRow;
        }
        return new int[]{col, row};
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
