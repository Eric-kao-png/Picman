package com.picman.collision;

import com.picman.config.GameConfig;
import com.picman.model.entity.GridPosition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CollisionDetectorTest {
    private static final double HIT_RADIUS =
            GameConfig.TILE_SIZE * GameConfig.ENTITY_HIT_RADIUS_RATIO;

    @Test
    void sameCellCenters_overlap() {
        GridPosition pacman = new GridPosition(1, 1);
        GridPosition ghost = new GridPosition(1, 1);

        assertTrue(CollisionDetector.entitiesOverlap(pacman, ghost));
    }

    @Test
    void distanceWithinHitRadius_overlaps() {
        GridPosition a = positionAt(24, 24);
        GridPosition b = positionAt(24 + HIT_RADIUS - 1, 24);

        assertTrue(CollisionDetector.entitiesOverlap(a, b));
    }

    @Test
    void distanceExactlyAtHitRadius_overlaps() {
        GridPosition a = positionAt(24, 24);
        GridPosition b = positionAt(24 + HIT_RADIUS, 24);

        assertTrue(CollisionDetector.entitiesOverlap(a, b));
    }

    @Test
    void distanceBeyondHitRadius_doesNotOverlap() {
        GridPosition a = positionAt(24, 24);
        GridPosition b = positionAt(24 + HIT_RADIUS + 1, 24);

        assertFalse(CollisionDetector.entitiesOverlap(a, b));
    }

    @Test
    void diagonalDistanceWithinHitRadius_overlaps() {
        double offset = HIT_RADIUS / Math.sqrt(2);
        GridPosition a = positionAt(24, 24);
        GridPosition b = positionAt(24 + offset, 24 + offset);

        assertTrue(CollisionDetector.entitiesOverlap(a, b));
    }

    @Test
    void diagonalDistanceBeyondHitRadius_doesNotOverlap() {
        double offset = HIT_RADIUS / Math.sqrt(2) + 1;
        GridPosition a = positionAt(24, 24);
        GridPosition b = positionAt(24 + offset, 24 + offset);

        assertFalse(CollisionDetector.entitiesOverlap(a, b));
    }

    @Test
    void farApartEntities_doNotOverlap() {
        GridPosition pacman = new GridPosition(1, 1);
        GridPosition ghost = new GridPosition(10, 10);

        assertFalse(CollisionDetector.entitiesOverlap(pacman, ghost));
    }

    private static GridPosition positionAt(double centerX, double centerY) {
        GridPosition position = new GridPosition(0, 0);
        position.setCenter(centerX, centerY);
        return position;
    }
}
