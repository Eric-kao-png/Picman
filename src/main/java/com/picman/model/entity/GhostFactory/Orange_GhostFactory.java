package com.picman.model.entity.GhostFactory;

import com.picman.model.entity.GhostDefinitions;
import com.picman.model.entity.Ghost.Ghost;
import com.picman.model.entity.Ghost.Orange_Ghost;

public class Orange_GhostFactory implements GhostFactory{
    @Override
    public Ghost createGhost() {
        return new Orange_Ghost(GhostDefinitions.ALL.get(GhostDefinitions.RED_GHOST_INDEX + 3));
    }
}
