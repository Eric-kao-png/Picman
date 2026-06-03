package com.picman.model.ai;

/**
 * 網格座標（col, row）。
 */
public record GridCell(int col, int row) {
    public static GridCell of(int col, int row) {
        return new GridCell(col, row);
    }

    public static GridCell fromArray(int[] cell) {
        return new GridCell(cell[0], cell[1]);
    }
}
