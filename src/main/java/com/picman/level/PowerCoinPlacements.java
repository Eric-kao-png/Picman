package com.picman.level;

import com.picman.config.PowerCoinConfig;
import com.picman.model.CellType;

/**
 * 將指定格設為大金幣。
 */
public final class PowerCoinPlacements {
    private PowerCoinPlacements() {
    }

    public static void apply(int[][] grid) {
        for (int[] position : PowerCoinConfig.POSITIONS) {
            int col = position[0];
            int row = position[1];
            if (row < 0 || row >= grid.length || col < 0 || col >= grid[0].length) {
                continue;
            }
            if (grid[row][col] == CellType.COIN.getCode()) {
                grid[row][col] = CellType.POWER_COIN.getCode();
            }
        }
    }
}
