package com.picman.maze;

import com.picman.config.PickaxeConfig;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 追蹤被鎬子破壞、尚待恢復的牆格。
 */
public class BrokenWallTracker {
    private static final class Entry {
        private int remainingTicks;
        private final int totalTicks;

        private Entry(int ticks) {
            this.remainingTicks = ticks;
            this.totalTicks = ticks;
        }
    }

    private final Map<Long, Entry> recoveryTicks = new HashMap<>();

    public void reset() {
        recoveryTicks.clear();
    }

    public boolean isBroken(int col, int row) {
        return recoveryTicks.containsKey(key(col, row));
    }

    public void breakWall(int col, int row) {
        recoveryTicks.put(key(col, row), new Entry(PickaxeConfig.WALL_RECOVERY_TICKS));
    }

    /**
     * 0 = 剛破壞（視覺上無牆），1 = 即將恢復（牆完全顯示）。
     * 不在追蹤表內則為 1（實心牆）。
     */
    public float getRecoveryProgress(int col, int row) {
        Entry entry = recoveryTicks.get(key(col, row));
        if (entry == null || entry.totalTicks <= 0) {
            return 1f;
        }
        return 1f - (float) entry.remainingTicks / entry.totalTicks;
    }

    public void tick() {
        Iterator<Map.Entry<Long, Entry>> iterator = recoveryTicks.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Entry> entry = iterator.next();
            entry.getValue().remainingTicks--;
            if (entry.getValue().remainingTicks <= 0) {
                iterator.remove();
            }
        }
    }

    private static long key(int col, int row) {
        return ((long) row << 32) | (col & 0xffffffffL);
    }
}
