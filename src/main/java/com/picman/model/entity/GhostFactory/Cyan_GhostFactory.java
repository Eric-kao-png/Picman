package com.picman.model.entity.GhostFactory;

import com.picman.model.entity.GhostDefinitions;
import com.picman.model.entity.Ghost.Ghost;
import com.picman.model.entity.Ghost.Cyan_Ghost;

public class Cyan_GhostFactory implements GhostFactory{
    @Override
    public Ghost createGhost() {
        return new Cyan_Ghost(GhostDefinitions.ALL.get(GhostDefinitions.RED_GHOST_INDEX + 2));
    }
}
