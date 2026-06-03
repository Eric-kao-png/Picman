package com.picman.model.ai;

import com.picman.model.Maze;
import com.picman.model.entity.Pacman;

/**
 * 青色幽靈（Inky）追擊點：點 A 為玩家朝向前 1 格，點 B 為紅鬼位置，
 * 追擊點 = 由 B 指向 A 的向量延伸兩倍（即 2·A − B）。
 */
public final class InkyTargetCalculator {
    public static final int INKY_PACMAN_LOOKAHEAD_TILES = 1;

    private InkyTargetCalculator() {
    }

    public static int[] chaseCell(Maze maze, Pacman pacman, int redCol, int redRow) {
        int[] pointA = AmbushTargetCalculator.cellAhead(maze, pacman, INKY_PACMAN_LOOKAHEAD_TILES);
        int chaseCol = 2 * pointA[0] - redCol;
        int chaseRow = 2 * pointA[1] - redRow;
        chaseCol = clamp(chaseCol, 0, maze.getWidth() - 1);
        chaseRow = clamp(chaseRow, 0, maze.getHeight() - 1);
        return new int[]{chaseCol, chaseRow};
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
