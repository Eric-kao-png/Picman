package com.picman.model.entity;

import com.picman.config.GhostHouseConfig;
import com.picman.config.GhostSpawn;
import com.picman.model.Maze;
import com.picman.model.ai.GhostAI;
import com.picman.movement.GhostMover;
import com.picman.movement.GridMath;
import com.picman.movement.TurnPlanner;
import com.picman.util.Direction;

import java.awt.Color;

public class Ghost {
    private final int spawnCol;
    private final int spawnRow;
    private final Direction initialDirection;
    private final Color color;
    private final GridPosition position;
    private final GhostAI ai;

    private GhostMode mode = GhostMode.WAITING;
    private Direction direction;

    public Ghost(GhostSpawn spawn) {
        this.spawnCol = spawn.col();
        this.spawnRow = spawn.row();
        this.initialDirection = spawn.initialDirection();
        this.color = spawn.color();
        this.ai = spawn.ai();
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

    public boolean isActiveForCollision() {
        return mode != GhostMode.WAITING;
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

    private void updateLeaving(Maze maze) {
        direction = Direction.DOWN;
        if (GhostMover.advance(maze, position, direction) && hasExitedHouse()) {
            mode = GhostMode.ACTIVE;
            direction = Direction.DOWN;
        }
    }

    private void updateActive(Maze maze, Pacman pacman) {
        replanDirectionAtCenter(maze, pacman);
        if (direction != null) {
            GhostMover.advance(maze, position, direction);
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

    private void replanDirectionAtCenter(Maze maze, Pacman pacman) {
        if (!GridMath.isAtCellCenter(position.getCenterX(), position.getCenterY())) {
            return;
        }

        int col = getCol();
        int row = getRow();
        if (!TurnPlanner.needsDirectionChoice(maze, col, row, direction)) {
            return;
        }

        direction = ai.chooseDirection(maze, col, row, direction, pacman);
        if (direction == null) {
            GhostMover.snapToCell(position);
        }
    }
}
