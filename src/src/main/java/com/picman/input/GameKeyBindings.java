package com.picman.input;

import com.picman.Game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * 將鍵盤事件轉交 {@link KeyboardInput} 與遊戲指令（如重開）。
 */
public class GameKeyBindings extends KeyAdapter {
    private final Game game;
    private final KeyboardInput keyboardInput;

    public GameKeyBindings(Game game, KeyboardInput keyboardInput) {
        this.game = game;
        this.keyboardInput = keyboardInput;
    }

    @Override
    public void keyPressed(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_R) {
            game.restart();
            keyboardInput.clear();
            return;
        }
        keyboardInput.onKeyPressed(event.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent event) {
        keyboardInput.onKeyReleased(event.getKeyCode());
    }
}
