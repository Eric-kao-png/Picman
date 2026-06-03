package com.picman;

import com.picman.model.GameStatus;
import com.picman.model.Ghost;
import com.picman.model.Maze;
import com.picman.model.Pacman;
import com.picman.util.Constants;
import com.picman.util.Direction;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Game {
    private final Maze maze = new Maze();
    private final Pacman pacman = new Pacman();
    private final Ghost ghost = new Ghost();

    private GameStatus status = GameStatus.PLAYING;
    private int score;
    private int lives = Constants.INITIAL_LIVES;
    private int invincibleTicks;

    public void update() {
        if (status != GameStatus.PLAYING) {
            return;
        }

        if (invincibleTicks > 0) {
            invincibleTicks--;
        }

        pacman.update(maze);
        if (maze.tryEatCoin(pacman.getCol(), pacman.getRow())) {
            score += Constants.COIN_SCORE;
        }

        ghost.update(maze, pacman);

        if (ghost.collidesWith(pacman) && invincibleTicks == 0) {
            lives--;
            if (lives <= 0) {
                status = GameStatus.GAME_OVER;
            } else {
                resetPositions();
                invincibleTicks = Constants.INVINCIBLE_TICKS;
            }
        }

        if (maze.noCoinsLeft()) {
            status = GameStatus.WIN;
        }
    }

    public void render(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(Constants.COLOR_BACKGROUND);
        g.fillRect(0, 0, getPanelWidth(), getPanelHeight());

        drawHud(g);
        drawMaze(g);
        drawGhost(g);
        drawPacman(g);
        drawOverlay(g);
    }

    public void setActiveDirection(Direction direction) {
        if (status == GameStatus.PLAYING) {
            pacman.setActiveDirection(direction);
        }
    }

    public void restart() {
        maze.reset();
        pacman.reset();
        ghost.reset();
        score = 0;
        lives = Constants.INITIAL_LIVES;
        invincibleTicks = 0;
        status = GameStatus.PLAYING;
    }

    public GameStatus getStatus() {
        return status;
    }

    public int getPanelWidth() {
        return maze.getWidth() * Constants.TILE_SIZE;
    }

    public int getPanelHeight() {
        return Constants.HUD_HEIGHT + maze.getHeight() * Constants.TILE_SIZE;
    }

    private void resetPositions() {
        pacman.reset();
        ghost.reset();
    }

    private void drawHud(Graphics2D g) {
        g.setColor(Constants.COLOR_HUD_TEXT);
        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        g.drawString("Score: " + score, 8, 20);
        g.drawString("Lives: " + lives, 120, 20);
        g.drawString("WASD move  |  R restart", 220, 20);
    }

    private void drawMaze(Graphics2D g) {
        int offsetY = Constants.HUD_HEIGHT;
        for (int row = 0; row < maze.getHeight(); row++) {
            for (int col = 0; col < maze.getWidth(); col++) {
                int x = col * Constants.TILE_SIZE;
                int y = offsetY + row * Constants.TILE_SIZE;
                int cell = maze.getCell(col, row);

                if (cell == Maze.WALL) {
                    g.setColor(Constants.COLOR_WALL);
                    g.fillRect(x, y, Constants.TILE_SIZE, Constants.TILE_SIZE);
                } else if (cell == Maze.COIN) {
                    g.setColor(Constants.COLOR_COIN);
                    int margin = Constants.TILE_SIZE / 4;
                    g.fillOval(x + margin, y + margin,
                            Constants.TILE_SIZE - margin * 2,
                            Constants.TILE_SIZE - margin * 2);
                }
            }
        }
    }

    private void drawPacman(Graphics2D g) {
        int offsetY = Constants.HUD_HEIGHT;
        int x = (int) Math.round(pacman.getDrawX());
        int y = offsetY + (int) Math.round(pacman.getDrawY());
        int margin = 2;

        if (invincibleTicks > 0 && (invincibleTicks / 5) % 2 == 0) {
            return;
        }

        g.setColor(Constants.COLOR_PACMAN);
        g.fillOval(x + margin, y + margin,
                Constants.TILE_SIZE - margin * 2,
                Constants.TILE_SIZE - margin * 2);
    }

    private void drawGhost(Graphics2D g) {
        int offsetY = Constants.HUD_HEIGHT;
        int x = (int) Math.round(ghost.getDrawX());
        int y = offsetY + (int) Math.round(ghost.getDrawY());
        int margin = 2;

        g.setColor(Constants.COLOR_GHOST);
        g.fillRoundRect(x + margin, y + margin,
                Constants.TILE_SIZE - margin * 2,
                Constants.TILE_SIZE - margin * 2,
                6, 6);
    }

    private void drawOverlay(Graphics2D g) {
        if (status == GameStatus.PLAYING) {
            return;
        }

        g.setColor(new Color(0, 0, 0, 160));
        g.fillRect(0, Constants.HUD_HEIGHT, getPanelWidth(), getPanelHeight() - Constants.HUD_HEIGHT);

        g.setColor(Color.WHITE);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
        String message = status == GameStatus.WIN ? "YOU WIN!" : "GAME OVER";
        int textWidth = g.getFontMetrics().stringWidth(message);
        int x = (getPanelWidth() - textWidth) / 2;
        int y = Constants.HUD_HEIGHT + (maze.getHeight() * Constants.TILE_SIZE) / 2;
        g.drawString(message, x, y);

        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        String hint = "Press R to restart";
        int hintWidth = g.getFontMetrics().stringWidth(hint);
        g.drawString(hint, (getPanelWidth() - hintWidth) / 2, y + 28);
    }
}
