package com.picman;

import com.picman.game.GameCollisionHandler;
import com.picman.game.ItemSpawnScheduler;
import com.picman.model.CollectibleType;
import com.picman.model.GameSession;
import com.picman.model.GameStatus;
import com.picman.model.Maze;
import com.picman.model.entity.Ghost;
import com.picman.model.entity.GhostFactory;
import com.picman.model.entity.GhostReleaseScheduler;
import com.picman.model.entity.Pacman;
import com.picman.render.GameRenderer;
import com.picman.render.ViewLayout;
import com.picman.util.Direction;

import java.awt.Graphics2D;
import java.util.List;

public class Game {
    private final Maze maze = new Maze();
    private final Pacman pacman = new Pacman();
    private final List<Ghost> ghosts = GhostFactory.createAll();
    private final GhostReleaseScheduler ghostReleaseScheduler = new GhostReleaseScheduler();
    private final GameSession session = new GameSession();
    private final GameRenderer renderer = new GameRenderer();
    private final GameCollisionHandler collisionHandler = new GameCollisionHandler();
    private final ItemSpawnScheduler itemSpawnScheduler = new ItemSpawnScheduler();

    public Game() {
        ghostReleaseScheduler.reset(ghosts);
        itemSpawnScheduler.reset();
    }

    public void update() {
        if (!session.isPlaying()) {
            return;
        }

        session.tickInvincibility();
        session.tickPowered();
        pacman.update(maze);

        itemSpawnScheduler.tick(maze, session);
        handleCollectibleAtPacman();

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

    private void handleCollectibleAtPacman() {
        CollectibleType collected = maze.tryEatCollectible(pacman.getCol(), pacman.getRow());
        switch (collected) {
            case COIN -> session.onCoinCollected();
            case POWER_COIN -> {
                session.onPowerCoinCollected();
                itemSpawnScheduler.onItemCollected(pacman.getCol(), pacman.getRow());
            }
            case EXTRA_LIFE_ITEM -> {
                session.onExtraLifeCollected();
                itemSpawnScheduler.onItemCollected(pacman.getCol(), pacman.getRow());
            }
            case NONE -> {
            }
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

    public int getPanelWidth() {
        return ViewLayout.panelWidth(maze);
    }

    public int getPanelHeight() {
        return ViewLayout.panelHeight(maze);
    }
}
