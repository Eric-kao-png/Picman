package com.picman.model.entity.Ghost;

import com.picman.config.GhostHouseConfig;
import com.picman.config.GhostSpawn;
import com.picman.model.Maze;
import com.picman.model.ai.GhostAI;
import com.picman.model.ai.GhostAIContext;
import com.picman.model.entity.GhostMode;
import com.picman.model.entity.GridPosition;
import com.picman.model.entity.Pacman;
import com.picman.movement.GhostMover;
import com.picman.movement.GridMath;
import com.picman.movement.TurnPlanner;
import com.picman.util.Direction;

import java.awt.Color;
import java.util.List;

public class Red_Ghost implements Ghost {
    private final int spawnCol;
    private final int spawnRow;
    private final Direction initialDirection;
    private final Color color;
    private final GridPosition position;
    private final GhostAI ai;

    private GhostMode mode = GhostMode.WAITING;
    private Direction direction;

    public Red_Ghost(GhostSpawn spawn) {
        this.spawnCol = spawn.col();
        this.spawnRow = spawn.row();
        this.initialDirection = spawn.initialDirection();
        this.color = spawn.color();
        this.ai = spawn.ai();
        this.position = new GridPosition(spawnCol, spawnRow);
        this.direction = initialDirection;
    }

    @Override
    public void enterHouse() {
        position.snapToCell(spawnCol, spawnRow);
        direction = null;
        mode = GhostMode.WAITING;
    }

    @Override
    public void reset() {
        enterHouse();
    }

    @Override
    public void releaseFromHouse() {
        if (mode == GhostMode.WAITING) {
            mode = GhostMode.LEAVING;
            direction = Direction.DOWN;
        }
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public GridPosition getPosition() {
        return position;
    }

    @Override
    public int getCol() {
        return position.getCol();
    }

    @Override
    public int getRow() {
        return position.getRow();
    }

    @Override
    public boolean isActiveForCollision() {
        return mode != GhostMode.WAITING;
    }

    @Override
    public void update(Maze maze, Pacman pacman, List<Ghost> allGhosts) {
        ensureOnWalkableTile(maze);

        switch (mode) {
            case WAITING -> {
            }
            case LEAVING -> updateLeaving(maze);
            case ACTIVE -> updateActive(maze, pacman, allGhosts);
        }
    }

    private void updateLeaving(Maze maze) {
        direction = Direction.DOWN;
        if (GhostMover.advance(maze, position, direction) && hasExitedHouse()) {
            mode = GhostMode.ACTIVE;
            direction = Direction.DOWN;
        }
    }

    private void updateActive(Maze maze, Pacman pacman, List<Ghost> allGhosts) {
        replanDirectionAtCenter(maze, pacman, allGhosts);
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

    private void replanDirectionAtCenter(Maze maze, Pacman pacman, List<Ghost> allGhosts) {
        if (!GridMath.isAtCellCenter(position.getCenterX(), position.getCenterY())) {
            return;
        }

        int col = getCol();
        int row = getRow();
        if (!TurnPlanner.needsDirectionChoice(maze, col, row, direction)) {
            return;
        }

        GhostAIContext context = GhostAIContext.of(pacman, allGhosts);
        direction = ai.chooseDirection(maze, col, row, direction, context);
        if (direction == null) {
            GhostMover.snapToCell(position);
        }
    }
}
