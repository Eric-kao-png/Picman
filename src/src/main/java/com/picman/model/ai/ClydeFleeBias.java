package com.picman.model.ai;

import com.picman.model.Maze;
import com.picman.util.Direction;

/**
 * 逃離時偏好左下、右下方向（向下優先，其次往左右側靠近底角）。
 */
final class ClydeFleeBias {
    private static final int DOWN_BONUS = 100;
    private static final int HORIZONTAL_BONUS = 50;
    private static final int ROW_WEIGHT = 10;
    private static final int EDGE_WEIGHT = 5;

    private ClydeFleeBias() {
    }

    static int score(Maze maze, Direction direction, int nextCol, int nextRow) {
        int score = nextRow * ROW_WEIGHT;
        score += edgeDistance(nextCol, maze.getWidth()) * EDGE_WEIGHT;

        if (direction == Direction.DOWN) {
            score += DOWN_BONUS;
        } else if (direction.isHorizontal()) {
            score += HORIZONTAL_BONUS;
        }
        return score;
    }

    /** 越靠近左邊或右邊邊界分數越高（對應左下／右下兩角）。 */
    private static int edgeDistance(int col, int width) {
        return Math.max(col, width - 1 - col);
    }
}
