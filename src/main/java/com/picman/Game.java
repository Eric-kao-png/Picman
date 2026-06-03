package com.picman;

import com.picman.model.GameSession;
import com.picman.model.GameStatus;
import com.picman.model.Maze;
import com.picman.model.entity.Ghost;
import com.picman.model.entity.Pacman;
import com.picman.render.GameRenderer;
import com.picman.render.ViewLayout;
import com.picman.util.Direction;

import java.awt.Graphics2D;

public class Game {
    private final Maze maze = new Maze();
    private final Pacman pacman = new Pacman();
    private final Ghost ghost = new Ghost();
    private final GameSession session = new GameSession();
    private final GameRenderer renderer = new GameRenderer();

    public void update() {
        if (!session.isPlaying()) {
            return;
        }

        session.tickInvincibility();
        pacman.update(maze);

        if (maze.tryEatCoin(pacman.getCol(), pacman.getRow())) {
            session.onCoinCollected();
        }

        ghost.update(maze, pacman);
        handleGhostCollision();

        if (maze.noCoinsLeft()) {
            session.onAllCoinsCollected();
        }
    }

    public void render(Graphics2D g) {
        renderer.render(g, maze, pacman, ghost, session);
    }

    public void setActiveDirection(Direction direction) {
        if (session.isPlaying()) {
            pacman.setActiveDirection(direction);
        }
    }

    public void restart() {
        maze.reset();
        pacman.reset();
        ghost.reset();
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

    private void handleGhostCollision() {
        if (!ghost.collidesWith(pacman) || session.isInvincible()) {
            return;
        }

        session.onGhostHit();
        if (session.getStatus() == GameStatus.PLAYING) {
            pacman.reset();
            ghost.reset();
        }
    }
}
