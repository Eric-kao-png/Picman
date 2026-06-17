package com.picman.game;

import com.picman.config.ItemSpawnConfig;
import com.picman.level.SpawnCellSelector;
import com.picman.model.GameSession;
import com.picman.model.ItemType;
import com.picman.model.Maze;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 依時間在地圖上隨機生成道具，並在逾時後清除。
 */
public class ItemSpawnScheduler {
    private final Random random = new Random();
    private int spawnCooldownTicks = ItemSpawnConfig.INITIAL_SPAWN_DELAY_TICKS;
    private ActiveItem activeItem;

    public void reset() {
        spawnCooldownTicks = ItemSpawnConfig.INITIAL_SPAWN_DELAY_TICKS;
        activeItem = null;
    }

    public void tick(Maze maze, GameSession session) {
        tickActiveItem(maze);
        if (activeItem != null) {
            return;
        }
        if (spawnCooldownTicks > 0) {
            spawnCooldownTicks--;
            return;
        }
        trySpawnRandomItem(maze, session);
        spawnCooldownTicks = ItemSpawnConfig.SPAWN_INTERVAL_TICKS;
    }

    public void onItemCollected(int col, int row) {
        if (activeItem == null) {
            return;
        }
        if (activeItem.col == col && activeItem.row == row) {
            activeItem = null;
        }
    }

    private void tickActiveItem(Maze maze) {
        if (activeItem == null) {
            return;
        }
        activeItem.remainingTicks--;
        if (activeItem.remainingTicks <= 0) {
            maze.clearSpawnedItem(activeItem.col, activeItem.row, activeItem.type.getCellType());
            activeItem = null;
        }
    }

    private void trySpawnRandomItem(Maze maze, GameSession session) {
        ItemType type = pickSpawnType(session);
        if (type == null) {
            return;
        }
        int[] cell = SpawnCellSelector.pickRandomEmptyCell(maze, random);
        if (cell == null) {
            return;
        }
        if (!maze.placeSpawnedItem(cell[0], cell[1], type.getCellType())) {
            return;
        }
        activeItem = new ActiveItem(cell[0], cell[1], type, ItemSpawnConfig.ITEM_DURATION_TICKS);
    }

    private ItemType pickSpawnType(GameSession session) {
        List<ItemType> candidates = new ArrayList<>();
        candidates.add(ItemType.TEMP_POWER_COIN);
        candidates.add(ItemType.PICKAXE);
        if (session.getLives() < ItemSpawnConfig.MAX_LIVES) {
            candidates.add(ItemType.EXTRA_LIFE);
        }
        return candidates.get(random.nextInt(candidates.size()));
    }

    private static final class ActiveItem {
        private final int col;
        private final int row;
        private final ItemType type;
        private int remainingTicks;

        private ActiveItem(int col, int row, ItemType type, int remainingTicks) {
            this.col = col;
            this.row = row;
            this.type = type;
            this.remainingTicks = remainingTicks;
        }
    }
}
