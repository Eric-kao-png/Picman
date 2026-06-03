package com.picman.movement;

import com.picman.model.Maze;
import com.picman.model.entity.GridPosition;
import com.picman.util.Direction;

/**
 * 幽靈沿單一方向的移動與停格。
 */
public final class GhostMover {
    private GhostMover() {
    }

    public static boolean advance(Maze maze, GridPosition position, Direction direction) {
        if (direction == null) {
            return false;
        }
        int col = position.getCol();
        int row = position.getRow();
        if (!GridMovement.canAdvance(maze, col, row, direction)) {
            snapToCell(position);
            return false;
        }
        if (!GridMovement.moveGhost(maze, position, direction)) {
            snapToCell(position);
            return false;
        }
        return true;
    }

    public static void snapToCell(GridPosition position) {
        position.snapToCell(position.getCol(), position.getRow());
    }
}
