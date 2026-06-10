package com.picman;

import com.picman.config.GameConfig;
import com.picman.config.RenderTheme;
import com.picman.input.GameKeyBindings;
import com.picman.input.KeyboardInput;
import com.picman.model.GameStatus;
import com.picman.ui.GameOverAnimation;
import com.picman.ui.GameOverButton;
import com.picman.ui.GameOverOverlayRenderer;
import com.picman.ui.GameOverTheme;
import com.picman.ui.GameOverViewModel;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GamePanel extends JPanel {
    private final Game game;
    private final Runnable onReturnToMenu;
    private final KeyboardInput keyboardInput = new KeyboardInput();
    private final Timer timer;
    private final GameOverButton restartButton;
    private final GameOverButton menuButton;
    private final GameOverAnimation gameOverAnimation = new GameOverAnimation();
    private final GameOverOverlayRenderer gameOverRenderer = new GameOverOverlayRenderer();

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
        resetSessionState();
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
        layoutGameOverButtons(gameOverAnimation.progress());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        game.render(g2);
        if (isGameOverVisible()) {
            gameOverAnimation.markShownIfNeeded();
            gameOverRenderer.render(g2, createViewModel(), gameOverAnimation, getWidth(), getHeight());
        } else {
            gameOverAnimation.reset();
        }
        g2.dispose();
    }

    private void tick() {
        game.setActiveDirection(keyboardInput.getActiveDirection());
        game.update();
        updateGameOverButtons();
        repaint();
    }

    private GameOverViewModel createViewModel() {
        return new GameOverViewModel(
                game.getStatus(),
                game.getScore(),
                game.getPelletsCollected(),
                game.getGhostsEaten(),
                game.getElapsedSeconds());
    }

    private boolean isGameOverVisible() {
        return game.getStatus() != GameStatus.PLAYING;
    }

    private void updateGameOverButtons() {
        if (isGameOverVisible()) {
            gameOverAnimation.markShownIfNeeded();
            showGameOverButtons();
        } else {
            resetGameOverAnimation();
            hideGameOverButtons();
        }
    }

    private void resetGameOverAnimation() {
        gameOverAnimation.reset();
        restartButton.setAnimationAlpha(0f);
        menuButton.setAnimationAlpha(0f);
    }

    private void showGameOverButtons() {
        if (!restartButton.isVisible()) {
            restartButton.setVisible(true);
            menuButton.setVisible(true);
        }
        layoutGameOverButtons(gameOverAnimation.progress());
    }

    private void hideGameOverButtons() {
        restartButton.setVisible(false);
        menuButton.setVisible(false);
    }

    private void restartGame() {
        resetSessionState();
        repaint();
        requestFocusInWindow();
    }

    private void returnToMainMenu() {
        resetSessionState();
        repaint();
        onReturnToMenu.run();
    }

    private void resetSessionState() {
        game.restart();
        keyboardInput.clear();
        resetGameOverAnimation();
        hideGameOverButtons();
    }

    private GameOverButton createGameOverButton(String text, boolean primary) {
        GameOverButton button = new GameOverButton(text, primary);
        button.setFont(GameOverTheme.BUTTON_FONT);
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
        int panelWidth = gameOverAnimation.panelWidth(getWidth());
        int panelHeight = gameOverAnimation.panelHeight(getHeight());
        int panelX = (getWidth() - panelWidth) / 2;
        int panelY = gameOverAnimation.animatedPanelY(panelHeight, getHeight(), panelProgress);
        int buttonX = panelX + (panelWidth - GameOverTheme.BUTTON_WIDTH) / 2;
        int firstButtonY = panelY + panelHeight - 130;

        restartButton.setBounds(buttonX, firstButtonY, GameOverTheme.BUTTON_WIDTH, GameOverTheme.BUTTON_HEIGHT);
        menuButton.setBounds(
                buttonX,
                firstButtonY + GameOverTheme.BUTTON_HEIGHT + GameOverTheme.BUTTON_GAP,
                GameOverTheme.BUTTON_WIDTH,
                GameOverTheme.BUTTON_HEIGHT);
        restartButton.setAnimationAlpha(gameOverAnimation.buttonProgress(GameOverAnimation.BUTTON_STAGGER_MS));
        menuButton.setAnimationAlpha(gameOverAnimation.buttonProgress(GameOverAnimation.BUTTON_STAGGER_MS * 2));
    }
}
