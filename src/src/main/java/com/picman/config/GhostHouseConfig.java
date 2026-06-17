package com.picman.config;

public final class GhostHouseConfig {
    private GhostHouseConfig() {
    }

    /** 幽靈房外框（單層牆；col 11 與 16 為地圖左右鏡像對） */
    public static final int WALL_MIN_COL = 11;
    public static final int WALL_MAX_COL = 16;
    public static final int WALL_TOP_ROW = 12;
    public static final int WALL_BOTTOM_ROW = 16;

    /** 房內可走區域（不含牆） */
    public static final int INTERIOR_MIN_COL = 12;
    public static final int INTERIOR_MAX_COL = 15;
    public static final int INTERIOR_MIN_ROW = 13;
    public static final int INTERIOR_MAX_ROW = 15;

    /** 底部中央出口（牆上的門洞） */
    public static final int DOOR_COL_LEFT = 13;
    public static final int DOOR_COL_RIGHT = 14;

    /** 離開房間後的列（門口外通道） */
    public static final int EXIT_ROW = 17;

    /** 鎬子不可破壞的幽靈房區域（含外殼牆，略大於房體幾何） */
    public static final int PICKAXE_PROTECT_MIN_COL = WALL_MIN_COL - 1;
    public static final int PICKAXE_PROTECT_MAX_COL = WALL_MAX_COL + 1;
    public static final int PICKAXE_PROTECT_MIN_ROW = WALL_TOP_ROW - 1;
    public static final int PICKAXE_PROTECT_MAX_ROW = EXIT_ROW + 1;

    public static final int RELEASE_INTERVAL_TICKS = 45;

    public static boolean isDoorCell(int col, int row) {
        return row == WALL_BOTTOM_ROW
                && col >= DOOR_COL_LEFT
                && col <= DOOR_COL_RIGHT;
    }

    /** 門口正下方的出口通道（不產生金幣） */
    public static boolean isExitCorridorCell(int col, int row) {
        return row == EXIT_ROW
                && col >= WALL_MIN_COL
                && col <= WALL_MAX_COL;
    }
}
