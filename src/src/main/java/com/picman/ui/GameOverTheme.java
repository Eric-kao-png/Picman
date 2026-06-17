package com.picman.ui;

import java.awt.Color;
import java.awt.Font;

public final class GameOverTheme {
    public static final Color OVERLAY_COLOR = new Color(0, 0, 0, 191);
    static final Color PANEL_FILL = new Color(10, 10, 30, 220);
    static final Color PANEL_STROKE = new Color(92, 190, 255, 150);
    static final Color PANEL_INNER_GLOW = new Color(100, 210, 255, 45);
    static final Color PANEL_SHADOW = new Color(0, 0, 0, 120);
    static final Color TITLE_RED = new Color(255, 64, 64);
    static final Color TITLE_GLOW = new Color(255, 48, 64, 90);
    static final Color TEXT_WHITE = new Color(236, 241, 248);
    static final Color MUTED_TEXT = new Color(154, 164, 188);
    static final Color SCORE_YELLOW = new Color(255, 214, 76);
    static final Color DEAD_PACMAN_FILL = new Color(218, 166, 34);
    static final Color DEAD_PACMAN_EDGE = new Color(255, 220, 86, 130);
    static final Color DEAD_PACMAN_GLOW = new Color(255, 208, 62, 42);
    static final Color PRIMARY_BUTTON = new Color(239, 196, 48);
    static final Color PRIMARY_BUTTON_HOVER = new Color(255, 222, 92);
    static final Color PRIMARY_TEXT = new Color(20, 18, 12);
    static final Color SECONDARY_BUTTON = new Color(16, 24, 52);
    static final Color SECONDARY_BUTTON_HOVER = new Color(28, 42, 82);
    static final Color SECONDARY_STROKE = new Color(80, 112, 170, 125);

    static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 25);
    static final Font LABEL_FONT = new Font("SansSerif", Font.BOLD, 12);
    static final Font SCORE_FONT = new Font("SansSerif", Font.BOLD, 52);
    static final Font STAT_FONT = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 15);

    static final int PANEL_RADIUS = 26;
    public static final int BUTTON_WIDTH = 240;
    public static final int BUTTON_HEIGHT = 54;
    public static final int BUTTON_GAP = 12;

    private GameOverTheme() {
    }
}
