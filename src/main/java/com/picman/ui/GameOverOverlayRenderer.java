package com.picman.ui;

import com.picman.model.GameStatus;
import com.picman.render.UiDraw;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class GameOverOverlayRenderer {
    public void render(Graphics2D g2, GameOverViewModel viewModel, GameOverAnimation animation, int width, int height) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        double progress = animation.progress();
        float fade = (float) progress;

        Composite oldComposite = g2.getComposite();
        g2.setComposite(AlphaComposite.SrcOver.derive(fade));
        g2.setColor(GameOverTheme.OVERLAY_COLOR);
        g2.fillRect(0, 0, width, height);
        drawFloatingGhost(g2, width / 2 + 118, Math.max(46, height / 2 - 230), fade);
        g2.setComposite(oldComposite);

        int panelWidth = animation.panelWidth(width);
        int panelHeight = animation.panelHeight(height);
        int panelX = (width - panelWidth) / 2;
        int panelY = animation.animatedPanelY(panelHeight, height, progress);

        g2.setComposite(AlphaComposite.SrcOver.derive(fade));
        drawPanel(g2, panelX, panelY, panelWidth, panelHeight);
        drawDeadPacman(g2, panelX + panelWidth / 2, panelY + 48, 58);

        int centerX = width / 2;
        String title = viewModel.status() == GameStatus.WIN ? "Y O U   W I N" : "G A M E   O V E R";
        UiDraw.drawNeonCenteredText(
                g2, title, GameOverTheme.TITLE_FONT, GameOverTheme.TITLE_RED, GameOverTheme.TITLE_GLOW,
                centerX, panelY + 96);
        UiDraw.drawCenteredText(
                g2, "FINAL SCORE", GameOverTheme.LABEL_FONT, GameOverTheme.MUTED_TEXT, centerX, panelY + 137);
        UiDraw.drawCenteredText(
                g2, String.valueOf(viewModel.score()), GameOverTheme.SCORE_FONT, GameOverTheme.SCORE_YELLOW,
                centerX, panelY + 191);
        drawStats(g2, viewModel, panelX, panelY, panelWidth);
        g2.setComposite(oldComposite);
    }

    private void drawPanel(Graphics2D g2, int x, int y, int width, int height) {
        g2.setColor(GameOverTheme.PANEL_SHADOW);
        g2.fillRoundRect(x + 8, y + 12, width - 16, height - 2, GameOverTheme.PANEL_RADIUS, GameOverTheme.PANEL_RADIUS);

        g2.setColor(GameOverTheme.PANEL_FILL);
        g2.fillRoundRect(x, y, width, height, GameOverTheme.PANEL_RADIUS, GameOverTheme.PANEL_RADIUS);

        g2.setStroke(new BasicStroke(1.6f));
        g2.setColor(GameOverTheme.PANEL_STROKE);
        g2.drawRoundRect(x, y, width - 1, height - 1, GameOverTheme.PANEL_RADIUS, GameOverTheme.PANEL_RADIUS);

        for (int i = 0; i < 6; i++) {
            int inset = 5 + i * 3;
            g2.setStroke(new BasicStroke(1f));
            g2.setColor(new Color(
                    GameOverTheme.PANEL_INNER_GLOW.getRed(),
                    GameOverTheme.PANEL_INNER_GLOW.getGreen(),
                    GameOverTheme.PANEL_INNER_GLOW.getBlue(),
                    Math.max(7, GameOverTheme.PANEL_INNER_GLOW.getAlpha() - i * 6)
            ));
            g2.drawRoundRect(
                    x + inset,
                    y + inset,
                    width - inset * 2 - 1,
                    height - inset * 2 - 1,
                    Math.max(12, GameOverTheme.PANEL_RADIUS - i * 2),
                    Math.max(12, GameOverTheme.PANEL_RADIUS - i * 2)
            );
        }
    }

    private void drawStats(Graphics2D g2, GameOverViewModel viewModel, int panelX, int panelY, int panelWidth) {
        int labelX = panelX + 86;
        int valueX = panelX + panelWidth - 86;
        int y = panelY + 232;

        drawStatRow(g2, "Pellets", String.valueOf(viewModel.pelletsCollected()), labelX, valueX, y);
        drawStatRow(g2, "Ghosts", String.valueOf(viewModel.ghostsEaten()), labelX, valueX, y + 25);
        drawStatRow(g2, "Time", formatTime(viewModel.elapsedSeconds()), labelX, valueX, y + 50);
    }

    private void drawStatRow(Graphics2D g2, String label, String value, int labelX, int valueX, int baselineY) {
        g2.setFont(GameOverTheme.STAT_FONT);
        g2.setColor(GameOverTheme.MUTED_TEXT);
        g2.drawString(label, labelX, baselineY);
        g2.setColor(GameOverTheme.TEXT_WHITE);
        FontMetrics metrics = g2.getFontMetrics();
        g2.drawString(value, valueX - metrics.stringWidth(value), baselineY);
    }

    private void drawFloatingGhost(Graphics2D g2, int x, int y, float overlayFade) {
        Composite oldComposite = g2.getComposite();
        g2.setComposite(AlphaComposite.SrcOver.derive(0.22f * overlayFade));
        int width = 58;
        int height = 66;

        g2.setColor(new Color(180, 220, 255));
        g2.fillRoundRect(x, y, width, height, 28, 28);
        g2.fillRect(x, y + height / 2, width, height / 2 - 9);

        int footY = y + height - 14;
        for (int i = 0; i < 4; i++) {
            g2.fillOval(x + i * 15, footY, 16, 18);
        }

        g2.setColor(Color.WHITE);
        g2.fillOval(x + 13, y + 21, 12, 15);
        g2.fillOval(x + 34, y + 21, 12, 15);
        g2.setColor(new Color(30, 45, 78));
        g2.fillOval(x + 17, y + 26, 5, 5);
        g2.fillOval(x + 38, y + 26, 5, 5);
        g2.setComposite(oldComposite);
    }

    private void drawDeadPacman(Graphics2D g2, int centerX, int centerY, int size) {
        Composite oldComposite = g2.getComposite();
        int radius = size / 2;
        int x = centerX - radius;
        int y = centerY - radius;

        for (int i = 4; i > 0; i--) {
            float alpha = (float) (0.09 * i);
            int glowInset = i * 4;
            g2.setComposite(AlphaComposite.SrcOver.derive(alpha));
            g2.setColor(GameOverTheme.DEAD_PACMAN_GLOW);
            g2.fillOval(x - glowInset, y - glowInset, size + glowInset * 2, size + glowInset * 2);
        }

        g2.setComposite(oldComposite);
        g2.setColor(GameOverTheme.DEAD_PACMAN_FILL);
        g2.fillArc(x, y, size, size, 24, 312);

        g2.setStroke(new BasicStroke(1.4f));
        g2.setColor(GameOverTheme.DEAD_PACMAN_EDGE);
        g2.drawArc(x, y, size, size, 24, 312);

        int eyeCenterX = centerX + size / 10;
        int eyeCenterY = centerY - size / 4;
        int eyeSize = Math.max(7, size / 8);

        g2.setColor(new Color(22, 15, 10));
        g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(eyeCenterX - eyeSize / 2, eyeCenterY - eyeSize / 2,
                eyeCenterX + eyeSize / 2, eyeCenterY + eyeSize / 2);
        g2.drawLine(eyeCenterX + eyeSize / 2, eyeCenterY - eyeSize / 2,
                eyeCenterX - eyeSize / 2, eyeCenterY + eyeSize / 2);
    }

    private static String formatTime(int elapsedSeconds) {
        int minutes = elapsedSeconds / 60;
        int seconds = elapsedSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
