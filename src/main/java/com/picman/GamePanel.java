package com.picman;

import com.picman.config.GameConfig;
import com.picman.config.RenderTheme;
import com.picman.input.KeyboardInput;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel {
    private final Game game;
    private final KeyboardInput keyboardInput = new KeyboardInput();
    private final Timer timer;

    public GamePanel(Game game) {
        this.game = game;
        setPreferredSize(new Dimension(game.getPanelWidth(), game.getPanelHeight()));
        setFocusable(true);
        setBackground(RenderTheme.BACKGROUND);
        addKeyListener(createKeyAdapter());

        timer = new Timer(GameConfig.TICK_MS, e -> tick());
        timer.start();
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

    private KeyAdapter createKeyAdapter() {
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    game.restart();
                    keyboardInput.clear();
                    return;
                }
                keyboardInput.onKeyPressed(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keyboardInput.onKeyReleased(e.getKeyCode());
            }
        };
    }
}
