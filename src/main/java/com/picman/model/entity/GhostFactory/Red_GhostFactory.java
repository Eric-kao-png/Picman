package com.picman.model.entity.GhostFactory;

import com.picman.model.entity.GhostDefinitions;
import com.picman.model.entity.Ghost.Ghost;
import com.picman.model.entity.Ghost.Red_Ghost;

public class Red_GhostFactory implements GhostFactory {
    @Override
    public Ghost createGhost() {
        return new Red_Ghost(GhostDefinitions.ALL.get(GhostDefinitions.RED_GHOST_INDEX));
    }
}
