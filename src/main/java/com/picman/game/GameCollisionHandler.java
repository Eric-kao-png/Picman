package com.picman.game;

import com.picman.collision.CollisionDetector;
import com.picman.model.GameSession;
import com.picman.model.GameStatus;
import com.picman.model.entity.Ghost;
import com.picman.model.entity.GhostReleaseScheduler;
import com.picman.model.entity.Pacman;

import java.util.List;

/**
 * 處理 Pac-Man 與幽靈碰撞：Powered 時吃幽靈，否則被幽靈撞到扣命。
 */
public class GameCollisionHandler {
    public void resolve(
            Pacman pacman,
            List<Ghost> ghosts,
            GameSession session,
            GhostReleaseScheduler releaseScheduler) {
        if (session.isPowered()) {
            resolvePoweredCollisions(pacman, ghosts, session, releaseScheduler);
            return;
        }

        if (session.isInvincible()) {
            return;
        }

        boolean hit = ghosts.stream()
                .filter(Ghost::isActiveForCollision)
                .filter(ghost -> !ghost.isEdibleByPacman())
                .anyMatch(ghost -> CollisionDetector.entitiesOverlap(
                        ghost.getPosition(),
                        pacman.getPosition()));

        if (!hit) {
            return;
        }

        session.onGhostHit();
        if (session.getStatus() == GameStatus.PLAYING) {
            pacman.reset();
            releaseScheduler.reset(ghosts);
        }
    }

    private void resolvePoweredCollisions(
            Pacman pacman,
            List<Ghost> ghosts,
            GameSession session,
            GhostReleaseScheduler releaseScheduler) {
        for (Ghost ghost : ghosts) {
            if (!ghost.isEdibleByPacman()) {
                continue;
            }
            if (!CollisionDetector.entitiesOverlap(ghost.getPosition(), pacman.getPosition())) {
                continue;
            }
            session.onGhostEaten();
            ghost.beEaten();
            releaseScheduler.onGhostEaten(ghost, ghosts);
        }
    }
}
