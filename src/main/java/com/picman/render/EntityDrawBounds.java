package com.picman.render;

import com.picman.config.GameConfig;
import com.picman.model.entity.GridPosition;

/**
 * 實體在螢幕上的繪製外框（左上與內縮後尺寸）。
 */
public record EntityDrawBounds(int x, int y, int margin, int size) {
    public static EntityDrawBounds from(GridPosition position) {
        double topLeftX = position.getCenterX() - GameConfig.TILE_SIZE / 2.0;
        double topLeftY = position.getCenterY() - GameConfig.TILE_SIZE / 2.0;
        int margin = GameConfig.ENTITY_DRAW_MARGIN;
        int size = GameConfig.TILE_SIZE - margin * 2;
        return new EntityDrawBounds(
                (int) Math.round(topLeftX),
                ViewLayout.toScreenY(topLeftY),
                margin,
                size);
    }

    public int innerX() {
        return x + margin;
    }

    public int innerY() {
        return y + margin;
    }
}
