package com.picman.model;

import com.picman.level.GhostHouseGeometry;
import com.picman.level.LevelInitializer;
import com.picman.maze.BreakableWallRules;
import com.picman.maze.BrokenWallTracker;
import com.picman.maze.MazeNavigator;
import com.picman.util.Direction;

import java.util.List;

public class Maze {
    private final int[][] grid;
    private final BrokenWallTracker brokenWalls = new BrokenWallTracker();

    public Maze() {
        grid = LevelInitializer.createPlayfieldGrid();
    }

    public void reset() {
        int[][] fresh = LevelInitializer.createPlayfieldGrid();
        for (int row = 0; row < grid.length; row++) {
            System.arraycopy(fresh[row], 0, grid[row], 0, grid[row].length);
        }
        brokenWalls.reset();
    }

    public void tickBrokenWalls() {
        brokenWalls.tick();
    }

    public boolean isBrokenWall(int col, int row) {
        return brokenWalls.isBroken(col, row);
    }

    public boolean canBreakWall(int col, int row) {
        return BreakableWallRules.canBreak(this, col, row);
    }

    public void breakWall(int col, int row) {
        if (canBreakWall(col, row)) {
            brokenWalls.breakWall(col, row);
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

    /** Pac-Man 專用：已破壞牆格與非牆格可走；未破壞的牆格不可站立。 */
    public boolean isWalkableForPacman(int col, int row) {
        if (!inBounds(col, row)) {
            return false;
        }
        if (isBrokenWall(col, row)) {
            return true;
        }
        return getCellType(col, row) != CellType.WALL;
    }

    /** 牆體恢復進度，供渲染與碰撞同步（0=剛破壞，1=實心牆）。 */
    public float getWallRecoveryProgress(int col, int row) {
        return brokenWalls.getRecoveryProgress(col, row);
    }

    public CollectibleType tryEatCollectible(int col, int row) {
        if (!inBounds(col, row)) {
            return CollectibleType.NONE;
        }
        return getCellType(col, row)
                .asCollectible()
                .map(collectible -> {
                    grid[row][col] = CellType.EMPTY.getCode();
                    return collectible;
                })
                .orElse(CollectibleType.NONE);
    }

    public boolean placeSpawnedItem(int col, int row, CellType itemType) {
        if (!inBounds(col, row) || getCellType(col, row) != CellType.EMPTY) {
            return false;
        }
        grid[row][col] = itemType.getCode();
        return true;
    }

    public void clearSpawnedItem(int col, int row, CellType itemType) {
        if (!inBounds(col, row) || getCellType(col, row) != itemType) {
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
