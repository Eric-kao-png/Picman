package com.picman.model.ai;

/**
 * 幽靈 AI 策略實例（可共用、無狀態）。
 */
public final class GhostAIRegistry {
    public static final GhostAI PATHFINDING_CHASE = new PathfindingChaseGhostAI();
    public static final GhostAI PINK_AMBUSH_PATHFINDING = new PinkPathfindingGhostAI();
    public static final GhostAI CYAN_INKY_PATHFINDING = new CyanPathfindingGhostAI();
    public static final GhostAI ORANGE_CLYDE_PATHFINDING = new OrangePathfindingGhostAI();
    public static final GhostAI MANHATTAN_CHASE = new GhostChaseAI();

    private GhostAIRegistry() {
    }
}
