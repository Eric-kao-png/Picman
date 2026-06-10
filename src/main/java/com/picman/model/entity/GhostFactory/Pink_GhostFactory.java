package com.picman.model.entity.GhostFactory;

import com.picman.model.entity.GhostDefinitions;
import com.picman.model.entity.Ghost.Ghost;
import com.picman.model.entity.Ghost.Pink_Ghost;

public class Pink_GhostFactory implements GhostFactory {
    @Override
    public Ghost createGhost() {
        return new Pink_Ghost(GhostDefinitions.ALL.get(GhostDefinitions.RED_GHOST_INDEX + 1));
    }
}
