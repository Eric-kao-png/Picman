package com.picman.model;

import com.picman.config.PowerCoinConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MazeTest {
    private static final int COIN_COL = 1;
    private static final int COIN_ROW = 1;
    private static final int EMPTY_COL = 14;
    private static final int EMPTY_ROW = 14;
    private static final int WALL_COL = 0;
    private static final int WALL_ROW = 0;

    private Maze maze;

    @BeforeEach
    void setUp() {
        maze = new Maze();
    }

    @Test
    void tryEatCollectible_outOfBounds_returnsNone() {
        assertEquals(CollectibleType.NONE, maze.tryEatCollectible(-1, 0));
        assertEquals(CollectibleType.NONE, maze.tryEatCollectible(0, maze.getHeight()));
    }

    @Test
    void tryEatCollectible_wall_returnsNone() {
        assertEquals(CollectibleType.NONE, maze.tryEatCollectible(WALL_COL, WALL_ROW));
        assertEquals(CellType.WALL, maze.getCellType(WALL_COL, WALL_ROW));
    }

    @Test
    void tryEatCollectible_emptyCell_returnsNone() {
        assertEquals(CellType.EMPTY, maze.getCellType(EMPTY_COL, EMPTY_ROW));

        assertEquals(CollectibleType.NONE, maze.tryEatCollectible(EMPTY_COL, EMPTY_ROW));
        assertEquals(CellType.EMPTY, maze.getCellType(EMPTY_COL, EMPTY_ROW));
    }

    @Test
    void tryEatCollectible_coin_clearsCell() {
        assertEquals(CellType.COIN, maze.getCellType(COIN_COL, COIN_ROW));

        assertEquals(CollectibleType.COIN, maze.tryEatCollectible(COIN_COL, COIN_ROW));
        assertEquals(CellType.EMPTY, maze.getCellType(COIN_COL, COIN_ROW));
        assertEquals(CollectibleType.NONE, maze.tryEatCollectible(COIN_COL, COIN_ROW));
    }

    @Test
    void tryEatCollectible_powerCoin_clearsCell() {
        int col = PowerCoinConfig.POSITIONS[0][0];
        int row = PowerCoinConfig.POSITIONS[0][1];

        assertEquals(CellType.POWER_COIN, maze.getCellType(col, row));

        assertEquals(CollectibleType.POWER_COIN, maze.tryEatCollectible(col, row));
        assertEquals(CellType.EMPTY, maze.getCellType(col, row));
    }

    @Test
    void tryEatCollectible_spawnedExtraLifeItem_clearsCell() {
        assertTrue(maze.placeSpawnedItem(EMPTY_COL, EMPTY_ROW, CellType.EXTRA_LIFE_ITEM));

        assertEquals(CollectibleType.EXTRA_LIFE_ITEM, maze.tryEatCollectible(EMPTY_COL, EMPTY_ROW));
        assertEquals(CellType.EMPTY, maze.getCellType(EMPTY_COL, EMPTY_ROW));
    }

    @Test
    void tryEatCollectible_spawnedPickaxeItem_clearsCell() {
        assertTrue(maze.placeSpawnedItem(EMPTY_COL, EMPTY_ROW, CellType.PICKAXE_ITEM));

        assertEquals(CollectibleType.PICKAXE_ITEM, maze.tryEatCollectible(EMPTY_COL, EMPTY_ROW));
        assertEquals(CellType.EMPTY, maze.getCellType(EMPTY_COL, EMPTY_ROW));
    }

    @Test
    void tryEatCollectible_tempPowerCoin_mapsToPowerCoin() {
        assertTrue(maze.placeSpawnedItem(EMPTY_COL, EMPTY_ROW, CellType.TEMP_POWER_COIN));

        assertEquals(CollectibleType.POWER_COIN, maze.tryEatCollectible(EMPTY_COL, EMPTY_ROW));
        assertEquals(CellType.EMPTY, maze.getCellType(EMPTY_COL, EMPTY_ROW));
    }

    @Test
    void placeSpawnedItem_failsOnOccupiedCell() {
        assertFalse(maze.placeSpawnedItem(COIN_COL, COIN_ROW, CellType.PICKAXE_ITEM));
        assertEquals(CellType.COIN, maze.getCellType(COIN_COL, COIN_ROW));
    }
}
