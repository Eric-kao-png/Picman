package com.picman.model.ai;

import com.picman.config.GhostAIConfig;
import com.picman.model.Maze;
import com.picman.movement.GridMath;
import com.picman.util.Direction;

/**
 * 橘色幽靈（Clyde）：距離大於門檻時如紅鬼追擊，否則逃離並偏向左下／右下。
 */
public class OrangePathfindingGhostAI implements GhostAI {
    private final GhostAI chaseAi;
    private final GhostAI fleeAi;

    public OrangePathfindingGhostAI(GhostAI chaseAi, GhostAI fleeAi) {
        this.chaseAi = chaseAi;
        this.fleeAi = fleeAi;
    }

    @Override
    public Direction chooseDirection(
            Maze maze,
            int col,
            int row,
            Direction currentDirection,
            GhostAIContext context) {
        int pacCol = context.pacman().getCol();
        int pacRow = context.pacman().getRow();
        if (GridMath.manhattan(col, row, pacCol, pacRow) > GhostAIConfig.CLYDE_CHASE_DISTANCE_THRESHOLD) {
            return chaseAi.chooseDirection(maze, col, row, currentDirection, context);
        }
        return fleeAi.chooseDirection(maze, col, row, currentDirection, context);
    }
}
