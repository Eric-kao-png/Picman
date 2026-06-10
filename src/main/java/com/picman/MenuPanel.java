package com.picman;

import com.picman.config.RenderTheme;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuPanel extends JPanel {
    private enum MenuState {
        MAIN,
        HELP
    }

    private static final Color MENU_BACKGROUND = new Color(3, 5, 18);
    private static final Color MAZE_BLUE = RenderTheme.WALL;
    private static final Color NEON_BLUE = new Color(64, 158, 255);
    private static final Color DEEP_BLUE = new Color(7, 17, 55);
    private static final Color BUTTON_BLUE = new Color(10, 30, 92);
    private static final Color BUTTON_HOVER = new Color(28, 95, 210);
    private static final Color MENU_YELLOW = RenderTheme.PACMAN;
    private static final Color TEXT_WHITE = new Color(245, 248, 255);
    private static final Color MUTED_TEXT = new Color(178, 204, 255);

    private static final Font TITLE_FONT = new Font("Arial Black", Font.BOLD, 48);
    private static final Font HELP_TITLE_FONT = new Font("SansSerif", Font.BOLD, 34);
    private static final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 18);
    private static final Font HELP_FONT = new Font("SansSerif", Font.BOLD, 18);
    private static final Font HINT_FONT = new Font("SansSerif", Font.PLAIN, 13);

    private static final int BUTTON_WIDTH = 230;
    private static final int BUTTON_HEIGHT = 46;
    private static final int BUTTON_GAP = 14;

    private final JButton startButton;
    private final JButton helpButton;
    private final JButton exitButton;
    private final JButton helpStartButton;
    private final JButton backButton;

    private MenuState menuState = MenuState.MAIN;

    public MenuPanel(int width, int height, Runnable onStartGame, Runnable onExitGame) {
        setPreferredSize(new Dimension(width, height));
        setBackground(MENU_BACKGROUND);
        setLayout(null);
        setFocusable(true);

        startButton = createMenuButton("開始遊戲", true);
        helpButton = createMenuButton("遊戲說明", false);
        exitButton = createMenuButton("離開遊戲", false);
        helpStartButton = createMenuButton("開始遊戲", true);
        backButton = createMenuButton("返回主選單", false);

        startButton.addActionListener(e -> onStartGame.run());
        helpStartButton.addActionListener(e -> onStartGame.run());
        helpButton.addActionListener(e -> setMenuState(MenuState.HELP));
        backButton.addActionListener(e -> setMenuState(MenuState.MAIN));
        exitButton.addActionListener(e -> onExitGame.run());

        add(startButton);
        add(helpButton);
        add(exitButton);
        add(helpStartButton);
        add(backButton);

        showMainButtons();
    }

    @Override
    public void doLayout() {
        super.doLayout();
        layoutMainButtons();
        layoutHelpButtons();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawMazeBackground(g2);
        drawNeonFrame(g2);

        if (menuState == MenuState.MAIN) {
            drawMainMenu(g2);
        } else {
            drawHelpMenu(g2);
        }

        g2.dispose();
    }

    private void setMenuState(MenuState state) {
        menuState = state;
        if (menuState == MenuState.MAIN) {
            showMainButtons();
        } else {
            showHelpButtons();
        }
        revalidate();
        repaint();
    }

    private void showMainButtons() {
        startButton.setVisible(true);
        helpButton.setVisible(true);
        exitButton.setVisible(true);
        helpStartButton.setVisible(false);
        backButton.setVisible(false);
    }

    private void showHelpButtons() {
        startButton.setVisible(false);
        helpButton.setVisible(false);
        exitButton.setVisible(false);
        helpStartButton.setVisible(true);
        backButton.setVisible(true);
    }

    private void layoutMainButtons() {
        int x = (getWidth() - BUTTON_WIDTH) / 2;
        int totalHeight = BUTTON_HEIGHT * 3 + BUTTON_GAP * 2;
        int y = Math.max(276, getHeight() - totalHeight - 58);

        startButton.setBounds(x, y, BUTTON_WIDTH, BUTTON_HEIGHT);
        helpButton.setBounds(x, y + BUTTON_HEIGHT + BUTTON_GAP, BUTTON_WIDTH, BUTTON_HEIGHT);
        exitButton.setBounds(x, y + (BUTTON_HEIGHT + BUTTON_GAP) * 2, BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    private void layoutHelpButtons() {
        int x = (getWidth() - BUTTON_WIDTH) / 2;
        int totalHeight = BUTTON_HEIGHT * 2 + BUTTON_GAP;
        int y = Math.max(330, getHeight() - totalHeight - 62);

        helpStartButton.setBounds(x, y, BUTTON_WIDTH, BUTTON_HEIGHT);
        backButton.setBounds(x, y + BUTTON_HEIGHT + BUTTON_GAP, BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    private JButton createMenuButton(String text, boolean primary) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(primary ? MENU_YELLOW : TEXT_WHITE);
        button.setBackground(BUTTON_BLUE);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(primary ? MENU_YELLOW : NEON_BLUE, 2),
                BorderFactory.createEmptyBorder(8, 24, 8, 24)
        ));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(BUTTON_HOVER);
                button.setForeground(Color.WHITE);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(MENU_YELLOW, 2),
                        BorderFactory.createEmptyBorder(8, 24, 8, 24)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(BUTTON_BLUE);
                button.setForeground(primary ? MENU_YELLOW : TEXT_WHITE);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(primary ? MENU_YELLOW : NEON_BLUE, 2),
                        BorderFactory.createEmptyBorder(8, 24, 8, 24)
                ));
            }
        });
        return button;
    }

    private void drawMainMenu(Graphics2D g2) {
        int centerX = getWidth() / 2;
        int pacmanSize = Math.max(42, Math.min(58, getWidth() / 8));
        int pacmanX = centerX - pacmanSize / 2;
        int pacmanY = Math.max(52, getHeight() / 10);

        drawPacman(g2, pacmanX, pacmanY, pacmanSize);
        drawCenteredText(g2, "PAC-MAN", TITLE_FONT, MENU_YELLOW, centerX, pacmanY + pacmanSize + 54);
        drawCenteredText(g2, "READY!", HINT_FONT, MUTED_TEXT, centerX, pacmanY + pacmanSize + 82);

        int dotY = Math.min(getHeight() - 252, pacmanY + pacmanSize + 114);
        drawPelletLine(g2, 58, getWidth() - 58, dotY);
        drawGhost(g2, getWidth() - 96, dotY - 21, new Color(255, 76, 76));
        drawGhost(g2, 66, dotY - 21, new Color(0, 220, 255));
    }

    private void drawHelpMenu(Graphics2D g2) {
        int centerX = getWidth() / 2;
        int titleY = Math.max(92, getHeight() / 5);

        drawCenteredText(g2, "遊戲說明", HELP_TITLE_FONT, MENU_YELLOW, centerX, titleY);
        drawPelletLine(g2, 96, getWidth() - 96, titleY + 34);

        String[] rules = {
                "1. 使用方向鍵移動",
                "2. 吃掉所有豆子獲勝",
                "3. 避開鬼魂"
        };

        g2.setFont(HELP_FONT);
        FontMetrics metrics = g2.getFontMetrics();
        int lineHeight = metrics.getHeight() + 13;
        int firstLineY = titleY + 92;

        for (int i = 0; i < rules.length; i++) {
            int y = firstLineY + i * lineHeight;
            drawCenteredText(g2, rules[i], HELP_FONT, TEXT_WHITE, centerX, y);
        }

        int iconSize = 34;
        drawPacman(g2, centerX - 72, firstLineY + lineHeight * rules.length + 18, iconSize);
        drawGhost(g2, centerX + 38, firstLineY + lineHeight * rules.length + 16, new Color(255, 105, 180));
    }

    private void drawMazeBackground(Graphics2D g2) {
        g2.setColor(DEEP_BLUE);
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setColor(new Color(15, 36, 110));
        g2.setStroke(new BasicStroke(2f));

        int margin = 38;
        int step = 52;
        for (int y = margin + 14; y < getHeight() - margin; y += step) {
            for (int x = margin; x < getWidth() - margin; x += step) {
                g2.drawLine(x, y, Math.min(x + 24, getWidth() - margin), y);
                if ((x / step + y / step) % 2 == 0) {
                    g2.drawLine(x, y, x, Math.min(y + 24, getHeight() - margin));
                }
            }
        }
    }

    private void drawNeonFrame(Graphics2D g2) {
        int margin = 18;
        int width = getWidth() - margin * 2;
        int height = getHeight() - margin * 2;

        g2.setStroke(new BasicStroke(5f));
        g2.setColor(new Color(20, 64, 180));
        g2.drawRoundRect(margin, margin, width, height, 18, 18);

        g2.setStroke(new BasicStroke(2f));
        g2.setColor(NEON_BLUE);
        g2.drawRoundRect(margin + 8, margin + 8, width - 16, height - 16, 14, 14);
    }

    private void drawPelletLine(Graphics2D g2, int startX, int endX, int y) {
        g2.setColor(new Color(255, 236, 170));
        for (int x = startX; x <= endX; x += 24) {
            g2.fillOval(x, y, 5, 5);
        }
    }

    private void drawPacman(Graphics2D g2, int x, int y, int size) {
        g2.setColor(MENU_YELLOW);
        g2.fillArc(x, y, size, size, 35, 290);

        g2.setColor(MENU_BACKGROUND);
        int eyeSize = Math.max(5, size / 9);
        g2.fillOval(x + size * 58 / 100, y + size * 22 / 100, eyeSize, eyeSize);
    }

    private void drawGhost(Graphics2D g2, int x, int y, Color color) {
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

    private void drawCenteredText(Graphics2D g2, String text, Font font, Color color, int centerX, int baselineY) {
        g2.setFont(font);
        g2.setColor(color);
        FontMetrics metrics = g2.getFontMetrics();
        int x = centerX - metrics.stringWidth(text) / 2;
        g2.drawString(text, x, baselineY);
    }
}
