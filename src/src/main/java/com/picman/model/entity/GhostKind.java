package com.picman.model.entity;

import com.picman.config.GhostSpawn;

/**
 * 幽靈類型常量與索引管理。
 * 提供向後相容的 API，背後使用 {@link GhostRegistry}。
 */
public class GhostKind {
    public static final String RED = "RED";
    public static final String PINK = "PINK";
    public static final String CYAN = "CYAN";
    public static final String ORANGE = "ORANGE";
    public static final String WHITE = "WHITE";

    private GhostKind() {
    }

    /**
     * 獲取幽靈的索引。
     */
    public static int index(String ghostName) {
        return GhostRegistry.getInstance().getIndex(ghostName);
    }

    /**
     * 根據幽靈名稱獲取生成配置。
     */
    public static GhostSpawn spawn(String ghostName) {
        return GhostRegistry.getInstance().getSpawn(ghostName);
    }
}
