package com.picman.model.ai;

import com.picman.model.Maze;
import com.picman.movement.MazePathfinder;
import com.picman.util.Direction;

/**
 * 逃離玩家：最大化 BFS 距離，並以 {@link ClydeFleeBias} 打破平手。
 */
final class FleeDirectionSelector implements GhostAI {
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

        int pacCol = context.pacman().getCol();
        int pacRow = context.pacman().getRow();
        int[][] distancesFromPacman = MazePathfinder.distancesFrom(maze, pacCol, pacRow);

        return DirectionRanker.selectMaximumByDirection(
                col,
                row,
                resolved.options(),
                candidate -> fleeScore(maze, col, row, candidate, distancesFromPacman),
                currentDirection);
    }

    private static int fleeScore(
            Maze maze,
            int col,
            int row,
            Direction candidate,
            int[][] distancesFromPacman) {
        int nextCol = col + candidate.dx;
        int nextRow = row + candidate.dy;
        int steps = MazePathfinder.stepsTo(distancesFromPacman, nextCol, nextRow);
        if (steps == Integer.MAX_VALUE) {
            return Integer.MIN_VALUE;
        }
        return steps * 1_000 + ClydeFleeBias.score(maze, candidate, nextCol, nextRow);
    }
}
