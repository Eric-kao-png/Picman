package com.picman.model.entity.ghostFactory;

import com.picman.model.entity.GhostKind;
import com.picman.model.entity.ghost.Ghost;
import com.picman.model.entity.ghost.RedGhost;

public class RedGhostFactory implements GhostFactory {
    @Override
    public Ghost createGhost() {
        return new RedGhost(GhostKind.spawn(GhostKind.RED));
    }
}
