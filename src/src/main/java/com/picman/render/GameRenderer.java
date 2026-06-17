package com.picman.render;

import com.picman.config.RenderTheme;
import com.picman.model.GameSession;
import com.picman.model.Maze;
import com.picman.model.entity.Pacman;
import com.picman.model.entity.ghost.Ghost;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;

public class GameRenderer {
    private final HudRenderer hudRenderer = new HudRenderer();
    private final MazeRenderer mazeRenderer = new MazeRenderer();
    private final EntityRenderer entityRenderer = new EntityRenderer();

    public void render(
            Graphics2D g,
            Maze maze,
            Pacman pacman,
            List<Ghost> ghosts,
            GameSession session) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int panelWidth = ViewLayout.panelWidth(maze);
        int panelHeight = ViewLayout.panelHeight(maze);

        g.setColor(RenderTheme.BACKGROUND);
        g.fillRect(0, 0, panelWidth, panelHeight);

        hudRenderer.render(g, session);
        mazeRenderer.render(g, maze);
        entityRenderer.renderGhosts(g, ghosts);
        entityRenderer.renderPacman(g, pacman, InvincibilityVisual.isPacmanVisible(session));
    }
}
