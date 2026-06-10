package com.picman.model.entity.ghostfactory;

import com.picman.model.entity.GhostKind;
import com.picman.model.entity.ghost.CyanGhost;
import com.picman.model.entity.ghost.Ghost;

public class CyanGhostFactory implements GhostFactory {
    @Override
    public Ghost createGhost() {
        return new CyanGhost(GhostKind.CYAN.spawn());
    }
}
