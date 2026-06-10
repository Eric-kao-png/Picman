package com.picman.render;

import com.picman.config.GameConfig;
import com.picman.config.RenderTheme;
import com.picman.model.CellType;
import com.picman.model.ItemType;
import com.picman.model.Maze;

import java.awt.Graphics2D;

public class MazeRenderer {
    public void render(Graphics2D g, Maze maze) {
        int offsetY = ViewLayout.mazeOffsetY();
        int coinMargin = GameConfig.TILE_SIZE / GameConfig.COIN_DRAW_MARGIN_DIVISOR;
        int coinSize = GameConfig.TILE_SIZE - coinMargin * 2;

        for (int row = 0; row < maze.getHeight(); row++) {
            for (int col = 0; col < maze.getWidth(); col++) {
                int x = col * GameConfig.TILE_SIZE;
                int y = offsetY + row * GameConfig.TILE_SIZE;
                CellType cell = maze.getCellType(col, row);

                if (cell == CellType.WALL) {
                    g.setColor(RenderTheme.WALL);
                    g.fillRect(x, y, GameConfig.TILE_SIZE, GameConfig.TILE_SIZE);
                } else if (cell == CellType.COIN) {
                    g.setColor(RenderTheme.COIN);
                    g.fillOval(x + coinMargin, y + coinMargin, coinSize, coinSize);
                } else if (cell == CellType.POWER_COIN) {
                    int powerMargin = coinMargin / 2;
                    int powerSize = GameConfig.TILE_SIZE - powerMargin * 2;
                    g.setColor(RenderTheme.POWER_COIN);
                    g.fillOval(x + powerMargin, y + powerMargin, powerSize, powerSize);
                } else if (cell == CellType.EXTRA_LIFE_ITEM) {
                    renderSpawnedItem(g, x, y, ItemType.EXTRA_LIFE);
                }
            }
        }
    }

    private void renderSpawnedItem(Graphics2D g, int x, int y, ItemType itemType) {
        int margin = GameConfig.TILE_SIZE / 5;
        int size = GameConfig.TILE_SIZE - margin * 2;
        g.setColor(itemType.getColor());
        g.fillOval(x + margin, y + margin, size, size);
        g.setColor(RenderTheme.HUD_TEXT);
        int centerX = x + GameConfig.TILE_SIZE / 2;
        int centerY = y + GameConfig.TILE_SIZE / 2 + 4;
        g.drawString("+", centerX - 3, centerY);
    }
}
