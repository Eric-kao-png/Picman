package com.picman.model.ai;

import com.picman.model.Maze;
import com.picman.movement.MazePathfinder;

/**
 * 建立以 BFS 最短路徑步數為距離的評估器。
 */
final class PathfindingEvaluatorFactory {
    private PathfindingEvaluatorFactory() {
    }

    static ChaseDirectionSelector.DistanceEvaluator toCell(Maze maze, int targetCol, int targetRow) {
        int[][] distances = MazePathfinder.distancesToward(maze, targetCol, targetRow);
        return (fromCol, fromRow) -> MazePathfinder.stepsTo(distances, fromCol, fromRow);
    }
}
