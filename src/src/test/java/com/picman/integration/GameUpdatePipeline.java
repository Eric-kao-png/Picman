package com.picman.integration;

import com.picman.game.CollectibleCollector;
import com.picman.game.GameCollisionHandler;
import com.picman.game.GameLoop;
import com.picman.game.GameWorld;
import com.picman.game.ItemSpawnScheduler;
import com.picman.model.GameSession;
import com.picman.model.Maze;
import com.picman.model.entity.GhostRegistry;
import com.picman.model.entity.GhostReleaseScheduler;
import com.picman.model.entity.GhostSpawnInfo;
import com.picman.model.entity.Pacman;
import com.picman.model.entity.ghost.Ghost;

import java.util.List;

/**
 * 測試用更新管線：暴露內部元件供整合測試斷言，
 * 執行邏輯與 {@link com.picman.Game#update()} 完全相同（均 delegate 給 {@link GameLoop}）。
 */
final class GameUpdatePipeline {

    // 暴露給測試使用的子系統引用
    final GameWorld world;
    final Maze maze;
    final Pacman pacman;
    final List<Ghost> ghosts;
    final GhostReleaseScheduler ghostReleaseScheduler = new GhostReleaseScheduler();
    final GameSession session = new GameSession();
    final GameCollisionHandler collisionHandler = new GameCollisionHandler();
    final ItemSpawnScheduler itemSpawnScheduler = new ItemSpawnScheduler();
    final CollectibleCollector collectibleCollector = new CollectibleCollector(itemSpawnScheduler);

    private final GameLoop gameLoop = new GameLoop();

    GameUpdatePipeline() {
        GhostRegistry.getInstance().reset();
        GhostSpawnInfo.initializeDefaultGhosts();

        world = new GameWorld();
        maze = world.getMaze();
        pacman = world.getPacman();
        ghosts = world.getGhosts();

        ghostReleaseScheduler.reset(ghosts);
        itemSpawnScheduler.reset();
    }

    void update() {
        gameLoop.tick(world, session, ghostReleaseScheduler, itemSpawnScheduler, collectibleCollector);
    }
}
