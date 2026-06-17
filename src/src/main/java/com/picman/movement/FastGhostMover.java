package com.picman.movement;

import com.picman.model.Maze;
import com.picman.model.entity.GridPosition;
import com.picman.util.Direction;

/**
 * 支持自定義速度的幽靈移動器。
 * 用於需要不同移動速度的特殊幽靈（例如快速幽靈）。
 */
public final class FastGhostMover {
    private FastGhostMover() {
    }

    /**
     * 以指定速度推進幽靈。
     * @param maze 迷宮
     * @param position 位置
     * @param direction 移動方向
     * @param speed 移動速度
     * @return 是否成功移動
     */
    public static boolean advance(Maze maze, GridPosition position, Direction direction, double speed) {
        if (direction == null) {
            return false;
        }
        int col = position.getCol();
        int row = position.getRow();
        if (!GridMovement.canAdvance(maze, col, row, direction)) {
            snapToCell(position);
            return false;
        }
        if (!GridMovement.moveGhostWithSpeed(maze, position, direction, speed)) {
            snapToCell(position);
            return false;
        }
        return true;
    }

    public static void snapToCell(GridPosition position) {
        position.snapToCell(position.getCol(), position.getRow());
    }
}
