package com.picman.config;

/**
 * 隨機道具生成相關常數。
 */
public final class ItemSpawnConfig {
    private ItemSpawnConfig() {
    }

    public static final int MAX_LIVES = GameConfig.INITIAL_LIVES;
    /** 開局後首次生成前的等待 tick */
    public static final int INITIAL_SPAWN_DELAY_TICKS = 600;
    /** 兩次生成嘗試之間的間隔 tick（約 15 秒） */
    public static final int SPAWN_INTERVAL_TICKS = 900;
    /** 道具在地圖上停留的 tick（約 7.5 秒） */
    public static final int ITEM_DURATION_TICKS = 450;
}
