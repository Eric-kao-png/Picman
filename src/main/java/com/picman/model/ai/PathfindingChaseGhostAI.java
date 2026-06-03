package com.picman.model.ai;

import com.picman.model.Maze;
import com.picman.model.entity.Pacman;
import com.picman.movement.GridMath;

/**
 * 紅色幽靈（Blinky）：以 BFS 最短路徑追擊玩家所在格子。
 */
public class PathfindingChaseGhostAI extends ChaseDirectionSelector {
    @Override
    protected DistanceEvaluator createEvaluator(Maze maze, Pacman pacman) {
        int targetCol = GridMath.cellIndex(pacman.getPosition().getCenterX());
        int targetRow = GridMath.cellIndex(pacman.getPosition().getCenterY());
        return PathfindingEvaluatorFactory.toCell(maze, targetCol, targetRow);
    }
}
