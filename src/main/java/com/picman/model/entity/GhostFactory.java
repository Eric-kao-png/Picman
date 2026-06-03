package com.picman.model.entity;

import com.picman.config.GhostSpawn;

import java.util.ArrayList;
import java.util.List;

public final class GhostFactory {
    private GhostFactory() {
    }

    public static List<Ghost> createAll() {
        List<Ghost> ghosts = new ArrayList<>(GhostDefinitions.ALL.size());
        for (GhostSpawn spawn : GhostDefinitions.ALL) {
            ghosts.add(new Ghost(spawn));
        }
        return List.copyOf(ghosts);
    }
}
