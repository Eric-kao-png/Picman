package com.picman.model.entity.ghost;

import com.picman.config.GhostSpawn;
import com.picman.config.RenderTheme;
import com.picman.model.Maze;
import com.picman.model.ai.GhostAI;
import com.picman.model.ai.GhostAIRegistry;
import com.picman.model.entity.GhostMode;
import com.picman.model.entity.GridPosition;
import com.picman.model.entity.Pacman;
import com.picman.util.Direction;

import java.awt.Color;
import java.util.List;

abstract class AbstractGhost implements Ghost {
    private final int spawnCol;
    private final int spawnRow;
    private final Color color;
    private final GridPosition position;
    private final GhostAI ai;
    private final GhostAI frightenedAi = GhostAIRegistry.FRIGHTENED_FLEE;

    private GhostMode mode = GhostMode.WAITING;
    private Direction direction;
    private boolean pendingRespawn;

    protected AbstractGhost(GhostSpawn spawn) {
        this.spawnCol = spawn.col();
        this.spawnRow = spawn.row();
        this.color = spawn.color();
        this.ai = spawn.ai();
        this.position = new GridPosition(spawnCol, spawnRow);
        this.direction = spawn.initialDirection();
    }

    @Override
    public void enterHouse() {
        position.snapToCell(spawnCol, spawnRow);
        direction = null;
        mode = GhostMode.WAITING;
    }

    @Override
    public void reset() {
        pendingRespawn = false;
        enterHouse();
    }

    @Override
    public void releaseFromHouse() {
        if (mode == GhostMode.WAITING) {
            mode = GhostMode.LEAVING;
            direction = Direction.DOWN;
            pendingRespawn = false;
        }
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public Color getDisplayColor() {
        return mode == GhostMode.FRIGHTENED ? RenderTheme.GHOST_FRIGHTENED : color;
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
    public GhostMode getMode() {
        return mode;
    }

    @Override
    public boolean isActiveForCollision() {
        return mode == GhostMode.ACTIVE || mode == GhostMode.FRIGHTENED || mode == GhostMode.LEAVING;
    }

    @Override
    public boolean isEdibleByPacman() {
        return mode == GhostMode.FRIGHTENED;
    }

    @Override
    public boolean isPendingRespawn() {
        return pendingRespawn;
    }

    @Override
    public void enterFrightened() {
        if (mode == GhostMode.ACTIVE || mode == GhostMode.LEAVING) {
            mode = GhostMode.FRIGHTENED;
        }
    }

    @Override
    public void exitFrightened() {
        if (mode == GhostMode.FRIGHTENED) {
            mode = GhostMode.ACTIVE;
        }
    }

    @Override
    public void beEaten() {
        pendingRespawn = true;
        enterHouse();
    }

    @Override
    public void update(Maze maze, Pacman pacman, List<Ghost> allGhosts) {
        GhostNavigator.ensureOnWalkableTile(
                maze, position, spawnCol, spawnRow, mode, this::getDirection, this::setDirection);

        switch (mode) {
            case WAITING -> {
            }
            case LEAVING -> GhostNavigator.updateLeaving(
                    maze, position, this::getDirection, this::setDirection, next -> mode = next);
            case ACTIVE, FRIGHTENED -> GhostNavigator.updateChase(
                    maze, pacman, allGhosts, position, mode,
                    this::getDirection, this::setDirection, ai, frightenedAi);
        }
    }

    private Direction getDirection() {
        return direction;
    }

    private void setDirection(Direction next) {
        direction = next;
    }
}
