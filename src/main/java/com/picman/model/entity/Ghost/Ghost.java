package com.picman.model.entity.Ghost;

import com.picman.config.GhostSpawn;
import com.picman.model.Maze;
import com.picman.model.entity.GhostMode;
import com.picman.model.entity.GridPosition;
import com.picman.model.entity.Pacman;
import com.picman.util.Direction;

import java.awt.Color;
import java.util.List;

public interface Ghost {
    void enterHouse();

    void reset();

    void releaseFromHouse();

    Color getColor();

    GridPosition getPosition();

    int getCol();

    int getRow();

    boolean isActiveForCollision();

    void update(Maze maze, Pacman pacman, List<Ghost> allGhosts);
    
}
