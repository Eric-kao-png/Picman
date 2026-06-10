package com.picman.model.entity.ghostFactory;

import com.picman.model.entity.GhostKind;
import com.picman.model.entity.ghost.Ghost;
import com.picman.model.entity.ghost.PinkGhost;

public class PinkGhostFactory implements GhostFactory {
    @Override
    public Ghost createGhost() {
        return new PinkGhost(GhostKind.spawn(GhostKind.PINK));
    }
}
