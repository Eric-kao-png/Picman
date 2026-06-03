package com.picman.movement;

import com.picman.config.GameConfig;

public final class GridMath {
    private GridMath() {
    }

    public static int cellIndex(double pixelCoord) {
        return (int) Math.floor(pixelCoord / GameConfig.TILE_SIZE);
    }

    public static double cellCenter(int index) {
        return index * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2.0;
    }

    public static boolean isAtCellCenter(double centerX, double centerY) {
        int col = cellIndex(centerX);
        int row = cellIndex(centerY);
        return Math.abs(centerX - cellCenter(col)) <= GameConfig.ALIGN_THRESHOLD
                && Math.abs(centerY - cellCenter(row)) <= GameConfig.ALIGN_THRESHOLD;
    }

    public static boolean isPerpendicularAligned(double centerX, double centerY, com.picman.util.Direction moveDir) {
        if (moveDir == com.picman.util.Direction.LEFT || moveDir == com.picman.util.Direction.RIGHT) {
            int row = cellIndex(centerY);
            return Math.abs(centerY - cellCenter(row)) <= GameConfig.ALIGN_THRESHOLD;
        }
        if (moveDir == com.picman.util.Direction.UP || moveDir == com.picman.util.Direction.DOWN) {
            int col = cellIndex(centerX);
            return Math.abs(centerX - cellCenter(col)) <= GameConfig.ALIGN_THRESHOLD;
        }
        return true;
    }

    public static int manhattan(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }
}
