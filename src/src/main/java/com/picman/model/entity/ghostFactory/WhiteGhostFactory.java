package com.picman.model.entity.ghostFactory;

import com.picman.model.entity.GhostKind;
import com.picman.model.entity.ghost.Ghost;
import com.picman.model.entity.ghost.WhiteGhost;

public class WhiteGhostFactory implements GhostFactory {
    @Override
    public Ghost createGhost() {
        return new WhiteGhost(GhostKind.spawn(GhostKind.WHITE));
    }
}
