package com.picman.render;

import com.picman.config.GameConfig;
import com.picman.model.GameSession;
import com.picman.model.GameStatus;
import com.picman.model.Maze;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class OverlayRenderer {
    public void render(Graphics2D g, GameSession session, Maze maze, int panelWidth, int panelHeight) {
        if (session.isPlaying()) {
            return;
        }

        g.setColor(com.picman.config.RenderTheme.OVERLAY);
        g.fillRect(0, ViewLayout.mazeOffsetY(), panelWidth, panelHeight - ViewLayout.mazeOffsetY());

        String message = session.getStatus() == GameStatus.WIN ? "YOU WIN!" : "GAME OVER";
        int centerY = ViewLayout.mazeOffsetY() + (maze.getHeight() * GameConfig.TILE_SIZE) / 2;

        g.setColor(Color.WHITE);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
        drawCentered(g, message, centerY, panelWidth);

        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        drawCentered(g, "Press R to restart", centerY + 28, panelWidth);
    }

    private void drawCentered(Graphics2D g, String text, int y, int panelWidth) {
        int textWidth = g.getFontMetrics().stringWidth(text);
        g.drawString(text, (panelWidth - textWidth) / 2, y);
    }
}
