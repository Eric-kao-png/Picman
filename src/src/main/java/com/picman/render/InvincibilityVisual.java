package com.picman.render;

import com.picman.model.GameSession;

/**
 * 無敵狀態下的 Pac-Man 閃爍顯示。
 */
public final class InvincibilityVisual {
    private static final int BLINK_INTERVAL_TICKS = 5;

    private InvincibilityVisual() {
    }

    public static boolean isPacmanVisible(GameSession session) {
        if (!session.isInvincible()) {
            return true;
        }
        return (session.getInvincibleTicks() / BLINK_INTERVAL_TICKS) % 2 != 0;
    }
}
