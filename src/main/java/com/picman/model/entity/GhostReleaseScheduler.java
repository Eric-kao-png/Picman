package com.picman.model.entity;

import com.picman.config.GhostHouseConfig;
import com.picman.model.entity.Ghost.Ghost;

import java.util.List;

public class GhostReleaseScheduler {
    private int nextIndex;
    private int cooldownTicks;

    public void reset(List<Ghost> ghosts) {
        nextIndex = 0;
        cooldownTicks = 0;
        for (Ghost ghost : ghosts) {
            ghost.enterHouse();
        }
    }

    public void tick(List<Ghost> ghosts) {
        if (nextIndex >= ghosts.size()) {
            return;
        }
        if (cooldownTicks > 0) {
            cooldownTicks--;
            return;
        }

        ghosts.get(nextIndex).releaseFromHouse();
        nextIndex++;
        cooldownTicks = GhostHouseConfig.RELEASE_INTERVAL_TICKS;
    }

}
