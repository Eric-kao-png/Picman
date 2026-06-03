package com.picman.render;

import com.picman.config.GameConfig;
import com.picman.config.RenderTheme;
import com.picman.model.entity.Ghost;
import com.picman.model.entity.GridPosition;
import com.picman.model.entity.Pacman;

import java.awt.Graphics2D;

public class EntityRenderer {
    private static final int ARC_SIZE = 6;

    public void renderPacman(Graphics2D g, Pacman pacman, boolean visible) {
        if (!visible) {
            return;
        }
        drawCircle(g, pacman.getPosition(), RenderTheme.PACMAN);
    }

    public void renderGhosts(Graphics2D g, java.util.List<Ghost> ghosts) {
        for (Ghost ghost : ghosts) {
            renderGhost(g, ghost);
        }
    }

    public void renderGhost(Graphics2D g, Ghost ghost) {
        GridPosition position = ghost.getPosition();
        int x = (int) Math.round(position.getDrawX());
        int y = ViewLayout.toScreenY(position.getDrawY());
        int margin = GameConfig.ENTITY_DRAW_MARGIN;
        int size = GameConfig.TILE_SIZE - margin * 2;

        g.setColor(ghost.getColor());
        g.fillRoundRect(x + margin, y + margin, size, size, ARC_SIZE, ARC_SIZE);
    }

    private void drawCircle(Graphics2D g, GridPosition position, java.awt.Color color) {
        int x = (int) Math.round(position.getDrawX());
        int y = ViewLayout.toScreenY(position.getDrawY());
        int margin = GameConfig.ENTITY_DRAW_MARGIN;
        int size = GameConfig.TILE_SIZE - margin * 2;

        g.setColor(color);
        g.fillOval(x + margin, y + margin, size, size);
    }
}
