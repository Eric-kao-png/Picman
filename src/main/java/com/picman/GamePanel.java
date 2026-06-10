package com.picman;

import com.picman.config.GameConfig;
import com.picman.config.RenderTheme;
import com.picman.input.GameKeyBindings;
import com.picman.input.KeyboardInput;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class GamePanel extends JPanel {
    private final Game game;
    private final KeyboardInput keyboardInput = new KeyboardInput();
    private final Timer timer;

    public GamePanel(Game game) {
        this.game = game;
        setPreferredSize(new Dimension(game.getPanelWidth(), game.getPanelHeight()));
        setFocusable(true);
        setBackground(RenderTheme.BACKGROUND);
        addKeyListener(new GameKeyBindings(game, keyboardInput));

        timer = new Timer(GameConfig.TICK_MS, e -> tick());
    }

    public void startGame() {
        game.restart();
        keyboardInput.clear();
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
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.render((Graphics2D) g);
    }

    private void tick() {
        game.setActiveDirection(keyboardInput.getActiveDirection());
        game.update();
        repaint();
    }
}
