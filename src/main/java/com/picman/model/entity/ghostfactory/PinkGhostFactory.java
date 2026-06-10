package com.picman.model.entity.ghostfactory;

import com.picman.model.entity.GhostKind;
import com.picman.model.entity.ghost.Ghost;
import com.picman.model.entity.ghost.PinkGhost;

public class PinkGhostFactory implements GhostFactory {
    @Override
    public Ghost createGhost() {
        return new PinkGhost(GhostKind.PINK.spawn());
    }
}
