package com.picman.level;

import com.picman.model.CellType;
import com.picman.model.MazeLayout;

/**
 * 將地圖模板轉為執行時格子（含幽靈房覆寫）。
 */
public final class LevelInitializer {
    private LevelInitializer() {
    }

    public static int[][] createPlayfieldGrid() {
        int[][] raw = MazeLayout.RAW;
        int[][] grid = new int[raw.length][raw[0].length];
        for (int row = 0; row < raw.length; row++) {
            for (int col = 0; col < raw[row].length; col++) {
                grid[row][col] = resolveCellCode(raw[row][col], col, row);
            }
        }
        PowerCoinPlacements.apply(grid);
        return grid;
    }

    private static int resolveCellCode(int rawCode, int col, int row) {
        if (GhostHouseGeometry.isWall(col, row)) {
            return CellType.WALL.getCode();
        }
        if (GhostHouseGeometry.isSpecialWalkable(col, row)) {
            return CellType.EMPTY.getCode();
        }
        if (rawCode == 0) {
            return CellType.COIN.getCode();
        }
        return rawCode;
    }
}
