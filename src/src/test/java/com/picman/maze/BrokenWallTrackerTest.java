package com.picman.maze;

import com.picman.config.PickaxeConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BrokenWallTrackerTest {
    private BrokenWallTracker tracker;

    @BeforeEach
    void setUp() {
        tracker = new BrokenWallTracker();
    }

    @Test
    void untouchedCell_isNotBroken_andShowsSolidWall() {
        assertFalse(tracker.isBroken(2, 3));
        assertEquals(1f, tracker.getRecoveryProgress(2, 3), 0.001f);
    }

    @Test
    void breakWall_marksCellBroken_withZeroRecoveryProgress() {
        tracker.breakWall(4, 5);

        assertTrue(tracker.isBroken(4, 5));
        assertEquals(0f, tracker.getRecoveryProgress(4, 5), 0.001f);
    }

    @Test
    void recoveryProgress_increasesAsTicksPass() {
        tracker.breakWall(1, 1);

        tracker.tick();
        float afterOneTick = tracker.getRecoveryProgress(1, 1);

        assertTrue(afterOneTick > 0f);
        assertEquals(
                1f - (float) (PickaxeConfig.WALL_RECOVERY_TICKS - 1) / PickaxeConfig.WALL_RECOVERY_TICKS,
                afterOneTick,
                0.0001f);
    }

    @Test
    void cellRecovers_afterAllTicksElapse() {
        tracker.breakWall(6, 7);

        for (int i = 0; i < PickaxeConfig.WALL_RECOVERY_TICKS; i++) {
            tracker.tick();
        }

        assertFalse(tracker.isBroken(6, 7));
        assertEquals(1f, tracker.getRecoveryProgress(6, 7), 0.001f);
    }

    @Test
    void reset_clearsAllBrokenWalls() {
        tracker.breakWall(0, 0);
        tracker.breakWall(3, 3);

        tracker.reset();

        assertFalse(tracker.isBroken(0, 0));
        assertFalse(tracker.isBroken(3, 3));
    }

    @Test
    void tracksMultipleCellsIndependently() {
        tracker.breakWall(2, 2);
        for (int i = 0; i < 10; i++) {
            tracker.tick();
        }
        tracker.breakWall(8, 9);

        assertTrue(tracker.isBroken(2, 2));
        assertTrue(tracker.isBroken(8, 9));
        assertEquals(0f, tracker.getRecoveryProgress(8, 9), 0.001f);
        assertTrue(tracker.getRecoveryProgress(2, 2) > tracker.getRecoveryProgress(8, 9));
    }

    @Test
    void breakWallAgain_resetsRecoveryTimer() {
        tracker.breakWall(5, 5);
        for (int i = 0; i < 100; i++) {
            tracker.tick();
        }
        assertTrue(tracker.getRecoveryProgress(5, 5) > 0f);

        tracker.breakWall(5, 5);

        assertEquals(0f, tracker.getRecoveryProgress(5, 5), 0.001f);
        assertTrue(tracker.isBroken(5, 5));
    }
}
