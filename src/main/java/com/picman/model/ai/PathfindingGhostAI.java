package com.picman.model.ai;

import com.picman.model.Maze;
import com.picman.util.Direction;

/**
 * 依 BFS 最短路徑步數，在岔路選擇最接近目標格的方向。
 */
@FunctionalInterface
interface ChaseTargetCell {
    GridCell resolve(Maze maze, GhostAIContext context);
}

final class PathfindingGhostAI extends ChaseDirectionSelector {
    private final ChaseTargetCell targetCell;

    PathfindingGhostAI(ChaseTargetCell targetCell) {
        this.targetCell = targetCell;
    }

    @Override
    protected DistanceEvaluator createEvaluator(Maze maze, GhostAIContext context) {
        GridCell target = targetCell.resolve(maze, context);
        return PathfindingEvaluatorFactory.toCell(maze, target.col(), target.row());
    }
}
