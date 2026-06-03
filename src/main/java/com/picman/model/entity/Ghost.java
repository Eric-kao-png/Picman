package com.picman.model.entity;

import com.picman.config.GhostHouseConfig;
import com.picman.config.GhostSpawn;
import com.picman.model.Maze;
import com.picman.model.ai.GhostChaseAI;
import com.picman.movement.GridMath;
import com.picman.movement.GridMovement;
import com.picman.util.Direction;

import java.awt.Color;

public class Ghost {
    private final int spawnCol;
    private final int spawnRow;
    private final Direction initialDirection;
    private final Color color;
    private final GridPosition position;
    private final GhostChaseAI ai = new GhostChaseAI();

    private GhostMode mode = GhostMode.WAITING;
    private Direction direction;

    public Ghost(GhostSpawn spawn) {
        this.spawnCol = spawn.col();
        this.spawnRow = spawn.row();
        this.initialDirection = spawn.initialDirection();
        this.color = spawn.color();
        this.position = new GridPosition(spawnCol, spawnRow);
        this.direction = initialDirection;
    }

    public void enterHouse() {
        position.snapToCell(spawnCol, spawnRow);
        direction = null;
        mode = GhostMode.WAITING;
    }

    public void reset() {
        enterHouse();
    }

    public void releaseFromHouse() {
        if (mode == GhostMode.WAITING) {
            mode = GhostMode.LEAVING;
            direction = Direction.DOWN;
        }
    }

    public Color getColor() {
        return color;
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

    public void update(Maze maze, Pacman pacman) {
        ensureOnWalkableTile(maze);

        switch (mode) {
            case WAITING -> {
            }
            case LEAVING -> updateLeaving(maze);
            case ACTIVE -> updateActive(maze, pacman);
        }
    }

    public boolean collidesWith(Pacman pacman) {
        if (mode == GhostMode.WAITING) {
            return false;
        }
        return com.picman.collision.CollisionDetector.entitiesOverlap(position, pacman.getPosition());
    }

    private void updateLeaving(Maze maze) {
        direction = Direction.DOWN;
        if (!GridMovement.canAdvance(maze, getCol(), getRow(), direction)) {
            stopAtCurrentCell();
            return;
        }
        if (!GridMovement.moveGhost(maze, position, direction)) {
            stopAtCurrentCell();
            return;
        }
        if (hasExitedHouse()) {
            mode = GhostMode.ACTIVE;
            direction = Direction.DOWN;
        }
    }

    private void updateActive(Maze maze, Pacman pacman) {
        planDirectionAtCenter(maze, pacman);

        if (direction == null) {
            return;
        }
        if (!GridMovement.canAdvance(maze, getCol(), getRow(), direction)) {
            stopAtCurrentCell();
            return;
        }
        if (!GridMovement.moveGhost(maze, position, direction)) {
            stopAtCurrentCell();
        }
    }

    private boolean hasExitedHouse() {
        return getRow() >= GhostHouseConfig.EXIT_ROW;
    }

    private void ensureOnWalkableTile(Maze maze) {
        if (maze.isWalkable(getCol(), getRow())) {
            return;
        }
        position.snapToCell(spawnCol, spawnRow);
        direction = mode == GhostMode.LEAVING ? Direction.DOWN : null;
    }

    private void planDirectionAtCenter(Maze maze, Pacman pacman) {
        if (!GridMath.isAtCellCenter(position.getCenterX(), position.getCenterY())) {
            return;
        }

        int col = getCol();
        int row = getRow();
        boolean blocked = direction != null && !GridMovement.canAdvance(maze, col, row, direction);
        if (!blocked && direction != null && !maze.isIntersection(col, row)) {
            return;
        }

        direction = ai.chooseDirection(maze, col, row, direction, pacman.getPosition());
        if (direction == null) {
            position.snapToCell(col, row);
        }
    }

    private void stopAtCurrentCell() {
        position.snapToCell(getCol(), getRow());
        direction = null;
    }
}
