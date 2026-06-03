package com.picman;

import com.picman.util.Constants;
import com.picman.util.Direction;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class GamePanel extends JPanel {
    private final Game game;
    private final Timer timer;
    private final Set<Integer> pressedKeys = new HashSet<>();
    private Direction lastDirection;

    public GamePanel(Game game) {
        this.game = game;
        setPreferredSize(new Dimension(game.getPanelWidth(), game.getPanelHeight()));
        setFocusable(true);
        setBackground(Constants.COLOR_BACKGROUND);

        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPressed(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                handleKeyReleased(e.getKeyCode());
            }
        };
        addKeyListener(keyAdapter);

        timer = new Timer(Constants.TICK_MS, e -> {
            game.setActiveDirection(resolveActiveDirection());
            game.update();
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.render((Graphics2D) g);
    }

    private void handleKeyPressed(int keyCode) {
        if (keyCode == KeyEvent.VK_R) {
            game.restart();
            pressedKeys.clear();
            lastDirection = null;
            return;
        }

        Direction direction = toDirection(keyCode);
        if (direction != null) {
            pressedKeys.add(keyCode);
            lastDirection = direction;
        }
    }

    private void handleKeyReleased(int keyCode) {
        pressedKeys.remove(keyCode);
        if (toDirection(keyCode) == lastDirection) {
            lastDirection = resolveActiveDirection();
        }
    }

    private Direction resolveActiveDirection() {
        if (isDirectionPressed(lastDirection)) {
            return lastDirection;
        }
        if (pressedKeys.contains(KeyEvent.VK_W)) {
            return Direction.UP;
        }
        if (pressedKeys.contains(KeyEvent.VK_S)) {
            return Direction.DOWN;
        }
        if (pressedKeys.contains(KeyEvent.VK_A)) {
            return Direction.LEFT;
        }
        if (pressedKeys.contains(KeyEvent.VK_D)) {
            return Direction.RIGHT;
        }
        return null;
    }

    private boolean isDirectionPressed(Direction direction) {
        if (direction == null) {
            return false;
        }
        return switch (direction) {
            case UP -> pressedKeys.contains(KeyEvent.VK_W);
            case DOWN -> pressedKeys.contains(KeyEvent.VK_S);
            case LEFT -> pressedKeys.contains(KeyEvent.VK_A);
            case RIGHT -> pressedKeys.contains(KeyEvent.VK_D);
        };
    }

    private static Direction toDirection(int keyCode) {
        return switch (keyCode) {
            case KeyEvent.VK_W -> Direction.UP;
            case KeyEvent.VK_S -> Direction.DOWN;
            case KeyEvent.VK_A -> Direction.LEFT;
            case KeyEvent.VK_D -> Direction.RIGHT;
            default -> null;
        };
    }
}
