package com.picman.model.ai;

import com.picman.model.Maze;

/**
 * 粉色幽靈（Pinky）：追擊玩家朝向前 4 格的位置，以 BFS 最短路徑接近。
 */
public class PinkPathfindingGhostAI extends ChaseDirectionSelector {
    @Override
    protected DistanceEvaluator createEvaluator(Maze maze, GhostAIContext context) {
        int[] ambushCell = AmbushTargetCalculator.cellAhead(
                maze,
                context.pacman(),
                AmbushTargetCalculator.PINK_LOOKAHEAD_TILES);
        return PathfindingEvaluatorFactory.toCell(maze, ambushCell[0], ambushCell[1]);
    }
}
