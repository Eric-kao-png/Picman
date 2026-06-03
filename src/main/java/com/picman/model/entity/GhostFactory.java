package com.picman.model.entity;

import com.picman.config.GameConfig;
import com.picman.config.GhostSpawn;
import com.picman.config.RenderTheme;
import com.picman.util.Direction;

import java.util.ArrayList;
import java.util.List;

public final class GhostFactory {
    /** 由靠近出口到深處排列；依序從底部出口放出 */
    private static final GhostSpawn[] SPAWNS = {
            new GhostSpawn(14, 15, Direction.DOWN, RenderTheme.GHOST_RED),
            new GhostSpawn(14, 14, Direction.DOWN, RenderTheme.GHOST_PINK),
            new GhostSpawn(14, 13, Direction.DOWN, RenderTheme.GHOST_CYAN),
            new GhostSpawn(13, 13, Direction.DOWN, RenderTheme.GHOST_ORANGE),
    };

    private GhostFactory() {
    }

    public static List<Ghost> createAll() {
        List<Ghost> ghosts = new ArrayList<>(GameConfig.GHOST_COUNT);
        for (GhostSpawn spawn : SPAWNS) {
            ghosts.add(new Ghost(spawn));
        }
        return List.copyOf(ghosts);
    }
}
