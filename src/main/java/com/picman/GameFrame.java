package com.picman;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;

public class GameFrame extends JFrame {
    private static final String MENU_CARD = "menu";
    private static final String GAME_CARD = "game";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);
    private final GamePanel gamePanel;

    public GameFrame() {
        super("PAC-MAN");

        Game game = new Game();
        gamePanel = new GamePanel(game);
        MenuPanel menuPanel = new MenuPanel(
                game.getPanelWidth(),
                game.getPanelHeight(),
                this::startGame,
                this::exitGame
        );

        cards.add(menuPanel, MENU_CARD);
        cards.add(gamePanel, GAME_CARD);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(cards);
        pack();
        setLocationRelativeTo(null);
        cardLayout.show(cards, MENU_CARD);
    }

    private void startGame() {
        gamePanel.startGame();
        cardLayout.show(cards, GAME_CARD);
        gamePanel.requestFocusInWindow();
    }

    private void exitGame() {
        dispose();
        System.exit(0);
    }
}
