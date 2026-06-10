package com.picman.movement;

import com.picman.model.Maze;
import com.picman.model.MazeLayout;
import com.picman.model.entity.GridPosition;
import com.picman.util.Direction;

/**
 * row 16 左右死胡同的橫向穿牆通道：(0,16) ↔ (27,16)。
 */
public final class SideTunnel {
    public static final int ROW = 16;
    public static final int WEST_COL = 0;
    public static final int EAST_COL = MazeLayout.WIDTH - 1;

    private SideTunnel() {
    }

    public static boolean isOnTunnelRow(int row) {
        return row == ROW;
    }

    public static boolean isWrapEdge(int col, int row, Direction direction) {
        return isOnTunnelRow(row)
                && direction.isHorizontal()
                && ((col == WEST_COL && direction == Direction.LEFT)
                || (col == EAST_COL && direction == Direction.RIGHT));
    }

    /**
     * 解析一步之後的目標格；若為穿牆則回傳對側出口格。
     *
     * @return int[]{col, row}，無法前進則 null
     */
    public static int[] resolveTarget(Maze maze, int col, int row, Direction direction) {
        int nextCol = col + direction.dx;
        int nextRow = row + direction.dy;
        if (maze.isWalkable(nextCol, nextRow)) {
            return new int[]{nextCol, nextRow};
        }
        if (isWrapEdge(col, row, direction)) {
            int wrappedCol = direction == Direction.LEFT ? EAST_COL : WEST_COL;
            if (maze.isWalkable(wrappedCol, row)) {
                return new int[]{wrappedCol, row};
            }
        }
        return null;
    }

    public static boolean canStep(Maze maze, int col, int row, Direction direction) {
        return resolveTarget(maze, col, row, direction) != null;
    }

    /** 將超出左右邊界的像素座標折回地圖內（僅在隧道列生效）。 */
    public static void wrapPositionHorizontally(GridPosition position) {
        if (!isOnTunnelRow(position.getRow())) {
            return;
        }
        double width = MazeLayout.WIDTH * com.picman.config.GameConfig.TILE_SIZE;
        double x = position.getCenterX();
        if (x < 0) {
            position.setCenterX(x + width);
        } else if (x >= width) {
            position.setCenterX(x - width);
        }
    }
}
