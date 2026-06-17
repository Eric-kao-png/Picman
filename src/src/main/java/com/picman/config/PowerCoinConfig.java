package com.picman.config;

/**
 * 大金幣（能量豆）相關常數。
 */
public final class PowerCoinConfig {
    private PowerCoinConfig() {
    }

    public static final int SCORE = 50;
    /** 可反吃幽靈的持續 tick 數（約 6 秒 @ 16ms/tick） */
    public static final int POWERED_TICKS = 375;
    /** 大金幣被吃後，幽靈回房復活的等待 tick */
    public static final int GHOST_RESPAWN_DELAY_TICKS = 90;

    /**
     * 關卡內大金幣位置（col, row），選在四隅走廊。
     */
    public static final int[][] POSITIONS = {
            {1, 4},
            {26, 4},
            {1, 25},
            {26, 25},
    };
}
