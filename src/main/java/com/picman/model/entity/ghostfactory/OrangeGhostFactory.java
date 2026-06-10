package com.picman.model.entity.ghostfactory;

import com.picman.model.entity.GhostKind;
import com.picman.model.entity.ghost.Ghost;
import com.picman.model.entity.ghost.OrangeGhost;

public class OrangeGhostFactory implements GhostFactory {
    @Override
    public Ghost createGhost() {
        return new OrangeGhost(GhostKind.ORANGE.spawn());
    }
}
