package com.picman.model.ai;

import com.picman.model.Maze;
import com.picman.movement.GridMath;

/** 以曼哈頓直線距離追擊（暫供橘色幽靈使用）。 */
public class GhostChaseAI extends ChaseDirectionSelector {
    @Override
    protected DistanceEvaluator createEvaluator(Maze maze, GhostAIContext context) {
        int targetCol = GridMath.cellIndex(context.pacman().getPosition().getCenterX());
        int targetRow = GridMath.cellIndex(context.pacman().getPosition().getCenterY());
        return (fromCol, fromRow) -> GridMath.manhattan(fromCol, fromRow, targetCol, targetRow);
    }
}
