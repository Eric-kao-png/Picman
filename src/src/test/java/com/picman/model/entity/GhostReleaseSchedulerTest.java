package com.picman.model.entity;

import com.picman.config.GhostHouseConfig;
import com.picman.config.PowerCoinConfig;
import com.picman.model.entity.ghost.Ghost;
import com.picman.model.entity.ghostFactory.GhostAssembly;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GhostReleaseSchedulerTest {
    private GhostReleaseScheduler scheduler;
    private List<Ghost> ghosts;

    @BeforeEach
    void setUp() {
        GhostRegistry.getInstance().reset();
        GhostSpawnInfo.initializeDefaultGhosts();
        ghosts = GhostAssembly.createAll();
        scheduler = new GhostReleaseScheduler();
        scheduler.reset(ghosts);
    }

    @Test
    void reset_putsAllGhostsInWaiting() {
        scheduler.tick(ghosts);
        scheduler.reset(ghosts);

        for (Ghost ghost : ghosts) {
            assertEquals(GhostMode.WAITING, ghost.getMode());
        }
    }

    @Test
    void firstTick_releasesFirstGhost() {
        scheduler.tick(ghosts);

        assertEquals(GhostMode.LEAVING, ghosts.get(GhostKind.index(GhostKind.RED)).getMode());
        assertEquals(GhostMode.WAITING, ghosts.get(GhostKind.index(GhostKind.PINK)).getMode());
    }

    @Test
    void releaseInterval_delaysNextGhost() {
        advanceTicks(1);

        assertEquals(GhostMode.LEAVING, ghosts.get(GhostKind.index(GhostKind.RED)).getMode());
        assertEquals(GhostMode.WAITING, ghosts.get(GhostKind.index(GhostKind.PINK)).getMode());

        advanceTicks(GhostHouseConfig.RELEASE_INTERVAL_TICKS);

        assertEquals(GhostMode.WAITING, ghosts.get(GhostKind.index(GhostKind.PINK)).getMode());

        advanceTicks(1);

        assertEquals(GhostMode.LEAVING, ghosts.get(GhostKind.index(GhostKind.PINK)).getMode());
    }

    @Test
    void initialRelease_skipsWhiteGhost() {
        advanceTicks(ticksToReleaseColoredGhosts());

        assertEquals(GhostMode.LEAVING, ghosts.get(GhostKind.index(GhostKind.ORANGE)).getMode());
        assertEquals(GhostMode.WAITING, ghosts.get(GhostKind.index(GhostKind.WHITE)).getMode());
    }

    @Test
    void onGhostEaten_releasesGhostAfterRespawnDelay() {
        exhaustInitialReleases();

        Ghost ghost = ghosts.get(GhostKind.index(GhostKind.RED));
        ghost.beEaten();
        scheduler.onGhostEaten(ghost, ghosts);

        advanceTicks(PowerCoinConfig.GHOST_RESPAWN_DELAY_TICKS);
        assertEquals(GhostMode.WAITING, ghost.getMode());

        advanceTicks(1);
        assertEquals(GhostMode.LEAVING, ghost.getMode());
    }

    @Test
    void onGhostEaten_queuesMultipleGhostsWithInterval() {
        exhaustInitialReleases();

        Ghost red = ghosts.get(GhostKind.index(GhostKind.RED));
        Ghost pink = ghosts.get(GhostKind.index(GhostKind.PINK));
        red.beEaten();
        pink.beEaten();
        scheduler.onGhostEaten(red, ghosts);
        scheduler.onGhostEaten(pink, ghosts);

        advanceTicks(PowerCoinConfig.GHOST_RESPAWN_DELAY_TICKS + 1);
        assertEquals(GhostMode.LEAVING, red.getMode());
        assertEquals(GhostMode.WAITING, pink.getMode());

        advanceTicks(GhostHouseConfig.RELEASE_INTERVAL_TICKS + 1);
        assertEquals(GhostMode.LEAVING, pink.getMode());
    }

    @Test
    void reset_clearsPendingRespawns() {
        exhaustInitialReleases();

        Ghost ghost = ghosts.get(GhostKind.index(GhostKind.RED));
        ghost.beEaten();
        scheduler.onGhostEaten(ghost, ghosts);

        scheduler.reset(ghosts);

        for (Ghost g : ghosts) {
            assertEquals(GhostMode.WAITING, g.getMode());
        }
    }

    private void exhaustInitialReleases() {
        advanceTicks(ticksToReleaseColoredGhosts());
    }

    private int ticksToReleaseColoredGhosts() {
        int coloredGhostCount = ghosts.size() - 1;
        return 1 + (GhostHouseConfig.RELEASE_INTERVAL_TICKS + 1) * (coloredGhostCount - 1);
    }

    private void advanceTicks(int count) {
        for (int i = 0; i < count; i++) {
            scheduler.tick(ghosts);
        }
    }
}
