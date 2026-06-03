package com.picman.model.ai;

import com.picman.model.Maze;
import com.picman.model.entity.Pacman;
import com.picman.movement.GridMath;

/** 以曼哈頓直線距離追擊（暫供青/橘幽靈使用）。 */
public class GhostChaseAI extends ChaseDirectionSelector {
    @Override
    protected DistanceEvaluator createEvaluator(Maze maze, Pacman pacman) {
        int targetCol = GridMath.cellIndex(pacman.getPosition().getCenterX());
        int targetRow = GridMath.cellIndex(pacman.getPosition().getCenterY());
        return (fromCol, fromRow) -> GridMath.manhattan(fromCol, fromRow, targetCol, targetRow);
    }
}
