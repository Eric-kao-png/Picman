package com.picman.integration;

import com.picman.game.CollectibleCollector;
import com.picman.game.GameCollisionHandler;
import com.picman.game.ItemSpawnScheduler;
import com.picman.model.GameSession;
import com.picman.model.Maze;
import com.picman.model.entity.GhostKind;
import com.picman.model.entity.GhostMode;
import com.picman.model.entity.GhostRegistry;
import com.picman.model.entity.GhostReleaseScheduler;
import com.picman.model.entity.GhostSpawnInfo;
import com.picman.model.entity.Pacman;
import com.picman.model.entity.ghost.Ghost;
import com.picman.model.entity.ghostFactory.GhostAssembly;
import com.picman.movement.GridMovement;

import java.util.List;

/**
 * 測試用更新管線，與 {@link com.picman.Game#update()} 相同順序，並暴露內部元件供整合測試斷言。
 */
final class GameUpdatePipeline {
    final Maze maze = new Maze();
    final Pacman pacman = new Pacman();
    final List<Ghost> ghosts;
    final GhostReleaseScheduler ghostReleaseScheduler = new GhostReleaseScheduler();
    final GameSession session = new GameSession();
    final GameCollisionHandler collisionHandler = new GameCollisionHandler();
    final ItemSpawnScheduler itemSpawnScheduler = new ItemSpawnScheduler();
    final CollectibleCollector collectibleCollector = new CollectibleCollector(itemSpawnScheduler);

    GameUpdatePipeline() {
        GhostRegistry.getInstance().reset();
        GhostSpawnInfo.initializeDefaultGhosts();
        ghosts = GhostAssembly.createAll();
        ghostReleaseScheduler.reset(ghosts);
        itemSpawnScheduler.reset();
    }

    void update() {
        if (!session.isPlaying()) {
            return;
        }

        session.tickElapsed();
        session.tickInvincibility();
        session.tickPowered();
        session.tickPickaxe();
        maze.tickBrokenWalls();
        GridMovement.ejectFromSolidCell(maze, pacman.getPosition());
        pacman.update(maze, session.isPickaxeActive());
        GridMovement.ejectFromSolidCell(maze, pacman.getPosition());

        itemSpawnScheduler.tick(maze, session);
        collectibleCollector.collectAt(maze, pacman, session);

        ghostReleaseScheduler.tick(ghosts);
        releaseWhiteGhostIfScoreThreshold();
        syncGhostFrightenedState();
        for (Ghost ghost : ghosts) {
            ghost.update(maze, pacman, ghosts);
        }
        collisionHandler.resolve(pacman, ghosts, session, ghostReleaseScheduler);

        if (maze.noCoinsLeft()) {
            session.onAllCoinsCollected();
        }
    }

    private void syncGhostFrightenedState() {
        if (session.isPowered()) {
            ghosts.forEach(Ghost::enterFrightened);
        } else {
            ghosts.forEach(Ghost::exitFrightened);
        }
    }

    private void releaseWhiteGhostIfScoreThreshold() {
        if (session.getScore() > 2000) {
            int whiteGhostIndex = GhostKind.index(GhostKind.WHITE);
            if (whiteGhostIndex >= 0 && whiteGhostIndex < ghosts.size()) {
                Ghost whiteGhost = ghosts.get(whiteGhostIndex);
                if (whiteGhost.getMode() == GhostMode.WAITING) {
                    whiteGhost.releaseFromHouse();
                }
            }
        }
    }
}
