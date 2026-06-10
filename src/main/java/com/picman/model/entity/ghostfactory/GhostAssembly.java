package com.picman.model.entity.ghostFactory;

import com.picman.model.entity.GhostRegistry;
import com.picman.model.entity.ghost.Ghost;

import java.util.List;

/**
 * 幽靈組裝工廠。
 * 使用 {@link GhostRegistry} 建立所有已註冊的幽靈。
 */
public final class GhostAssembly {
    private GhostAssembly() {
    }

    /**
     * 根據註冊表建立所有幽靈。
     * 確保在呼叫此方法前已初始化幽靈註冊表。
     */
    public static List<Ghost> createAll() {
        return GhostRegistry.getInstance().createAllGhosts();
    }
}
