package com.picman.util;

import java.awt.Color;

public final class Constants {
    private Constants() {
    }

    public static final int TILE_SIZE = 16;
    public static final int HUD_HEIGHT = 32;
    public static final int TICK_MS = 16;

    public static final double PACMAN_SPEED = 2.5;
    public static final double GHOST_SPEED = 1.25;
    public static final int ALIGN_THRESHOLD = 4;

    public static final int INITIAL_LIVES = 3;
    public static final int COIN_SCORE = 10;
    public static final int INVINCIBLE_TICKS = 90;

    public static final int PACMAN_START_COL = 1;
    public static final int PACMAN_START_ROW = 1;
    public static final int GHOST_START_COL = 10;
    public static final int GHOST_START_ROW = 7;

    public static final Color COLOR_BACKGROUND = new Color(0, 0, 0);
    public static final Color COLOR_WALL = new Color(33, 33, 222);
    public static final Color COLOR_COIN = new Color(255, 255, 100);
    public static final Color COLOR_PACMAN = Color.YELLOW;
    public static final Color COLOR_GHOST = Color.RED;
    public static final Color COLOR_HUD_TEXT = Color.WHITE;
}
