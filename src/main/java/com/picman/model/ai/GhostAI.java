package com.picman.model.ai;

import com.picman.model.Maze;
import com.picman.model.entity.GridPosition;
import com.picman.util.Direction;

public interface GhostAI {
    Direction chooseDirection(
            Maze maze,
            int col,
            int row,
            Direction currentDirection,
            GridPosition target);
}
