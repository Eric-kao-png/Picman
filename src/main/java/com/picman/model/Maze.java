package com.picman.model;

import com.picman.level.GhostHouseGeometry;
import com.picman.level.LevelInitializer;
import com.picman.maze.MazeNavigator;
import com.picman.util.Direction;

import java.util.List;

public class Maze {
    private final int[][] grid;

    public Maze() {
        grid = LevelInitializer.createPlayfieldGrid();
    }

    public void reset() {
        int[][] fresh = LevelInitializer.createPlayfieldGrid();
        for (int row = 0; row < grid.length; row++) {
            System.arraycopy(fresh[row], 0, grid[row], 0, grid[row].length);
        }
    }

    public int getWidth() {
        return grid[0].length;
    }

    public int getHeight() {
        return grid.length;
    }

    public int getCell(int col, int row) {
        return grid[row][col];
    }

    public CellType getCellType(int col, int row) {
        return CellType.fromCode(getCell(col, row));
    }

    public boolean isWalkable(int col, int row) {
        return inBounds(col, row) && getCellType(col, row) != CellType.WALL;
    }

    public CollectibleType tryEatCollectible(int col, int row) {
        if (!inBounds(col, row)) {
            return CollectibleType.NONE;
        }
        CellType cell = getCellType(col, row);
        if (cell == CellType.COIN) {
            grid[row][col] = CellType.EMPTY.getCode();
            return CollectibleType.COIN;
        }
        if (cell == CellType.POWER_COIN) {
            grid[row][col] = CellType.EMPTY.getCode();
            return CollectibleType.POWER_COIN;
        }
        if (cell == CellType.EXTRA_LIFE_ITEM) {
            grid[row][col] = CellType.EMPTY.getCode();
            return CollectibleType.EXTRA_LIFE_ITEM;
        }
        return CollectibleType.NONE;
    }

    public boolean placeSpawnedItem(int col, int row, CellType itemType) {
        if (!inBounds(col, row) || getCellType(col, row) != CellType.EMPTY) {
            return false;
        }
        grid[row][col] = itemType.getCode();
        return true;
    }

    public void clearSpawnedItem(int col, int row) {
        if (!inBounds(col, row) || getCellType(col, row) != CellType.EXTRA_LIFE_ITEM) {
            return;
        }
        grid[row][col] = CellType.EMPTY.getCode();
    }

    /** 僅計算普通金幣；大金幣不影響過關條件。 */
    public boolean noCoinsLeft() {
        for (int[] row : grid) {
            for (int cell : row) {
                if (cell == CellType.COIN.getCode()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isIntersection(int col, int row) {
        return MazeNavigator.isIntersection(this, col, row);
    }

    public boolean isGhostHouseInterior(int col, int row) {
        return GhostHouseGeometry.isInterior(col, row);
    }

    public List<Direction> getValidDirections(int col, int row, Direction current, boolean excludeReverse) {
        return MazeNavigator.getValidDirections(this, col, row, current, excludeReverse);
    }

    private boolean inBounds(int col, int row) {
        return col >= 0 && col < getWidth() && row >= 0 && row < getHeight();
    }
}
