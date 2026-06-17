package com.picman.model.entity.ghostFactory;

import com.picman.model.entity.GhostKind;
import com.picman.model.entity.ghost.Ghost;
import com.picman.model.entity.ghost.OrangeGhost;

public class OrangeGhostFactory implements GhostFactory {
    @Override
    public Ghost createGhost() {
        return new OrangeGhost(GhostKind.spawn(GhostKind.ORANGE));
    }
}
