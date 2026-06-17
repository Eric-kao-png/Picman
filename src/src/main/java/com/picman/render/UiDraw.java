package com.picman.render;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

/** 選單與結束畫面共用的文字繪製工具。 */
public final class UiDraw {
    private UiDraw() {
    }

    public static void drawCenteredText(
            Graphics2D g2,
            String text,
            Font font,
            Color color,
            int centerX,
            int baselineY) {
        g2.setFont(font);
        g2.setColor(color);
        FontMetrics metrics = g2.getFontMetrics();
        g2.drawString(text, centerX - metrics.stringWidth(text) / 2, baselineY);
    }

    public static void drawNeonCenteredText(
            Graphics2D g2,
            String text,
            Font font,
            Color color,
            Color glow,
            int centerX,
            int baselineY) {
        g2.setFont(font);
        FontMetrics metrics = g2.getFontMetrics();
        int x = centerX - metrics.stringWidth(text) / 2;

        g2.setColor(glow);
        g2.drawString(text, x - 2, baselineY);
        g2.drawString(text, x + 2, baselineY);
        g2.drawString(text, x, baselineY - 2);
        g2.drawString(text, x, baselineY + 2);

        g2.setColor(color);
        g2.drawString(text, x, baselineY);
    }
}
