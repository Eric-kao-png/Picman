package com.picman.maze;

import com.picman.model.Maze;

/**
 * 統一 Pac-Man 與幽靈的格子通行判定，避免多處重複且不一致的邏輯。
 */
public final class Walkability {
    private Walkability() {
    }

    /** Pac-Man 目前所站格子是否合法（含已破壞牆格）。 */
    public static boolean isOccupiableForPacman(Maze maze, int col, int row) {
        return maze.isWalkableForPacman(col, row);
    }

    /** 幽靈目前所站格子是否合法（牆格一律不可，含已破壞牆）。 */
    public static boolean isOccupiableForGhost(Maze maze, int col, int row) {
        return maze.isWalkable(col, row);
    }

    /**
     * 下一步目標格是否可進入。
     * Pac-Man 在鎬子生效時可進入可破壞牆；隧道列仍沿用一般可走格規則。
     */
    public static boolean canEnter(Maze maze, int col, int row, boolean canBreakWalls) {
        if (maze.isWalkableForPacman(col, row)) {
            return true;
        }
        if (canBreakWalls && maze.canBreakWall(col, row)) {
            return true;
        }
        return maze.isWalkable(col, row);
    }
}
