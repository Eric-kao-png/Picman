package com.picman.model.entity;

import com.picman.config.GameConfig;
import com.picman.model.Maze;
import com.picman.movement.GridMovement;
import com.picman.util.Direction;

public class Pacman {
    private final GridPosition position = new GridPosition(
            GameConfig.PACMAN_START_COL,
            GameConfig.PACMAN_START_ROW);
    private Direction direction;
    private Direction activeDirection;
    /** 最後一次有效的移動朝向（供粉色幽靈伏擊點計算）。 */
    private Direction lastFacingDirection;

    public void reset() {
        position.snapToCell(GameConfig.PACMAN_START_COL, GameConfig.PACMAN_START_ROW);
        direction = null;
        activeDirection = null;
        lastFacingDirection = null;
    }

    public GridPosition getPosition() {
        return position;
    }

    public int getCol() {
        return position.getCol();
    }

    public int getRow() {
        return position.getRow();
    }

    /**
     * 記錄的朝向：優先目前移動方向，其次按鍵意圖，最後為曾經的面向。
     */
    public Direction getFacingDirection() {
        if (direction != null) {
            return direction;
        }
        if (activeDirection != null) {
            return activeDirection;
        }
        return lastFacingDirection;
    }

    public void setActiveDirection(Direction direction) {
        activeDirection = direction;
        if (direction != null) {
            recordFacing(direction);
        } else {
            this.direction = null;
        }
    }

    private void recordFacing(Direction facing) {
        lastFacingDirection = facing;
    }

    public void update(Maze maze, boolean canBreakWalls) {
        if (activeDirection == null) {
            direction = null;
            return;
        }

        applyTurnIfPossible(maze, canBreakWalls);
        if (direction == null) {
            return;
        }

        GridMovement.movePacman(maze, position, direction, canBreakWalls);
    }

    private void applyTurnIfPossible(Maze maze, boolean canBreakWalls) {
        if (GridMovement.canTurn(position, direction, activeDirection, maze, canBreakWalls)) {
            direction = activeDirection;
            recordFacing(direction);
            return;
        }
        if (direction == null && GridMovement.canTurn(position, null, activeDirection, maze, canBreakWalls)) {
            direction = activeDirection;
            recordFacing(direction);
        }
    }
}
