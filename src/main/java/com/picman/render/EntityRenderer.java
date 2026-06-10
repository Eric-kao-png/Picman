package com.picman.render;

import com.picman.config.RenderTheme;
import com.picman.model.entity.Ghost;
import com.picman.model.entity.GridPosition;
import com.picman.model.entity.Pacman;

import java.awt.Graphics2D;
import java.util.List;

public class EntityRenderer {
    private static final int GHOST_CORNER_ARC = 6;

    public void renderPacman(Graphics2D g, Pacman pacman, boolean visible) {
        if (!visible) {
            return;
        }
        drawFilledOval(g, pacman.getPosition(), RenderTheme.PACMAN);
    }

    public void renderGhosts(Graphics2D g, List<Ghost> ghosts) {
        for (Ghost ghost : ghosts) {
            renderGhost(g, ghost);
        }
    }

    public void renderGhost(Graphics2D g, Ghost ghost) {
        EntityDrawBounds bounds = EntityDrawBounds.from(ghost.getPosition());
        g.setColor(ghost.getDisplayColor());
        g.fillRoundRect(
                bounds.innerX(),
                bounds.innerY(),
                bounds.size(),
                bounds.size(),
                GHOST_CORNER_ARC,
                GHOST_CORNER_ARC);
    }

    private void drawFilledOval(Graphics2D g, GridPosition position, java.awt.Color color) {
        EntityDrawBounds bounds = EntityDrawBounds.from(position);
        g.setColor(color);
        g.fillOval(bounds.innerX(), bounds.innerY(), bounds.size(), bounds.size());
    }
}
