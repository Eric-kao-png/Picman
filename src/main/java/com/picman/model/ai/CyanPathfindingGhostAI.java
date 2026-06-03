package com.picman.model.ai;

import com.picman.model.Maze;
import com.picman.model.entity.Ghost;

/**
 * 青色幽靈（Inky）：追擊點為由紅鬼（B）指向玩家前方 1 格（A）的向量，從 B 再延伸兩倍。
 */
public class CyanPathfindingGhostAI extends ChaseDirectionSelector {
    @Override
    protected DistanceEvaluator createEvaluator(Maze maze, GhostAIContext context) {
        Ghost redGhost = context.redGhost();
        int[] chaseCell = InkyTargetCalculator.chaseCell(
                maze,
                context.pacman(),
                redGhost.getCol(),
                redGhost.getRow());
        return PathfindingEvaluatorFactory.toCell(maze, chaseCell[0], chaseCell[1]);
    }
}
