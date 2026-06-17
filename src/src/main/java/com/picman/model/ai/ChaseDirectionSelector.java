package com.picman.model.ai;

import com.picman.model.Maze;
import com.picman.util.Direction;

/**
 * 岔路時依「到目標的距離」挑方向；子類提供距離評估器。
 */
abstract class ChaseDirectionSelector implements GhostAI {
    @Override
    public Direction chooseDirection(
            Maze maze,
            int col,
            int row,
            Direction currentDirection,
            GhostAIContext context) {
        IntersectionResolver.Result resolved = IntersectionResolver.resolve(maze, col, row, currentDirection);
        if (!resolved.needsRanking()) {
            return resolved.direction();
        }
        return DirectionRanker.selectMinimum(
                col,
                row,
                resolved.options(),
                (nextCol, nextRow) -> createEvaluator(maze, context).steps(nextCol, nextRow),
                currentDirection);
    }

    protected abstract DistanceEvaluator createEvaluator(Maze maze, GhostAIContext context);

    @FunctionalInterface
    protected interface DistanceEvaluator {
        int steps(int fromCol, int fromRow);
    }
}
