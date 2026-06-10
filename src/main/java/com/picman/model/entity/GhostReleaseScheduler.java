package com.picman.model.entity;

import com.picman.config.GhostHouseConfig;
import com.picman.config.PowerCoinConfig;
import com.picman.model.entity.ghost.Ghost;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class GhostReleaseScheduler {
    private int nextIndex;
    private int cooldownTicks;
    private int respawnCooldownTicks;
    private final Queue<Integer> respawnQueue = new ArrayDeque<>();

    public void reset(List<Ghost> ghosts) {
        nextIndex = 0;
        cooldownTicks = 0;
        respawnCooldownTicks = 0;
        respawnQueue.clear();
        for (Ghost ghost : ghosts) {
            ghost.reset();
        }
    }

    public void tick(List<Ghost> ghosts) {
        tickInitialRelease(ghosts);
        tickRespawnRelease(ghosts);
    }

    public void onGhostEaten(Ghost ghost, List<Ghost> ghosts) {
        int index = ghosts.indexOf(ghost);
        if (index >= 0 && !respawnQueue.contains(index)) {
            respawnQueue.add(index);
        }
        if (respawnCooldownTicks == 0) {
            respawnCooldownTicks = PowerCoinConfig.GHOST_RESPAWN_DELAY_TICKS;
        }
    }

    private void tickInitialRelease(List<Ghost> ghosts) {
        if (nextIndex >= ghosts.size() - 1) {   // 有刻意 - 1，因為最後一隻幽靈（白色）不受初始釋放機制控制
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

    private void tickRespawnRelease(List<Ghost> ghosts) {
        if (respawnQueue.isEmpty()) {
            return;
        }
        if (respawnCooldownTicks > 0) {
            respawnCooldownTicks--;
            return;
        }

        Integer index = respawnQueue.poll();
        if (index == null || index < 0 || index >= ghosts.size()) {
            return;
        }

        Ghost ghost = ghosts.get(index);
        if (ghost.getMode() == GhostMode.WAITING) {
            ghost.releaseFromHouse();
        }

        if (!respawnQueue.isEmpty()) {
            respawnCooldownTicks = GhostHouseConfig.RELEASE_INTERVAL_TICKS;
        }
    }
}
