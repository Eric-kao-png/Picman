package com.picman.integration;

import com.picman.config.PowerCoinConfig;
import com.picman.model.CellType;
import com.picman.model.GameStatus;
import com.picman.model.entity.GhostMode;
import com.picman.model.entity.ghost.Ghost;
import com.picman.util.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameUpdatePipelineIntegrationTest {
    private static final int EMPTY_COL = 14;
    private static final int EMPTY_ROW = 14;
    private static final int BREAKABLE_WALL_COL = 2;
    private static final int BREAKABLE_WALL_ROW = 2;
    private static final int APPROACH_WALL_COL = 1;
    private static final int APPROACH_WALL_ROW = 2;

    private GameUpdatePipeline pipeline;

    @BeforeEach
    void setUp() {
        pipeline = new GameUpdatePipeline();
    }

    @Test
    void collectingAllCoins_setsWinStatus() {
        eatAllCoins();

        pipeline.update();

        assertEquals(GameStatus.WIN, pipeline.session.getStatus());
    }

    @Test
    void powerCoin_makesGhostEdibleAndEatable() {
        Ghost ghost = pipeline.ghosts.get(0);
        ghost.releaseFromHouse();

        movePacmanTo(PowerCoinConfig.POSITIONS[0][0], PowerCoinConfig.POSITIONS[0][1]);
        pipeline.update();

        assertTrue(pipeline.session.isPowered());
        assertTrue(ghost.isEdibleByPacman());

        alignPacmanWith(ghost);
        pipeline.update();

        assertEquals(1, pipeline.session.getGhostsEaten());
        assertEquals(GhostMode.WAITING, ghost.getMode());
    }

    @Test
    void spawnedPickaxe_activatesPickaxeOnCollection() {
        assertTrue(pipeline.maze.placeSpawnedItem(EMPTY_COL, EMPTY_ROW, CellType.PICKAXE_ITEM));
        movePacmanTo(EMPTY_COL, EMPTY_ROW);

        pipeline.update();

        assertTrue(pipeline.session.isPickaxeActive());
        assertEquals(CellType.EMPTY, pipeline.maze.getCellType(EMPTY_COL, EMPTY_ROW));
    }

    @Test
    void pickaxe_allowsBreakingWallWhileMoving() {
        assertTrue(pipeline.maze.placeSpawnedItem(EMPTY_COL, EMPTY_ROW, CellType.PICKAXE_ITEM));
        movePacmanTo(EMPTY_COL, EMPTY_ROW);
        pipeline.update();
        assertTrue(pipeline.session.isPickaxeActive());

        movePacmanTo(APPROACH_WALL_COL, APPROACH_WALL_ROW);
        pipeline.pacman.setActiveDirection(Direction.RIGHT);

        for (int i = 0; i < 40; i++) {
            pipeline.update();
        }

        assertTrue(pipeline.maze.isBrokenWall(BREAKABLE_WALL_COL, BREAKABLE_WALL_ROW));
    }

    @Test
    void itemSpawnScheduler_placesAndClearsItemsOverTime() {
        advanceUntilItemSpawns();

        assertTrue(hasSpawnedItemOnMap());

        for (int i = 0; i < 500; i++) {
            pipeline.update();
        }

        assertFalse(hasSpawnedItemOnMap());
    }

    private void eatAllCoins() {
        for (int row = 0; row < pipeline.maze.getHeight(); row++) {
            for (int col = 0; col < pipeline.maze.getWidth(); col++) {
                if (pipeline.maze.getCellType(col, row) == CellType.COIN) {
                    movePacmanTo(col, row);
                    pipeline.maze.tryEatCollectible(col, row);
                    pipeline.session.onCoinCollected();
                }
            }
        }
    }

    private void advanceUntilItemSpawns() {
        for (int i = 0; i < 700; i++) {
            pipeline.update();
            if (hasSpawnedItemOnMap()) {
                return;
            }
        }
        throw new AssertionError("No spawned item appeared within expected ticks");
    }

    private boolean hasSpawnedItemOnMap() {
        for (int row = 0; row < pipeline.maze.getHeight(); row++) {
            for (int col = 0; col < pipeline.maze.getWidth(); col++) {
                CellType type = pipeline.maze.getCellType(col, row);
                if (type == CellType.EXTRA_LIFE_ITEM
                        || type == CellType.PICKAXE_ITEM
                        || type == CellType.TEMP_POWER_COIN) {
                    return true;
                }
            }
        }
        return false;
    }

    private void movePacmanTo(int col, int row) {
        pipeline.pacman.getPosition().snapToCell(col, row);
    }

    private void alignPacmanWith(Ghost ghost) {
        pipeline.pacman.getPosition().setCenter(
                ghost.getPosition().getCenterX(),
                ghost.getPosition().getCenterY());
    }
}
