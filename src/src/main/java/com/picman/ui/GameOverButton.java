package com.picman.ui;

import com.picman.config.GameConfig;

import javax.swing.JButton;
import javax.swing.Timer;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class GameOverButton extends JButton {
    private final boolean primary;
    private final Timer hoverTimer;
    private float hoverProgress;
    private float animationAlpha;
    private boolean hoverTarget;

    public GameOverButton(String text, boolean primary) {
        super(text);
        this.primary = primary;
        this.hoverTimer = new Timer(GameConfig.TICK_MS, e -> updateHover());
    }

    public void setAnimationAlpha(float animationAlpha) {
        this.animationAlpha = Math.max(0f, Math.min(1f, animationAlpha));
        repaint();
    }

    public void setHoverTarget(boolean hoverTarget) {
        this.hoverTarget = hoverTarget;
        if (!hoverTimer.isRunning()) {
            hoverTimer.start();
        }
    }

    private void updateHover() {
        float target = hoverTarget ? 1f : 0f;
        if (Math.abs(hoverProgress - target) < 0.02f) {
            hoverProgress = target;
            hoverTimer.stop();
        } else {
            hoverProgress += (target - hoverProgress) * 0.22f;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setComposite(AlphaComposite.SrcOver.derive(animationAlpha));

        Color base = primary ? GameOverTheme.PRIMARY_BUTTON : GameOverTheme.SECONDARY_BUTTON;
        Color hover = primary ? GameOverTheme.PRIMARY_BUTTON_HOVER : GameOverTheme.SECONDARY_BUTTON_HOVER;
        Color fill = mix(base, hover, hoverProgress);
        Color stroke = primary ? new Color(255, 240, 155, 120) : GameOverTheme.SECONDARY_STROKE;
        int arc = 18;
        int shadowAlpha = primary ? 72 : 55;

        g2.setColor(new Color(0, 0, 0, shadowAlpha));
        g2.fillRoundRect(3, 5, getWidth() - 6, getHeight() - 6, arc, arc);
        g2.setColor(fill);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 7, arc, arc);
        g2.setColor(stroke);
        g2.setStroke(new java.awt.BasicStroke(1.2f));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 7, arc, arc);

        g2.setFont(getFont());
        g2.setColor(primary ? GameOverTheme.PRIMARY_TEXT : GameOverTheme.TEXT_WHITE);
        FontMetrics metrics = g2.getFontMetrics();
        int textX = (getWidth() - metrics.stringWidth(getText())) / 2;
        int textY = (getHeight() - 7 - metrics.getHeight()) / 2 + metrics.getAscent();
        g2.drawString(getText(), textX, textY);
        g2.dispose();
    }

    private static Color mix(Color from, Color to, float amount) {
        float clamped = Math.max(0f, Math.min(1f, amount));
        int red = Math.round(from.getRed() + (to.getRed() - from.getRed()) * clamped);
        int green = Math.round(from.getGreen() + (to.getGreen() - from.getGreen()) * clamped);
        int blue = Math.round(from.getBlue() + (to.getBlue() - from.getBlue()) * clamped);
        int alpha = Math.round(from.getAlpha() + (to.getAlpha() - from.getAlpha()) * clamped);
        return new Color(red, green, blue, alpha);
    }
}
