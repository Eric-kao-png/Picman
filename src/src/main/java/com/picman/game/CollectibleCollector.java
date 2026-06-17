package com.picman.game;

import com.picman.model.CollectibleType;
import com.picman.model.GameSession;
import com.picman.model.Maze;
import com.picman.model.entity.Pacman;

/**
 * 處理 Pac-Man 吃到收集物後對 {@link GameSession} 與道具排程的副作用。
 */
public class CollectibleCollector {
    private final ItemSpawnScheduler itemSpawnScheduler;

    public CollectibleCollector(ItemSpawnScheduler itemSpawnScheduler) {
        this.itemSpawnScheduler = itemSpawnScheduler;
    }

    public void collectAt(Maze maze, Pacman pacman, GameSession session) {
        CollectibleType collected = maze.tryEatCollectible(pacman.getCol(), pacman.getRow());
        apply(collected, session, pacman.getCol(), pacman.getRow());
    }

    private void apply(CollectibleType collected, GameSession session, int col, int row) {
        switch (collected) {
            case COIN -> session.onCoinCollected();
            case POWER_COIN -> {
                session.onPowerCoinCollected();
                itemSpawnScheduler.onItemCollected(col, row);
            }
            case EXTRA_LIFE_ITEM -> {
                session.onExtraLifeCollected();
                itemSpawnScheduler.onItemCollected(col, row);
            }
            case PICKAXE_ITEM -> {
                session.onPickaxeCollected();
                itemSpawnScheduler.onItemCollected(col, row);
            }
            case NONE -> {
            }
        }
    }
}
