package com.picman.render;

import com.picman.config.GameConfig;
import com.picman.model.Maze;

public final class ViewLayout {
    private ViewLayout() {
    }

    public static int panelWidth(Maze maze) {
        return maze.getWidth() * GameConfig.TILE_SIZE;
    }

    public static int panelHeight(Maze maze) {
        return GameConfig.HUD_HEIGHT + maze.getHeight() * GameConfig.TILE_SIZE;
    }

    public static int mazeOffsetY() {
        return GameConfig.HUD_HEIGHT;
    }

    public static int toScreenY(double entityY) {
        return mazeOffsetY() + (int) Math.round(entityY);
    }
}
