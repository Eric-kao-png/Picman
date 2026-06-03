package com.picman.model.ai;

import com.picman.model.Maze;
import com.picman.model.entity.GridPosition;
import com.picman.movement.GridMath;

/** 以曼哈頓直線距離追擊（暫供非紅色幽靈使用）。 */
public class GhostChaseAI extends ChaseDirectionSelector {
    @Override
    protected int distanceToTarget(Maze maze, int fromCol, int fromRow, GridPosition target) {
        int targetCol = GridMath.cellIndex(target.getCenterX());
        int targetRow = GridMath.cellIndex(target.getCenterY());
        return GridMath.manhattan(fromCol, fromRow, targetCol, targetRow);
    }
}
