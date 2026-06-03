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

    public boolean tryEatCoin(int col, int row) {
        if (!inBounds(col, row) || getCellType(col, row) != CellType.COIN) {
            return false;
        }
        grid[row][col] = CellType.EMPTY.getCode();
        return true;
    }

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
