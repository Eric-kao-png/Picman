package com.picman.model;

import com.picman.util.Direction;

import java.util.ArrayList;
import java.util.List;

public class Maze {
    private final int[][] grid;

    public Maze() {
        grid = createInitialGrid();
    }

    public void reset() {
        int[][] fresh = createInitialGrid();
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
        return countWalkableNeighbors(col, row) > 2;
    }

    public List<Direction> getValidDirections(int col, int row, Direction current, boolean excludeReverse) {
        List<Direction> options = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            if (excludeReverse && current != null && direction == current.opposite()) {
                continue;
            }
            if (isWalkable(col + direction.dx, row + direction.dy)) {
                options.add(direction);
            }
        }

        if (options.isEmpty() && current != null) {
            Direction reverse = current.opposite();
            if (isWalkable(col + reverse.dx, row + reverse.dy)) {
                options.add(reverse);
            }
        }
        return options;
    }

    private int countWalkableNeighbors(int col, int row) {
        int count = 0;
        for (Direction direction : Direction.values()) {
            if (isWalkable(col + direction.dx, row + direction.dy)) {
                count++;
            }
        }
        return count;
    }

    private boolean inBounds(int col, int row) {
        return col >= 0 && col < getWidth() && row >= 0 && row < getHeight();
    }

    private static int[][] createInitialGrid() {
        int[][] raw = MazeLayout.RAW;
        int[][] copy = new int[raw.length][raw[0].length];
        for (int row = 0; row < raw.length; row++) {
            for (int col = 0; col < raw[row].length; col++) {
                copy[row][col] = raw[row][col] == 0 ? CellType.COIN.getCode() : raw[row][col];
            }
        }
        return copy;
    }
}
