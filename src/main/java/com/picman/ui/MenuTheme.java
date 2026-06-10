package com.picman.ui;

import com.picman.config.RenderTheme;

import java.awt.Color;
import java.awt.Font;

public final class MenuTheme {
    public static final Color BACKGROUND = new Color(3, 5, 18);
    public static final Color NEON_BLUE = new Color(64, 158, 255);
    public static final Color DEEP_BLUE = new Color(7, 17, 55);
    public static final Color FRAME_OUTER = new Color(20, 64, 180);
    public static final Color MAZE_LINE = new Color(15, 36, 110);
    public static final Color BUTTON_BLUE = new Color(10, 30, 92);
    public static final Color BUTTON_HOVER = new Color(28, 95, 210);
    public static final Color MENU_YELLOW = RenderTheme.PACMAN;
    public static final Color TEXT_WHITE = new Color(245, 248, 255);
    public static final Color MUTED_TEXT = new Color(178, 204, 255);
    public static final Color PELLET = new Color(255, 236, 170);

    public static final Font TITLE_FONT = new Font("Arial Black", Font.BOLD, 48);
    public static final Font HELP_TITLE_FONT = new Font("SansSerif", Font.BOLD, 34);
    public static final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 18);
    public static final Font HELP_FONT = new Font("SansSerif", Font.BOLD, 18);
    public static final Font HINT_FONT = new Font("SansSerif", Font.PLAIN, 13);

    public static final int BUTTON_WIDTH = 230;
    public static final int BUTTON_HEIGHT = 46;
    public static final int BUTTON_GAP = 14;

    private MenuTheme() {
    }
}
