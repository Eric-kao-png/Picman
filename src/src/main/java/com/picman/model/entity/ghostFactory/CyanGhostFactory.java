package com.picman.model.entity.ghostFactory;

import com.picman.model.entity.GhostKind;
import com.picman.model.entity.ghost.CyanGhost;
import com.picman.model.entity.ghost.Ghost;

public class CyanGhostFactory implements GhostFactory {
    @Override
    public Ghost createGhost() {
        return new CyanGhost(GhostKind.spawn(GhostKind.CYAN));
    }
}
