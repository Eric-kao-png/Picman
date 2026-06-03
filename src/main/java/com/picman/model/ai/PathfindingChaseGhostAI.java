package com.picman.model.ai;

import com.picman.model.Maze;
import com.picman.movement.GridMath;

/**
 * 紅色幽靈（Blinky）：以 BFS 最短路徑追擊玩家所在格子。
 */
public class PathfindingChaseGhostAI extends ChaseDirectionSelector {
    @Override
    protected DistanceEvaluator createEvaluator(Maze maze, GhostAIContext context) {
        int targetCol = GridMath.cellIndex(context.pacman().getPosition().getCenterX());
        int targetRow = GridMath.cellIndex(context.pacman().getPosition().getCenterY());
        return PathfindingEvaluatorFactory.toCell(maze, targetCol, targetRow);
    }
}
