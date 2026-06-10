package com.picman.model;

import com.picman.config.GameConfig;
import com.picman.config.ItemSpawnConfig;
import com.picman.config.PickaxeConfig;
import com.picman.config.PowerCoinConfig;

public class GameSession {
    private GameStatus status = GameStatus.PLAYING;
    private int score;
    private int lives = GameConfig.INITIAL_LIVES;
    private int invincibleTicks;
    private int poweredTicks;
    private int pickaxeTicks;
    private int pelletsCollected;
    private int ghostsEaten;
    private int elapsedTicks;

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

    public int getPelletsCollected() {
        return pelletsCollected;
    }

    public int getGhostsEaten() {
        return ghostsEaten;
    }

    public int getElapsedSeconds() {
        return elapsedTicks * GameConfig.TICK_MS / 1000;
    }

    public boolean isInvincible() {
        return invincibleTicks > 0;
    }

    public boolean isPowered() {
        return poweredTicks > 0;
    }

    public boolean isPickaxeActive() {
        return pickaxeTicks > 0;
    }

    public int getInvincibleTicks() {
        return invincibleTicks;
    }

    public void tickInvincibility() {
        if (invincibleTicks > 0) {
            invincibleTicks--;
        }
    }

    public void tickPowered() {
        if (poweredTicks > 0) {
            poweredTicks--;
        }
    }

    public void tickPickaxe() {
        if (pickaxeTicks > 0) {
            pickaxeTicks--;
        }
    }

    public void tickElapsed() {
        elapsedTicks++;
    }

    public void addScore(int points) {
        score += points;
    }

    public void onCoinCollected() {
        pelletsCollected++;
        addScore(GameConfig.COIN_SCORE);
    }

    public void onPowerCoinCollected() {
        pelletsCollected++;
        addScore(PowerCoinConfig.SCORE);
        poweredTicks = PowerCoinConfig.POWERED_TICKS;
    }

    public void onGhostEaten() {
        ghostsEaten++;
        addScore(PowerCoinConfig.SCORE);
    }

    public void onExtraLifeCollected() {
        if (lives < ItemSpawnConfig.MAX_LIVES) {
            lives++;
        }
    }

    public void onPickaxeCollected() {
        addScore(PickaxeConfig.SCORE);
        pickaxeTicks = PickaxeConfig.PICKAXE_TICKS;
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
        poweredTicks = 0;
        pickaxeTicks = 0;
        pelletsCollected = 0;
        ghostsEaten = 0;
        elapsedTicks = 0;
    }
}
