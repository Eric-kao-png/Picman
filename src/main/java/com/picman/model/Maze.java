package com.picman.model;

import com.picman.util.Direction;

import java.util.ArrayList;
import java.util.List;

public class Maze {
    public static final int EMPTY = 0;
    public static final int WALL = 1;
    public static final int COIN = 2;

    private static final int[][] TEMPLATE = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
            {1, 2, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1},
            {1, 2, 1, 0, 1, 2, 2, 2, 1, 0, 1, 2, 2, 2, 2, 2, 2, 1, 0, 2, 1},
            {1, 2, 1, 0, 1, 0, 1, 2, 1, 0, 1, 0, 1, 1, 1, 1, 2, 1, 0, 2, 1},
            {1, 2, 1, 0, 1, 0, 1, 2, 2, 0, 2, 0, 1, 2, 2, 1, 0, 1, 0, 2, 1},
            {1, 2, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 2, 1, 0, 1, 0, 2, 1},
            {1, 2, 2, 2, 2, 0, 1, 2, 2, 1, 2, 0, 1, 2, 2, 1, 0, 2, 2, 2, 1},
            {1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 2, 1},
            {1, 0, 0, 0, 0, 0, 0, 2, 1, 0, 0, 0, 0, 0, 2, 1, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 0, 2, 1, 1, 1, 1, 1, 1},
            {1, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 1, 0, 2, 2, 2, 2, 2, 2, 1},
            {1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1},
            {1, 2, 2, 2, 2, 1, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 1, 2, 2, 2, 1},
            {1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1},
            {1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
    };

    private final int[][] grid;

    public Maze() {
        grid = copyTemplate();
    }

    public void reset() {
        int[][] fresh = copyTemplate();
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

    public boolean isWalkable(int col, int row) {
        if (!inBounds(col, row)) {
            return false;
        }
        return grid[row][col] != WALL;
    }

    public boolean tryEatCoin(int col, int row) {
        if (!inBounds(col, row) || grid[row][col] != COIN) {
            return false;
        }
        grid[row][col] = EMPTY;
        return true;
    }

    public int countCoins() {
        int count = 0;
        for (int[] row : grid) {
            for (int cell : row) {
                if (cell == COIN) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean noCoinsLeft() {
        return countCoins() == 0;
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

    private boolean inBounds(int col, int row) {
        return col >= 0 && col < getWidth() && row >= 0 && row < getHeight();
    }

    private static int[][] copyTemplate() {
        int[][] copy = new int[TEMPLATE.length][TEMPLATE[0].length];
        for (int row = 0; row < TEMPLATE.length; row++) {
            System.arraycopy(TEMPLATE[row], 0, copy[row], 0, TEMPLATE[row].length);
        }
        return copy;
    }
}
