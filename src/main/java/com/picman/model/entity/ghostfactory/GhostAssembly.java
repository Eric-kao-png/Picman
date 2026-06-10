package com.picman.model.entity.ghostfactory;

import com.picman.model.entity.ghost.Ghost;

import java.util.List;

public final class GhostAssembly {
    private static final List<GhostFactory> FACTORIES = List.of(
            new RedGhostFactory(),
            new PinkGhostFactory(),
            new CyanGhostFactory(),
            new OrangeGhostFactory());

    private GhostAssembly() {
    }

    public static List<Ghost> createAll() {
        return FACTORIES.stream()
                .map(GhostFactory::createGhost)
                .toList();
    }
}
