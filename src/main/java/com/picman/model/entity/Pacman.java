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

    public void reset() {
        position.snapToCell(GameConfig.PACMAN_START_COL, GameConfig.PACMAN_START_ROW);
        direction = null;
        activeDirection = null;
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
     * 目前移動方向；靜止時改為玩家最後按下的方向（對應經典 Pinky 伏擊判定）。
     */
    public Direction getFacingDirection() {
        if (direction != null) {
            return direction;
        }
        return activeDirection;
    }

    public void setActiveDirection(Direction direction) {
        activeDirection = direction;
        if (direction == null) {
            this.direction = null;
        }
    }

    public void update(Maze maze) {
        if (activeDirection == null) {
            direction = null;
            return;
        }

        applyTurnIfPossible(maze);
        if (direction == null) {
            return;
        }

        GridMovement.movePacman(maze, position, direction);
    }

    private void applyTurnIfPossible(Maze maze) {
        if (GridMovement.canTurn(position, direction, activeDirection, maze)) {
            direction = activeDirection;
            return;
        }
        if (direction == null && GridMovement.canTurn(position, null, activeDirection, maze)) {
            direction = activeDirection;
        }
    }
}
