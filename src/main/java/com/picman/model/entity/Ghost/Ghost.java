package com.picman.model.entity.Ghost;

import com.picman.model.Maze;
import com.picman.model.entity.GhostMode;
import com.picman.model.entity.GridPosition;
import com.picman.model.entity.Pacman;

import java.awt.Color;
import java.util.List;

public interface Ghost {
    void enterHouse();

    void reset();

    void releaseFromHouse();

    Color getColor();

    Color getDisplayColor();

    GridPosition getPosition();

    int getCol();

    int getRow();

    GhostMode getMode();

    boolean isActiveForCollision();

    boolean isEdibleByPacman();

    boolean isPendingRespawn();

    void enterFrightened();

    void exitFrightened();

    void beEaten();

    void update(Maze maze, Pacman pacman, List<Ghost> allGhosts);
}
