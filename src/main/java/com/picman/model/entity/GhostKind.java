package com.picman.model.entity;

import com.picman.config.GhostSpawn;

/**
 * 四隻幽靈在 {@link GhostDefinitions#ALL} 中的固定順序。
 */
public enum GhostKind {
    RED(0),
    PINK(1),
    CYAN(2),
    ORANGE(3);

    private final int listIndex;

    GhostKind(int listIndex) {
        this.listIndex = listIndex;
    }

    public int index() {
        return listIndex;
    }

    public GhostSpawn spawn() {
        return GhostDefinitions.ALL.get(listIndex);
    }
}
