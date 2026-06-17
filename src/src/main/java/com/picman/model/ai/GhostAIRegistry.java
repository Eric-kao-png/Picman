package com.picman.model.ai;

/**
 * 幽靈 AI 策略實例（可共用、無狀態）。
 */
public final class GhostAIRegistry {
    public static final GhostAI PATHFINDING_CHASE = new PathfindingGhostAI(
            (maze, context) -> ChaseTargetCalculators.pacmanCell(context.pacman()));
    public static final GhostAI PINK_AMBUSH_PATHFINDING = new PathfindingGhostAI(
            (maze, context) -> ChaseTargetCalculators.pinkAmbush(maze, context.pacman()));
    public static final GhostAI CYAN_INKY_PATHFINDING = new PathfindingGhostAI(ChaseTargetCalculators::inkyVector);
    public static final GhostAI ORANGE_CLYDE_PATHFINDING = new OrangePathfindingGhostAI(
            PATHFINDING_CHASE,
            new FleeDirectionSelector());
    public static final GhostAI FRIGHTENED_FLEE = new FleeDirectionSelector();

    private GhostAIRegistry() {
    }
}
