package com.picman.model;

import com.picman.config.GameConfig;
import com.picman.config.ItemSpawnConfig;
import com.picman.config.PickaxeConfig;
import com.picman.config.PowerCoinConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameSessionTest {
    private GameSession session;

    @BeforeEach
    void setUp() {
        session = new GameSession();
    }

    @Test
    void initialState_isPlayingWithDefaultLives() {
        assertTrue(session.isPlaying());
        assertEquals(GameStatus.PLAYING, session.getStatus());
        assertEquals(0, session.getScore());
        assertEquals(GameConfig.INITIAL_LIVES, session.getLives());
        assertEquals(0, session.getPelletsCollected());
        assertEquals(0, session.getGhostsEaten());
        assertFalse(session.isInvincible());
        assertFalse(session.isPowered());
        assertFalse(session.isPickaxeActive());
    }

    @Test
    void onCoinCollected_incrementsPelletsAndScore() {
        session.onCoinCollected();

        assertEquals(1, session.getPelletsCollected());
        assertEquals(GameConfig.COIN_SCORE, session.getScore());
    }

    @Test
    void onPowerCoinCollected_grantsPoweredState() {
        session.onPowerCoinCollected();

        assertEquals(1, session.getPelletsCollected());
        assertEquals(PowerCoinConfig.SCORE, session.getScore());
        assertTrue(session.isPowered());

        for (int i = 0; i < PowerCoinConfig.POWERED_TICKS - 1; i++) {
            session.tickPowered();
        }
        assertTrue(session.isPowered());
        session.tickPowered();
        assertFalse(session.isPowered());
    }

    @Test
    void onGhostEaten_incrementsCountAndAddsScore() {
        session.onGhostEaten();

        assertEquals(1, session.getGhostsEaten());
        assertEquals(PowerCoinConfig.SCORE, session.getScore());
    }

    @Test
    void onExtraLifeCollected_incrementsLivesUpToMax() {
        session.onGhostHit();
        session.onExtraLifeCollected();

        assertEquals(GameConfig.INITIAL_LIVES, session.getLives());
    }

    @Test
    void onExtraLifeCollected_doesNotExceedMaxLives() {
        session.onExtraLifeCollected();

        assertEquals(ItemSpawnConfig.MAX_LIVES, session.getLives());
    }

    @Test
    void onPickaxeCollected_grantsPickaxeState() {
        session.onPickaxeCollected();

        assertEquals(PickaxeConfig.SCORE, session.getScore());
        assertTrue(session.isPickaxeActive());
    }

    @Test
    void onGhostHit_withLivesRemaining_grantsInvincibility() {
        session.onGhostHit();

        assertEquals(GameConfig.INITIAL_LIVES - 1, session.getLives());
        assertTrue(session.isPlaying());
        assertTrue(session.isInvincible());
        assertEquals(GameConfig.INVINCIBLE_TICKS, session.getInvincibleTicks());
    }

    @Test
    void onGhostHit_whenNoLivesLeft_setsGameOver() {
        session.onGhostHit();
        session.onGhostHit();
        session.onGhostHit();

        assertEquals(0, session.getLives());
        assertEquals(GameStatus.GAME_OVER, session.getStatus());
        assertFalse(session.isPlaying());
    }

    @Test
    void onAllCoinsCollected_setsWin() {
        session.onAllCoinsCollected();

        assertEquals(GameStatus.WIN, session.getStatus());
        assertFalse(session.isPlaying());
    }

    @Test
    void tickCounters_decrementUntilZero() {
        session.onPowerCoinCollected();
        session.onPickaxeCollected();
        session.onGhostHit();

        for (int i = 0; i < PowerCoinConfig.POWERED_TICKS; i++) {
            session.tickPowered();
        }
        for (int i = 0; i < PickaxeConfig.PICKAXE_TICKS; i++) {
            session.tickPickaxe();
        }
        for (int i = 0; i < GameConfig.INVINCIBLE_TICKS; i++) {
            session.tickInvincibility();
        }

        assertFalse(session.isPowered());
        assertFalse(session.isPickaxeActive());
        assertFalse(session.isInvincible());
    }

    @Test
    void tickCounters_doNotGoBelowZero() {
        session.tickPowered();
        session.tickPickaxe();
        session.tickInvincibility();

        assertFalse(session.isPowered());
        assertFalse(session.isPickaxeActive());
        assertFalse(session.isInvincible());
    }

    @Test
    void tickElapsed_tracksElapsedSeconds() {
        session.tickElapsed();
        session.tickElapsed();

        assertEquals(2 * GameConfig.TICK_MS / 1000, session.getElapsedSeconds());
    }

    @Test
    void reset_restoresInitialState() {
        session.onCoinCollected();
        session.onPowerCoinCollected();
        session.onPickaxeCollected();
        session.onGhostEaten();
        session.onGhostHit();
        session.onAllCoinsCollected();
        session.tickElapsed();

        session.reset();

        assertTrue(session.isPlaying());
        assertEquals(0, session.getScore());
        assertEquals(GameConfig.INITIAL_LIVES, session.getLives());
        assertEquals(0, session.getPelletsCollected());
        assertEquals(0, session.getGhostsEaten());
        assertEquals(0, session.getElapsedSeconds());
        assertFalse(session.isInvincible());
        assertFalse(session.isPowered());
        assertFalse(session.isPickaxeActive());
    }

}
