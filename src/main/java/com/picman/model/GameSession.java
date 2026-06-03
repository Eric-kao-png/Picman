package com.picman.model;

import com.picman.config.GameConfig;

public class GameSession {
    private GameStatus status = GameStatus.PLAYING;
    private int score;
    private int lives = GameConfig.INITIAL_LIVES;
    private int invincibleTicks;

    public boolean isPlaying() {
        return status == GameStatus.PLAYING;
    }

    public GameStatus getStatus() {
        return status;
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public boolean isInvincible() {
        return invincibleTicks > 0;
    }

    public boolean shouldBlink() {
        return invincibleTicks > 0 && (invincibleTicks / 5) % 2 == 0;
    }

    public void tickInvincibility() {
        if (invincibleTicks > 0) {
            invincibleTicks--;
        }
    }

    public void addScore(int points) {
        score += points;
    }

    public void onCoinCollected() {
        addScore(GameConfig.COIN_SCORE);
    }

    public void onGhostHit() {
        lives--;
        if (lives <= 0) {
            status = GameStatus.GAME_OVER;
        } else {
            invincibleTicks = GameConfig.INVINCIBLE_TICKS;
        }
    }

    public void onAllCoinsCollected() {
        status = GameStatus.WIN;
    }

    public void reset() {
        status = GameStatus.PLAYING;
        score = 0;
        lives = GameConfig.INITIAL_LIVES;
        invincibleTicks = 0;
    }
}
