package com.picman;

import com.picman.game.CollectibleCollector;
import com.picman.game.GameCollisionHandler;
import com.picman.game.ItemSpawnScheduler;
import com.picman.model.GameSession;
import com.picman.model.GameStatus;
import com.picman.model.Maze;
import com.picman.model.entity.GhostReleaseScheduler;
import com.picman.model.entity.Pacman;
import com.picman.model.entity.Ghost.Ghost;
import com.picman.model.entity.GhostFactory.Cyan_GhostFactory;
import com.picman.model.entity.GhostFactory.Orange_GhostFactory;
import com.picman.model.entity.GhostFactory.Pink_GhostFactory;
import com.picman.model.entity.GhostFactory.Red_GhostFactory;
import com.picman.render.GameRenderer;
import com.picman.render.ViewLayout;
import com.picman.movement.GridMovement;
import com.picman.util.Direction;

import java.awt.Graphics2D;
import java.util.List;

public class Game {
    private final Maze maze = new Maze();
    private final Pacman pacman = new Pacman();
    private final List<Ghost> ghosts = List.of(
            new Red_GhostFactory().createGhost(),
            new Pink_GhostFactory().createGhost(),
            new Cyan_GhostFactory().createGhost(),
            new Orange_GhostFactory().createGhost());
    private final GhostReleaseScheduler ghostReleaseScheduler = new GhostReleaseScheduler();
    private final GameSession session = new GameSession();
    private final GameRenderer renderer = new GameRenderer();
    private final GameCollisionHandler collisionHandler = new GameCollisionHandler();
    private final ItemSpawnScheduler itemSpawnScheduler = new ItemSpawnScheduler();
    private final CollectibleCollector collectibleCollector = new CollectibleCollector(itemSpawnScheduler);

    public Game() {
        ghostReleaseScheduler.reset(ghosts);
        itemSpawnScheduler.reset();
    }

    public void update() {
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

    public void render(Graphics2D g) {
        renderer.render(g, maze, pacman, ghosts, session);
    }

    public void setActiveDirection(Direction direction) {
        if (session.isPlaying()) {
            pacman.setActiveDirection(direction);
        }
    }

    public void restart() {
        maze.reset();
        pacman.reset();
        ghostReleaseScheduler.reset(ghosts);
        itemSpawnScheduler.reset();
        session.reset();
    }

    public GameStatus getStatus() {
        return session.getStatus();
    }

    public int getScore() {
        return session.getScore();
    }

    public int getPelletsCollected() {
        return session.getPelletsCollected();
    }

    public int getGhostsEaten() {
        return session.getGhostsEaten();
    }

    public int getElapsedSeconds() {
        return session.getElapsedSeconds();
    }

    public int getPanelWidth() {
        return ViewLayout.panelWidth(maze);
    }

    public int getPanelHeight() {
        return ViewLayout.panelHeight(maze);
    }
}
