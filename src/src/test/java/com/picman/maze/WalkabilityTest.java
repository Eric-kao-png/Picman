package com.picman.maze;

import com.picman.model.Maze;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WalkabilityTest {
    private static final int EMPTY_COL = 1;
    private static final int EMPTY_ROW = 1;
    private static final int BREAKABLE_WALL_COL = 2;
    private static final int BREAKABLE_WALL_ROW = 2;
    private static final int BORDER_WALL_COL = 0;
    private static final int BORDER_WALL_ROW = 5;
    private static final int PROTECTED_WALL_COL = 11;
    private static final int PROTECTED_WALL_ROW = 12;
    private static final int TUNNEL_COL = 0;
    private static final int TUNNEL_ROW = 16;

    private Maze maze;

    @BeforeEach
    void setUp() {
        maze = new Maze();
    }

    @Test
    void emptyCell_isOccupiableForPacmanAndGhost() {
        assertTrue(Walkability.isOccupiableForPacman(maze, EMPTY_COL, EMPTY_ROW));
        assertTrue(Walkability.isOccupiableForGhost(maze, EMPTY_COL, EMPTY_ROW));
    }

    @Test
    void solidWall_isNotOccupiableForPacman() {
        assertFalse(Walkability.isOccupiableForPacman(maze, BREAKABLE_WALL_COL, BREAKABLE_WALL_ROW));
    }

    @Test
    void solidWall_isNotOccupiableForGhost() {
        assertFalse(Walkability.isOccupiableForGhost(maze, BREAKABLE_WALL_COL, BREAKABLE_WALL_ROW));
    }

    @Test
    void brokenWall_isOccupiableForPacman_butNotForGhost() {
        maze.breakWall(BREAKABLE_WALL_COL, BREAKABLE_WALL_ROW);

        assertTrue(Walkability.isOccupiableForPacman(maze, BREAKABLE_WALL_COL, BREAKABLE_WALL_ROW));
        assertFalse(Walkability.isOccupiableForGhost(maze, BREAKABLE_WALL_COL, BREAKABLE_WALL_ROW));
    }

    @Test
    void canEnter_breakableWall_whenPickaxeActive() {
        assertTrue(Walkability.canEnter(maze, BREAKABLE_WALL_COL, BREAKABLE_WALL_ROW, true));
    }

    @Test
    void canEnter_breakableWall_false_whenPickaxeInactive() {
        assertFalse(Walkability.canEnter(maze, BREAKABLE_WALL_COL, BREAKABLE_WALL_ROW, false));
    }

    @Test
    void canEnter_borderWall_false_evenWithPickaxe() {
        assertFalse(maze.canBreakWall(BORDER_WALL_COL, BORDER_WALL_ROW));
        assertFalse(Walkability.canEnter(maze, BORDER_WALL_COL, BORDER_WALL_ROW, true));
    }

    @Test
    void canEnter_ghostHouseWall_false_evenWithPickaxe() {
        assertFalse(maze.canBreakWall(PROTECTED_WALL_COL, PROTECTED_WALL_ROW));
        assertFalse(Walkability.canEnter(maze, PROTECTED_WALL_COL, PROTECTED_WALL_ROW, true));
    }

    @Test
    void canEnter_tunnelEdge_isWalkableWithoutPickaxe() {
        assertTrue(Walkability.canEnter(maze, TUNNEL_COL, TUNNEL_ROW, false));
        assertTrue(Walkability.isOccupiableForGhost(maze, TUNNEL_COL, TUNNEL_ROW));
    }

    @Test
    void outOfBounds_isNotOccupiableOrEnterable() {
        assertFalse(Walkability.isOccupiableForPacman(maze, -1, 0));
        assertFalse(Walkability.isOccupiableForGhost(maze, maze.getWidth(), 0));
        assertFalse(Walkability.canEnter(maze, 0, maze.getHeight(), true));
    }
}
