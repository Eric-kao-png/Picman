package com.picman.game;

import com.picman.config.GameConfig;
import com.picman.config.PowerCoinConfig;
import com.picman.model.GameSession;
import com.picman.model.GameStatus;
import com.picman.model.entity.GhostMode;
import com.picman.model.entity.GhostRegistry;
import com.picman.model.entity.GhostReleaseScheduler;
import com.picman.model.entity.GhostSpawnInfo;
import com.picman.model.entity.Pacman;
import com.picman.model.entity.ghost.Ghost;
import com.picman.model.entity.ghostFactory.GhostAssembly;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameCollisionHandlerTest {
    private GameCollisionHandler handler;
    private Pacman pacman;
    private List<Ghost> ghosts;
    private GameSession session;
    private GhostReleaseScheduler releaseScheduler;

    @BeforeEach
    void setUp() {
        GhostRegistry.getInstance().reset();
        GhostSpawnInfo.initializeDefaultGhosts();

        handler = new GameCollisionHandler();
        pacman = new Pacman();
        ghosts = GhostAssembly.createAll();
        session = new GameSession();
        releaseScheduler = new GhostReleaseScheduler();
        releaseScheduler.reset(ghosts);
    }

    @Test
    void noOverlap_doesNotChangeSession() {
        handler.resolve(pacman, ghosts, session, releaseScheduler);

        assertEquals(GameConfig.INITIAL_LIVES, session.getLives());
        assertEquals(0, session.getGhostsEaten());
    }

    @Test
    void overlapWithLeavingGhost_deductsLifeAndResetsPacman() {
        Ghost ghost = leavingGhost(ghosts.get(0));
        movePacmanOnto(ghost);

        handler.resolve(pacman, ghosts, session, releaseScheduler);

        assertEquals(GameConfig.INITIAL_LIVES - 1, session.getLives());
        assertTrue(session.isInvincible());
        assertEquals(GameConfig.PACMAN_START_COL, pacman.getCol());
        assertEquals(GameConfig.PACMAN_START_ROW, pacman.getRow());
        assertEquals(GhostMode.WAITING, ghost.getMode());
    }

    @Test
    void overlapWhileInvincible_doesNotDeductLife() {
        Ghost ghost = leavingGhost(ghosts.get(0));
        session.onGhostHit();
        movePacmanOnto(ghost);

        handler.resolve(pacman, ghosts, session, releaseScheduler);

        assertEquals(GameConfig.INITIAL_LIVES - 1, session.getLives());
    }

    @Test
    void waitingGhost_doesNotCollide() {
        Ghost ghost = ghosts.get(0);
        movePacmanOnto(ghost);

        handler.resolve(pacman, ghosts, session, releaseScheduler);

        assertEquals(GameConfig.INITIAL_LIVES, session.getLives());
        assertEquals(GhostMode.WAITING, ghost.getMode());
    }

    @Test
    void powered_eatsFrightenedGhost() {
        session.onPowerCoinCollected();
        int scoreBefore = session.getScore();
        Ghost ghost = frightenedGhost(ghosts.get(0));
        movePacmanOnto(ghost);

        handler.resolve(pacman, ghosts, session, releaseScheduler);

        assertEquals(1, session.getGhostsEaten());
        assertEquals(scoreBefore + PowerCoinConfig.SCORE, session.getScore());
        assertEquals(GhostMode.WAITING, ghost.getMode());
        assertEquals(GameConfig.INITIAL_LIVES, session.getLives());
    }

    @Test
    void powered_overlapWithNonFrightenedGhost_stillDeductsLife() {
        session.onPowerCoinCollected();
        Ghost ghost = leavingGhost(ghosts.get(0));
        movePacmanOnto(ghost);

        handler.resolve(pacman, ghosts, session, releaseScheduler);

        assertEquals(GameConfig.INITIAL_LIVES - 1, session.getLives());
        assertEquals(0, session.getGhostsEaten());
    }

    @Test
    void overlapOnLastLife_setsGameOverWithoutResettingPacman() {
        Ghost ghost = leavingGhost(ghosts.get(0));

        loseLifeAgainst(ghost);
        loseLifeAgainst(ghost);

        movePacmanOnto(ghost);
        int colBefore = pacman.getCol();
        int rowBefore = pacman.getRow();

        handler.resolve(pacman, ghosts, session, releaseScheduler);

        assertEquals(GameStatus.GAME_OVER, session.getStatus());
        assertEquals(0, session.getLives());
        assertEquals(colBefore, pacman.getCol());
        assertEquals(rowBefore, pacman.getRow());
    }

    private void loseLifeAgainst(Ghost ghost) {
        movePacmanOnto(ghost);
        handler.resolve(pacman, ghosts, session, releaseScheduler);
        exhaustInvincibility();
        releaseScheduler.reset(ghosts);
        leavingGhost(ghost);
    }

    private void exhaustInvincibility() {
        while (session.isInvincible()) {
            session.tickInvincibility();
        }
    }

    private static Ghost leavingGhost(Ghost ghost) {
        ghost.releaseFromHouse();
        return ghost;
    }

    private static Ghost frightenedGhost(Ghost ghost) {
        leavingGhost(ghost);
        ghost.enterFrightened();
        return ghost;
    }

    private void movePacmanOnto(Ghost ghost) {
        pacman.getPosition().setCenter(
                ghost.getPosition().getCenterX(),
                ghost.getPosition().getCenterY());
    }
}
