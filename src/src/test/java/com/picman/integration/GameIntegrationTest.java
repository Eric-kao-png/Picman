package com.picman.integration;

import com.picman.Game;
import com.picman.config.GameConfig;
import com.picman.model.GameStatus;
import com.picman.util.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameIntegrationTest {
    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
        game.restart();
    }

    @Test
    void update_collectsCoinAtSpawn() {
        game.update();

        assertEquals(GameConfig.COIN_SCORE, game.getScore());
        assertEquals(1, game.getPelletsCollected());
        assertEquals(GameStatus.PLAYING, game.getStatus());
    }

    @Test
    void restart_restoresInitialState() {
        game.update();
        game.restart();

        assertEquals(0, game.getScore());
        assertEquals(0, game.getPelletsCollected());
        assertEquals(GameStatus.PLAYING, game.getStatus());
    }

    @Test
    void movingRight_collectsAnotherCoin() {
        game.update();
        game.setActiveDirection(Direction.RIGHT);

        for (int i = 0; i < 30; i++) {
            game.update();
        }

        assertTrue(game.getPelletsCollected() >= 2);
        assertTrue(game.getScore() >= GameConfig.COIN_SCORE * 2);
    }

    @Test
    void update_incrementsElapsedTime() {
        for (int i = 0; i < 70; i++) {
            game.update();
        }

        assertTrue(game.getElapsedSeconds() > 0);
    }

    @Test
    void setActiveDirection_ignoredAfterRestart() {
        game.update();
        game.restart();
        game.setActiveDirection(Direction.LEFT);

        for (int i = 0; i < 5; i++) {
            game.update();
        }

        assertEquals(1, game.getPelletsCollected());
    }
}
