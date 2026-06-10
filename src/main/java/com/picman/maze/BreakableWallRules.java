package com.picman.maze;

import com.picman.level.GhostHouseGeometry;
import com.picman.model.CellType;
import com.picman.model.Maze;

/**
 * 判定哪些牆可被鎬子破壞。最外圍邊界牆與幽靈房區域皆不可破壞。
 */
public final class BreakableWallRules {
    private BreakableWallRules() {
    }

    public static boolean canBreak(Maze maze, int col, int row) {
        if (col < 0 || row < 0 || col >= maze.getWidth() || row >= maze.getHeight()) {
            return false;
        }
        if (maze.getCellType(col, row) != CellType.WALL) {
            return false;
        }
        if (isOuterBorderWall(maze, col, row)) {
            return false;
        }
        return !GhostHouseGeometry.isPickaxeProtected(col, row);
    }

    private static boolean isOuterBorderWall(Maze maze, int col, int row) {
        return col == 0
                || row == 0
                || col == maze.getWidth() - 1
                || row == maze.getHeight() - 1;
    }
}
