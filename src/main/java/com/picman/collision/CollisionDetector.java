package com.picman.collision;

import com.picman.config.GameConfig;
import com.picman.model.entity.GridPosition;

public final class CollisionDetector {
    private CollisionDetector() {
    }

    public static boolean entitiesOverlap(GridPosition a, GridPosition b) {
        double dx = a.getCenterX() - b.getCenterX();
        double dy = a.getCenterY() - b.getCenterY();
        double hitRadius = GameConfig.TILE_SIZE * GameConfig.ENTITY_HIT_RADIUS_RATIO;
        return dx * dx + dy * dy <= hitRadius * hitRadius;
    }
}
