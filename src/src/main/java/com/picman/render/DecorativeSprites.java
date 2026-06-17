package com.picman.render;

import java.awt.Color;
import java.awt.Graphics2D;

/** 主選單等畫面的裝飾性 Pac-Man / 幽靈精靈。 */
public final class DecorativeSprites {
    private DecorativeSprites() {
    }

    public static void drawMenuPacman(Graphics2D g2, int x, int y, int size, Color fill, Color eyeCutout) {
        g2.setColor(fill);
        g2.fillArc(x, y, size, size, 35, 290);

        g2.setColor(eyeCutout);
        int eyeSize = Math.max(5, size / 9);
        g2.fillOval(x + size * 58 / 100, y + size * 22 / 100, eyeSize, eyeSize);
    }

    public static void drawSmallGhost(Graphics2D g2, int x, int y, Color color) {
        int width = 30;
        int height = 34;

        g2.setColor(color);
        g2.fillRoundRect(x, y, width, height, 16, 16);
        g2.fillRect(x, y + height / 2, width, height / 2);

        g2.setColor(Color.WHITE);
        g2.fillOval(x + 6, y + 10, 7, 8);
        g2.fillOval(x + 18, y + 10, 7, 8);

        g2.setColor(Color.BLACK);
        g2.fillOval(x + 9, y + 13, 3, 3);
        g2.fillOval(x + 21, y + 13, 3, 3);
    }

    public static void drawPelletLine(Graphics2D g2, int startX, int endX, int y, Color color) {
        g2.setColor(color);
        for (int x = startX; x <= endX; x += 24) {
            g2.fillOval(x, y, 5, 5);
        }
    }
}
