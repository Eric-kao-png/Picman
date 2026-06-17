package com.picman.level;

import com.picman.model.CellType;
import com.picman.model.Maze;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 從可走且為空白的格子中隨機挑選道具生成位置。
 */
public final class SpawnCellSelector {
    private SpawnCellSelector() {
    }

    public static int[] pickRandomEmptyCell(Maze maze, Random random) {
        List<int[]> candidates = new ArrayList<>();
        for (int row = 0; row < maze.getHeight(); row++) {
            for (int col = 0; col < maze.getWidth(); col++) {
                if (!isSpawnable(maze, col, row)) {
                    continue;
                }
                candidates.add(new int[]{col, row});
            }
        }
        if (candidates.isEmpty()) {
            return null;
        }
        return candidates.get(random.nextInt(candidates.size()));
    }

    private static boolean isSpawnable(Maze maze, int col, int row) {
        if (maze.getCellType(col, row) != CellType.EMPTY) {
            return false;
        }
        return !GhostHouseGeometry.isInterior(col, row);
    }
}
