package com.picman.model.ai;

import com.picman.model.Maze;
import com.picman.movement.GridMath;
import com.picman.movement.MazePathfinder;
import com.picman.movement.TurnPlanner;
import com.picman.util.Direction;

import java.util.ArrayList;
import java.util.List;

/**
 * 橘色幽靈（Clyde）：與玩家曼哈頓距離大於 {@link #CHASE_DISTANCE_THRESHOLD} 時如紅鬼追擊；
 * 否則沿最遠離玩家的路徑逃離，並偏向左下／右下。
 */
public class OrangePathfindingGhostAI implements GhostAI {
    public static final int CHASE_DISTANCE_THRESHOLD = 10;

    private final PathfindingChaseGhostAI chaseAi = new PathfindingChaseGhostAI();

    @Override
    public Direction chooseDirection(
            Maze maze,
            int col,
            int row,
            Direction currentDirection,
            GhostAIContext context) {
        int pacCol = context.pacman().getCol();
        int pacRow = context.pacman().getRow();
        if (GridMath.manhattan(col, row, pacCol, pacRow) > CHASE_DISTANCE_THRESHOLD) {
            return chaseAi.chooseDirection(maze, col, row, currentDirection, context);
        }
        return chooseFleeDirection(maze, col, row, currentDirection, pacCol, pacRow);
    }

    private Direction chooseFleeDirection(
            Maze maze,
            int col,
            int row,
            Direction currentDirection,
            int pacCol,
            int pacRow) {
        List<Direction> options = maze.getValidDirections(col, row, currentDirection, true);
        if (options.isEmpty()) {
            return null;
        }
        if (options.size() == 1) {
            return options.get(0);
        }
        if (TurnPlanner.canKeepCurrentDirection(maze, col, row, currentDirection, options)) {
            return currentDirection;
        }

        int[][] distancesFromPacman = MazePathfinder.distancesFrom(maze, pacCol, pacRow);
        List<Direction> bestOptions = new ArrayList<>();
        int bestSteps = Integer.MIN_VALUE;
        int bestCornerBias = Integer.MIN_VALUE;

        for (Direction candidate : options) {
            int nextCol = col + candidate.dx;
            int nextRow = row + candidate.dy;
            int steps = MazePathfinder.stepsTo(distancesFromPacman, nextCol, nextRow);
            if (steps == Integer.MAX_VALUE) {
                continue;
            }
            int cornerBias = ClydeFleeBias.score(maze, candidate, nextCol, nextRow);

            if (steps > bestSteps || (steps == bestSteps && cornerBias > bestCornerBias)) {
                bestSteps = steps;
                bestCornerBias = cornerBias;
                bestOptions.clear();
                bestOptions.add(candidate);
            } else if (steps == bestSteps && cornerBias == bestCornerBias) {
                bestOptions.add(candidate);
            }
        }

        if (bestOptions.isEmpty()) {
            return null;
        }
        if (currentDirection != null && bestOptions.contains(currentDirection)) {
            return currentDirection;
        }
        return bestOptions.get(0);
    }
}
