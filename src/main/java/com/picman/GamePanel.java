package com.picman;

import com.picman.config.GameConfig;
import com.picman.config.RenderTheme;
import com.picman.input.GameKeyBindings;
import com.picman.input.KeyboardInput;
import com.picman.model.GameStatus;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GamePanel extends JPanel {
    private static final Color OVERLAY_COLOR = new Color(0, 0, 0, 191);
    private static final Color PANEL_FILL = new Color(10, 10, 30, 220);
    private static final Color PANEL_STROKE = new Color(92, 190, 255, 150);
    private static final Color PANEL_INNER_GLOW = new Color(100, 210, 255, 45);
    private static final Color PANEL_SHADOW = new Color(0, 0, 0, 120);
    private static final Color TITLE_RED = new Color(255, 64, 64);
    private static final Color TITLE_GLOW = new Color(255, 48, 64, 90);
    private static final Color TEXT_WHITE = new Color(236, 241, 248);
    private static final Color MUTED_TEXT = new Color(154, 164, 188);
    private static final Color SCORE_YELLOW = new Color(255, 214, 76);
    private static final Color DEAD_PACMAN_FILL = new Color(218, 166, 34);
    private static final Color DEAD_PACMAN_EDGE = new Color(255, 220, 86, 130);
    private static final Color DEAD_PACMAN_GLOW = new Color(255, 208, 62, 42);
    private static final Color PRIMARY_BUTTON = new Color(239, 196, 48);
    private static final Color PRIMARY_BUTTON_HOVER = new Color(255, 222, 92);
    private static final Color PRIMARY_TEXT = new Color(20, 18, 12);
    private static final Color SECONDARY_BUTTON = new Color(16, 24, 52);
    private static final Color SECONDARY_BUTTON_HOVER = new Color(28, 42, 82);
    private static final Color SECONDARY_STROKE = new Color(80, 112, 170, 125);

    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 25);
    private static final Font LABEL_FONT = new Font("SansSerif", Font.BOLD, 12);
    private static final Font SCORE_FONT = new Font("SansSerif", Font.BOLD, 52);
    private static final Font STAT_FONT = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 15);

    private static final int PANEL_RADIUS = 26;
    private static final int BUTTON_WIDTH = 240;
    private static final int BUTTON_HEIGHT = 54;
    private static final int BUTTON_GAP = 12;
    private static final long OVERLAY_FADE_MS = 300L;
    private static final long BUTTON_STAGGER_MS = 90L;

    private final Game game;
    private final Runnable onReturnToMenu;
    private final KeyboardInput keyboardInput = new KeyboardInput();
    private final Timer timer;
    private final GameOverButton restartButton;
    private final GameOverButton menuButton;

    private long gameOverShownAt;
    private boolean gameOverVisibleLastFrame;

    public GamePanel(Game game, Runnable onReturnToMenu) {
        this.game = game;
        this.onReturnToMenu = onReturnToMenu;
        setPreferredSize(new Dimension(game.getPanelWidth(), game.getPanelHeight()));
        setFocusable(true);
        setBackground(RenderTheme.BACKGROUND);
        setLayout(null);
        addKeyListener(new GameKeyBindings(game, keyboardInput));

        restartButton = createGameOverButton("RESTART", true);
        menuButton = createGameOverButton("MAIN MENU", false);
        restartButton.addActionListener(e -> restartGame());
        menuButton.addActionListener(e -> returnToMainMenu());
        add(restartButton);
        add(menuButton);
        hideGameOverButtons();

        timer = new Timer(GameConfig.TICK_MS, e -> tick());
    }

    public void startGame() {
        game.restart();
        keyboardInput.clear();
        resetGameOverAnimation();
        hideGameOverButtons();
        if (!timer.isRunning()) {
            timer.start();
        }
        requestFocusInWindow();
    }

    public void stopGame() {
        if (timer.isRunning()) {
            timer.stop();
        }
    }

    @Override
    public void doLayout() {
        super.doLayout();
        layoutGameOverButtons(animationProgress());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        game.render(g2);
        if (isGameOverVisible()) {
            ensureGameOverAnimationStarted();
            drawGameOverOverlay(g2);
        } else {
            gameOverVisibleLastFrame = false;
        }
        g2.dispose();
    }

    private void tick() {
        game.setActiveDirection(keyboardInput.getActiveDirection());
        game.update();
        updateGameOverButtons();
        repaint();
    }

    private boolean isGameOverVisible() {
        return game.getStatus() != GameStatus.PLAYING;
    }

    private void updateGameOverButtons() {
        if (isGameOverVisible()) {
            ensureGameOverAnimationStarted();
            showGameOverButtons();
        } else {
            resetGameOverAnimation();
            hideGameOverButtons();
        }
    }

    private void ensureGameOverAnimationStarted() {
        if (!gameOverVisibleLastFrame) {
            gameOverShownAt = System.currentTimeMillis();
            gameOverVisibleLastFrame = true;
        }
    }

    private void resetGameOverAnimation() {
        gameOverShownAt = 0L;
        gameOverVisibleLastFrame = false;
        restartButton.setAnimationAlpha(0f);
        menuButton.setAnimationAlpha(0f);
    }

    private void showGameOverButtons() {
        if (!restartButton.isVisible()) {
            restartButton.setVisible(true);
            menuButton.setVisible(true);
        }
        layoutGameOverButtons(animationProgress());
    }

    private void hideGameOverButtons() {
        restartButton.setVisible(false);
        menuButton.setVisible(false);
    }

    private void restartGame() {
        game.restart();
        keyboardInput.clear();
        resetGameOverAnimation();
        hideGameOverButtons();
        repaint();
        requestFocusInWindow();
    }

    private void returnToMainMenu() {
        game.restart();
        keyboardInput.clear();
        resetGameOverAnimation();
        hideGameOverButtons();
        repaint();
        onReturnToMenu.run();
    }

    private GameOverButton createGameOverButton(String text, boolean primary) {
        GameOverButton button = new GameOverButton(text, primary);
        button.setFont(BUTTON_FONT);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setHoverTarget(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setHoverTarget(false);
            }
        });
        return button;
    }

    private void layoutGameOverButtons(double panelProgress) {
        int panelWidth = gameOverPanelWidth();
        int panelHeight = gameOverPanelHeight();
        int panelX = (getWidth() - panelWidth) / 2;
        int panelY = animatedPanelY(panelHeight, panelProgress);
        int buttonX = panelX + (panelWidth - BUTTON_WIDTH) / 2;
        int firstButtonY = panelY + panelHeight - 130;

        restartButton.setBounds(buttonX, firstButtonY, BUTTON_WIDTH, BUTTON_HEIGHT);
        menuButton.setBounds(buttonX, firstButtonY + BUTTON_HEIGHT + BUTTON_GAP, BUTTON_WIDTH, BUTTON_HEIGHT);
        restartButton.setAnimationAlpha(buttonProgress(BUTTON_STAGGER_MS));
        menuButton.setAnimationAlpha(buttonProgress(BUTTON_STAGGER_MS * 2));
    }

    private void drawGameOverOverlay(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        double progress = animationProgress();
        float fade = (float) progress;

        Composite oldComposite = g2.getComposite();
        g2.setComposite(AlphaComposite.SrcOver.derive(fade));
        g2.setColor(OVERLAY_COLOR);
        g2.fillRect(0, 0, getWidth(), getHeight());
        drawFloatingGhost(g2, getWidth() / 2 + 118, Math.max(46, getHeight() / 2 - 230), fade);
        g2.setComposite(oldComposite);

        int panelWidth = gameOverPanelWidth();
        int panelHeight = gameOverPanelHeight();
        int panelX = (getWidth() - panelWidth) / 2;
        int panelY = animatedPanelY(panelHeight, progress);

        g2.setComposite(AlphaComposite.SrcOver.derive(fade));
        drawPanel(g2, panelX, panelY, panelWidth, panelHeight);
        drawDeadPacman(g2, panelX + panelWidth / 2, panelY + 48, 58);

        int centerX = getWidth() / 2;
        String title = game.getStatus() == GameStatus.WIN ? "Y O U   W I N" : "G A M E   O V E R";
        drawNeonCenteredText(g2, title, TITLE_FONT, TITLE_RED, TITLE_GLOW, centerX, panelY + 96);
        drawCenteredText(g2, "FINAL SCORE", LABEL_FONT, MUTED_TEXT, centerX, panelY + 137);
        drawCenteredText(g2, String.valueOf(game.getScore()), SCORE_FONT, SCORE_YELLOW, centerX, panelY + 191);
        drawStats(g2, panelX, panelY, panelWidth);
        g2.setComposite(oldComposite);
    }

    private void drawPanel(Graphics2D g2, int x, int y, int width, int height) {
        g2.setColor(PANEL_SHADOW);
        g2.fillRoundRect(x + 8, y + 12, width - 16, height - 2, PANEL_RADIUS, PANEL_RADIUS);

        g2.setColor(PANEL_FILL);
        g2.fillRoundRect(x, y, width, height, PANEL_RADIUS, PANEL_RADIUS);

        g2.setStroke(new BasicStroke(1.6f));
        g2.setColor(PANEL_STROKE);
        g2.drawRoundRect(x, y, width - 1, height - 1, PANEL_RADIUS, PANEL_RADIUS);

        for (int i = 0; i < 6; i++) {
            int inset = 5 + i * 3;
            g2.setStroke(new BasicStroke(1f));
            g2.setColor(new Color(
                    PANEL_INNER_GLOW.getRed(),
                    PANEL_INNER_GLOW.getGreen(),
                    PANEL_INNER_GLOW.getBlue(),
                    Math.max(7, PANEL_INNER_GLOW.getAlpha() - i * 6)
            ));
            g2.drawRoundRect(
                    x + inset,
                    y + inset,
                    width - inset * 2 - 1,
                    height - inset * 2 - 1,
                    Math.max(12, PANEL_RADIUS - i * 2),
                    Math.max(12, PANEL_RADIUS - i * 2)
            );
        }
    }

    private void drawStats(Graphics2D g2, int panelX, int panelY, int panelWidth) {
        int labelX = panelX + 86;
        int valueX = panelX + panelWidth - 86;
        int y = panelY + 232;

        drawStatRow(g2, "Pellets", String.valueOf(game.getPelletsCollected()), labelX, valueX, y);
        drawStatRow(g2, "Ghosts", String.valueOf(game.getGhostsEaten()), labelX, valueX, y + 25);
        drawStatRow(g2, "Time", formatTime(game.getElapsedSeconds()), labelX, valueX, y + 50);
    }

    private void drawStatRow(Graphics2D g2, String label, String value, int labelX, int valueX, int baselineY) {
        g2.setFont(STAT_FONT);
        g2.setColor(MUTED_TEXT);
        g2.drawString(label, labelX, baselineY);
        g2.setColor(TEXT_WHITE);
        FontMetrics metrics = g2.getFontMetrics();
        g2.drawString(value, valueX - metrics.stringWidth(value), baselineY);
    }

    private void drawNeonCenteredText(
            Graphics2D g2,
            String text,
            Font font,
            Color color,
            Color glow,
            int centerX,
            int baselineY) {
        g2.setFont(font);
        FontMetrics metrics = g2.getFontMetrics();
        int x = centerX - metrics.stringWidth(text) / 2;

        g2.setColor(glow);
        g2.drawString(text, x - 2, baselineY);
        g2.drawString(text, x + 2, baselineY);
        g2.drawString(text, x, baselineY - 2);
        g2.drawString(text, x, baselineY + 2);

        g2.setColor(color);
        g2.drawString(text, x, baselineY);
    }

    private void drawCenteredText(Graphics2D g2, String text, Font font, Color color, int centerX, int baselineY) {
        g2.setFont(font);
        g2.setColor(color);
        FontMetrics metrics = g2.getFontMetrics();
        g2.drawString(text, centerX - metrics.stringWidth(text) / 2, baselineY);
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
            g2.setColor(DEAD_PACMAN_GLOW);
            g2.fillOval(x - glowInset, y - glowInset, size + glowInset * 2, size + glowInset * 2);
        }

        g2.setComposite(oldComposite);
        g2.setColor(DEAD_PACMAN_FILL);
        g2.fillArc(x, y, size, size, 24, 312);

        g2.setStroke(new BasicStroke(1.4f));
        g2.setColor(DEAD_PACMAN_EDGE);
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

    private int gameOverPanelWidth() {
        return Math.min(390, Math.max(320, getWidth() - 58));
    }

    private int gameOverPanelHeight() {
        return Math.min(390, Math.max(360, getHeight() - 92));
    }

    private int animatedPanelY(int panelHeight, double progress) {
        int targetY = (getHeight() - panelHeight) / 2;
        return targetY - (int) Math.round((1.0 - easeOutCubic(progress)) * 34.0);
    }

    private double animationProgress() {
        if (gameOverShownAt == 0L) {
            return 0.0;
        }
        return clamp((System.currentTimeMillis() - gameOverShownAt) / (double) OVERLAY_FADE_MS);
    }

    private float buttonProgress(long delayMs) {
        if (gameOverShownAt == 0L) {
            return 0f;
        }
        double raw = (System.currentTimeMillis() - gameOverShownAt - delayMs) / (double) OVERLAY_FADE_MS;
        return (float) easeOutCubic(clamp(raw));
    }

    private double easeOutCubic(double value) {
        double inverted = 1.0 - value;
        return 1.0 - inverted * inverted * inverted;
    }

    private double clamp(double value) {
        return Math.max(0.0, Math.min(1.0, value));
    }

    private String formatTime(int elapsedSeconds) {
        int minutes = elapsedSeconds / 60;
        int seconds = elapsedSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private static final class GameOverButton extends JButton {
        private final boolean primary;
        private final Timer hoverTimer;
        private float hoverProgress;
        private float animationAlpha;
        private boolean hoverTarget;

        private GameOverButton(String text, boolean primary) {
            super(text);
            this.primary = primary;
            this.hoverTimer = new Timer(GameConfig.TICK_MS, e -> updateHover());
        }

        private void setAnimationAlpha(float animationAlpha) {
            this.animationAlpha = Math.max(0f, Math.min(1f, animationAlpha));
            repaint();
        }

        private void setHoverTarget(boolean hoverTarget) {
            this.hoverTarget = hoverTarget;
            if (!hoverTimer.isRunning()) {
                hoverTimer.start();
            }
        }

        private void updateHover() {
            float target = hoverTarget ? 1f : 0f;
            if (Math.abs(hoverProgress - target) < 0.02f) {
                hoverProgress = target;
                hoverTimer.stop();
            } else {
                hoverProgress += (target - hoverProgress) * 0.22f;
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setComposite(AlphaComposite.SrcOver.derive(animationAlpha));

            Color base = primary ? PRIMARY_BUTTON : SECONDARY_BUTTON;
            Color hover = primary ? PRIMARY_BUTTON_HOVER : SECONDARY_BUTTON_HOVER;
            Color fill = mix(base, hover, hoverProgress);
            Color stroke = primary ? new Color(255, 240, 155, 120) : SECONDARY_STROKE;
            int arc = 18;
            int shadowAlpha = primary ? 72 : 55;

            g2.setColor(new Color(0, 0, 0, shadowAlpha));
            g2.fillRoundRect(3, 5, getWidth() - 6, getHeight() - 6, arc, arc);
            g2.setColor(fill);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 7, arc, arc);
            g2.setColor(stroke);
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 7, arc, arc);

            g2.setFont(getFont());
            g2.setColor(primary ? PRIMARY_TEXT : TEXT_WHITE);
            FontMetrics metrics = g2.getFontMetrics();
            int textX = (getWidth() - metrics.stringWidth(getText())) / 2;
            int textY = (getHeight() - 7 - metrics.getHeight()) / 2 + metrics.getAscent();
            g2.drawString(getText(), textX, textY);
            g2.dispose();
        }

        private Color mix(Color from, Color to, float amount) {
            float clamped = Math.max(0f, Math.min(1f, amount));
            int red = Math.round(from.getRed() + (to.getRed() - from.getRed()) * clamped);
            int green = Math.round(from.getGreen() + (to.getGreen() - from.getGreen()) * clamped);
            int blue = Math.round(from.getBlue() + (to.getBlue() - from.getBlue()) * clamped);
            int alpha = Math.round(from.getAlpha() + (to.getAlpha() - from.getAlpha()) * clamped);
            return new Color(red, green, blue, alpha);
        }
    }
}
