package com.picman.render;

import com.picman.config.RenderTheme;
import com.picman.model.GameSession;

import java.awt.Font;
import java.awt.Graphics2D;

public class HudRenderer {
    public void render(Graphics2D g, GameSession session) {
        g.setColor(RenderTheme.HUD_TEXT);
        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        g.drawString("Score: " + session.getScore(), 8, 20);
        g.drawString("Lives: " + session.getLives(), 120, 20);
        g.drawString("WASD move  |  R restart", 220, 20);
    }
}
