package com.picman.maze;

import com.picman.model.CellType;
import com.picman.model.Maze;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BreakableWallRulesTest {
    private static final int BREAKABLE_WALL_COL = 2;
    private static final int BREAKABLE_WALL_ROW = 2;
    private static final int BORDER_WALL_COL = 0;
    private static final int BORDER_WALL_ROW = 5;
    private static final int PROTECTED_WALL_COL = 11;
    private static final int PROTECTED_WALL_ROW = 12;
    private static final int EMPTY_COL = 1;
    private static final int EMPTY_ROW = 1;
    private static final int GHOST_HOUSE_INTERIOR_COL = 14;
    private static final int GHOST_HOUSE_INTERIOR_ROW = 14;

    private Maze maze;

    @BeforeEach
    void setUp() {
        maze = new Maze();
    }

    @Test
    void innerWall_canBreak() {
        assertEquals(CellType.WALL, maze.getCellType(BREAKABLE_WALL_COL, BREAKABLE_WALL_ROW));
        assertTrue(BreakableWallRules.canBreak(maze, BREAKABLE_WALL_COL, BREAKABLE_WALL_ROW));
    }

    @Test
    void outerBorderWall_cannotBreak() {
        assertFalse(BreakableWallRules.canBreak(maze, BORDER_WALL_COL, BORDER_WALL_ROW));
        assertFalse(BreakableWallRules.canBreak(
                maze, maze.getWidth() - 1, BORDER_WALL_ROW));
        assertFalse(BreakableWallRules.canBreak(
                maze, BORDER_WALL_COL, maze.getHeight() - 1));
    }

    @Test
    void ghostHouseWall_cannotBreak() {
        assertFalse(BreakableWallRules.canBreak(maze, PROTECTED_WALL_COL, PROTECTED_WALL_ROW));
    }

    @Test
    void nonWallCell_cannotBreak() {
        assertEquals(CellType.COIN, maze.getCellType(EMPTY_COL, EMPTY_ROW));
        assertFalse(BreakableWallRules.canBreak(maze, EMPTY_COL, EMPTY_ROW));
    }

    @Test
    void ghostHouseInterior_cannotBreak() {
        assertEquals(CellType.EMPTY, maze.getCellType(GHOST_HOUSE_INTERIOR_COL, GHOST_HOUSE_INTERIOR_ROW));
        assertFalse(BreakableWallRules.canBreak(
                maze, GHOST_HOUSE_INTERIOR_COL, GHOST_HOUSE_INTERIOR_ROW));
    }

    @Test
    void outOfBounds_cannotBreak() {
        assertFalse(BreakableWallRules.canBreak(maze, -1, 0));
        assertFalse(BreakableWallRules.canBreak(maze, maze.getWidth(), 0));
        assertFalse(BreakableWallRules.canBreak(maze, 0, maze.getHeight()));
    }

    @Test
    void mazeCanBreakWall_delegatesToRules() {
        assertTrue(maze.canBreakWall(BREAKABLE_WALL_COL, BREAKABLE_WALL_ROW));
        assertFalse(maze.canBreakWall(BORDER_WALL_COL, BORDER_WALL_ROW));
        assertFalse(maze.canBreakWall(PROTECTED_WALL_COL, PROTECTED_WALL_ROW));
    }
}
