package com.picman.model.entity;

import com.picman.config.GhostSpawn;
import com.picman.config.RenderTheme;
import com.picman.model.ai.GhostAIRegistry;
import com.picman.model.entity.ghostFactory.*;
import com.picman.util.Direction;

import java.util.List;

/**
 * 預設幽靈生成配置和工廠註冊。
 * 此類初始化預設的 5 隻幽靈到 {@link GhostRegistry}。
 * 
 * 若要新增更多幽靈，直接在應用啟動時呼叫 {@link GhostRegistry#registerGhost(String, GhostSpawn, GhostFactory.GhostFactory)}。
 */
public final class GhostSpawnInfo {
    // 預設的幽靈配置（保留以供參考）
    public static final List<GhostSpawn> ALL = List.of(
            new GhostSpawn(14, 15, Direction.DOWN, RenderTheme.GHOST_RED, GhostAIRegistry.PATHFINDING_CHASE),
            new GhostSpawn(14, 14, Direction.DOWN, RenderTheme.GHOST_PINK, GhostAIRegistry.PINK_AMBUSH_PATHFINDING),
            new GhostSpawn(14, 13, Direction.DOWN, RenderTheme.GHOST_CYAN, GhostAIRegistry.CYAN_INKY_PATHFINDING),
            new GhostSpawn(13, 14, Direction.DOWN, RenderTheme.GHOST_ORANGE, GhostAIRegistry.ORANGE_CLYDE_PATHFINDING),
            new GhostSpawn(13, 13, Direction.DOWN, RenderTheme.GHOST_WHITE, GhostAIRegistry.PATHFINDING_CHASE));

    private GhostSpawnInfo() {
    }

    /**
     * 初始化預設幽靈到註冊表。
     * 此方法應在應用啟動時呼叫一次。
     */
    public static void initializeDefaultGhosts() {
        GhostRegistry registry = GhostRegistry.getInstance();
        if (!registry.getAllGhostNames().isEmpty()) {
            return;
        }

        registry.registerGhost(
                GhostKind.RED,
                ALL.get(0),
                new RedGhostFactory()
        );
        
        registry.registerGhost(
                GhostKind.PINK,
                ALL.get(1),
                new PinkGhostFactory()
        );
        
        registry.registerGhost(
                GhostKind.CYAN,
                ALL.get(2),
                new CyanGhostFactory()
        );
        
        registry.registerGhost(
                GhostKind.ORANGE,
                ALL.get(3),
                new OrangeGhostFactory()
        );
        
        registry.registerGhost(
                GhostKind.WHITE,
                ALL.get(4),
                new WhiteGhostFactory()
        );
    }
}

