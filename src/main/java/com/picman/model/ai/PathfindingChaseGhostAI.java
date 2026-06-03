package com.picman.model.ai;

import com.picman.model.Maze;
import com.picman.model.entity.GridPosition;
import com.picman.movement.GridMath;
import com.picman.movement.MazePathfinder;

/**
 * 紅色幽靈（Blinky）：在可走格子上以 BFS 計算到玩家的最短路徑步數，並選擇縮短該距離的方向。
 */
public class PathfindingChaseGhostAI extends ChaseDirectionSelector {
    @Override
    protected DistanceEvaluator createEvaluator(Maze maze, GridPosition target) {
        int targetCol = GridMath.cellIndex(target.getCenterX());
        int targetRow = GridMath.cellIndex(target.getCenterY());
        int[][] distances = MazePathfinder.distancesFrom(maze, targetCol, targetRow);
        return (fromCol, fromRow) -> MazePathfinder.stepsTo(distances, fromCol, fromRow);
    }
}
