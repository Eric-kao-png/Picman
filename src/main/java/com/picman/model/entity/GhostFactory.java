package com.picman.model.entity;

import com.picman.config.GameConfig;
import com.picman.config.GhostSpawn;
import com.picman.config.RenderTheme;
import com.picman.util.Direction;

import java.util.ArrayList;
import java.util.List;

public final class GhostFactory {
    private static final GhostSpawn[] SPAWNS = {
            new GhostSpawn(10, 11, Direction.LEFT, RenderTheme.GHOST_RED),
            new GhostSpawn(12, 11, Direction.RIGHT, RenderTheme.GHOST_PINK),
            new GhostSpawn(14, 11, Direction.UP, RenderTheme.GHOST_CYAN),
            new GhostSpawn(16, 11, Direction.DOWN, RenderTheme.GHOST_ORANGE),
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
