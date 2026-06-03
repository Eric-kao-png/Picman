package com.picman.render;

import com.picman.config.RenderTheme;
import com.picman.model.GameSession;

import java.awt.Font;
import java.awt.Graphics2D;

public class HudRenderer {
    private static final int SCORE_X = 8;
    private static final int LIVES_X = 120;
    private static final int HINT_X = 220;
    private static final int TEXT_Y = 20;
    private static final int FONT_SIZE = 14;

    public void render(Graphics2D g, GameSession session) {
        g.setColor(RenderTheme.HUD_TEXT);
        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, FONT_SIZE));
        g.drawString("Score: " + session.getScore(), SCORE_X, TEXT_Y);
        g.drawString("Lives: " + session.getLives(), LIVES_X, TEXT_Y);
        g.drawString("WASD move  |  R restart", HINT_X, TEXT_Y);
    }
}
