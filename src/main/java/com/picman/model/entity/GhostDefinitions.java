package com.picman.model.entity;

import com.picman.config.GhostSpawn;
import com.picman.config.RenderTheme;
import com.picman.model.ai.GhostAIRegistry;
import com.picman.util.Direction;

import java.util.List;

/**
 * 關卡內幽靈生成定義（由靠近出口到深處排列）。
 */
public final class GhostDefinitions {
    public static final int RED_GHOST_INDEX = 0;    // 紅色幽靈在 ALL 中的索引

    public static final List<GhostSpawn> ALL = List.of(
            new GhostSpawn(14, 15, Direction.DOWN, RenderTheme.GHOST_RED, GhostAIRegistry.PATHFINDING_CHASE),
            new GhostSpawn(14, 14, Direction.DOWN, RenderTheme.GHOST_PINK, GhostAIRegistry.PINK_AMBUSH_PATHFINDING),
            new GhostSpawn(14, 13, Direction.DOWN, RenderTheme.GHOST_CYAN, GhostAIRegistry.CYAN_INKY_PATHFINDING),
            new GhostSpawn(13, 13, Direction.DOWN, RenderTheme.GHOST_ORANGE, GhostAIRegistry.ORANGE_CLYDE_PATHFINDING));

    private GhostDefinitions() {
    }
}
