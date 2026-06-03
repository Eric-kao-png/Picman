package com.picman.config;

public final class GameConfig {
    private GameConfig() {
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
    public static final int GHOST_START_COL = 13;
    public static final int GHOST_START_ROW = 11;

    public static final double ENTITY_HIT_RADIUS_RATIO = 0.75;
    public static final int ENTITY_DRAW_MARGIN = 2;
    public static final int COIN_DRAW_MARGIN_DIVISOR = 4;
}
