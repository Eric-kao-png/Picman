package com.picman.level;

import com.picman.config.GhostHouseConfig;

/**
 * 幽靈房幾何判定（單一來源，供關卡建圖與實體邏輯共用）。
 */
public final class GhostHouseGeometry {
    private GhostHouseGeometry() {
    }

    public static boolean isInterior(int col, int row) {
        return col >= GhostHouseConfig.INTERIOR_MIN_COL
                && col <= GhostHouseConfig.INTERIOR_MAX_COL
                && row >= GhostHouseConfig.INTERIOR_MIN_ROW
                && row <= GhostHouseConfig.INTERIOR_MAX_ROW;
    }

    public static boolean isWall(int col, int row) {
        if (row == GhostHouseConfig.WALL_TOP_ROW
                && col >= GhostHouseConfig.WALL_MIN_COL
                && col <= GhostHouseConfig.WALL_MAX_COL) {
            return true;
        }
        if (row == GhostHouseConfig.WALL_BOTTOM_ROW
                && col >= GhostHouseConfig.WALL_MIN_COL
                && col <= GhostHouseConfig.WALL_MAX_COL
                && !GhostHouseConfig.isDoorCell(col, row)) {
            return true;
        }
        if (col == GhostHouseConfig.WALL_MIN_COL
                && row >= GhostHouseConfig.INTERIOR_MIN_ROW
                && row <= GhostHouseConfig.INTERIOR_MAX_ROW) {
            return true;
        }
        return col == GhostHouseConfig.WALL_MAX_COL
                && row >= GhostHouseConfig.INTERIOR_MIN_ROW
                && row <= GhostHouseConfig.INTERIOR_MAX_ROW;
    }

    public static boolean isSpecialWalkable(int col, int row) {
        return isInterior(col, row)
                || GhostHouseConfig.isDoorCell(col, row)
                || GhostHouseConfig.isExitCorridorCell(col, row);
    }
}
