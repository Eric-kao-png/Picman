package com.picman.ui;

/**
 * Game Over 覆蓋層的淡入與面板位移动畫。
 */
public class GameOverAnimation {
    private static final long OVERLAY_FADE_MS = 300L;
    public static final long BUTTON_STAGGER_MS = 90L;

    private long shownAt;
    private boolean visibleLastFrame;

    public void markShownIfNeeded() {
        if (!visibleLastFrame) {
            shownAt = System.currentTimeMillis();
            visibleLastFrame = true;
        }
    }

    public void reset() {
        shownAt = 0L;
        visibleLastFrame = false;
    }

    public boolean wasVisibleLastFrame() {
        return visibleLastFrame;
    }

    public double progress() {
        if (shownAt == 0L) {
            return 0.0;
        }
        return clamp((System.currentTimeMillis() - shownAt) / (double) OVERLAY_FADE_MS);
    }

    public float buttonProgress(long delayMs) {
        if (shownAt == 0L) {
            return 0f;
        }
        double raw = (System.currentTimeMillis() - shownAt - delayMs) / (double) OVERLAY_FADE_MS;
        return (float) easeOutCubic(clamp(raw));
    }

    public int animatedPanelY(int panelHeight, int screenHeight, double progress) {
        int targetY = (screenHeight - panelHeight) / 2;
        return targetY - (int) Math.round((1.0 - easeOutCubic(progress)) * 34.0);
    }

    public int panelWidth(int screenWidth) {
        return Math.min(390, Math.max(320, screenWidth - 58));
    }

    public int panelHeight(int screenHeight) {
        return Math.min(390, Math.max(360, screenHeight - 92));
    }

    private static double easeOutCubic(double value) {
        double inverted = 1.0 - value;
        return 1.0 - inverted * inverted * inverted;
    }

    private static double clamp(double value) {
        return Math.max(0.0, Math.min(1.0, value));
    }
}
