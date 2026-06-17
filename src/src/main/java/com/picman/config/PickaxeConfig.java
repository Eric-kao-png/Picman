package com.picman.config;

/**
 * 鎬子道具相關常數。不可破壞最外圍邊界牆與幽靈房區域（見 {@link com.picman.maze.BreakableWallRules}）。
 */
public final class PickaxeConfig {
    private PickaxeConfig() {
    }

    public static final int SCORE = 30;
    /** 可破壞牆體的持續 tick 數（約 6 秒） */
    public static final int PICKAXE_TICKS = 375;
    /** 牆體被破壞後，恢復為牆的等待 tick（約 10 秒） */
    public static final int WALL_RECOVERY_TICKS = 600;
}
