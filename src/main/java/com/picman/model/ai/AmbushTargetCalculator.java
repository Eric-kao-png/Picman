package com.picman.model.ai;

import com.picman.model.Maze;
import com.picman.model.entity.Pacman;
import com.picman.util.Direction;

/**
 * 依玩家朝向計算伏擊追擊點（沿面向延伸若干格，遇牆則停在前一格）。
 */
public final class AmbushTargetCalculator {
    public static final int PINK_LOOKAHEAD_TILES = 4;

    private AmbushTargetCalculator() {
    }

    public static int[] cellAhead(Maze maze, Pacman pacman, int tilesAhead) {
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
}
