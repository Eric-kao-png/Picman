package com.picman.model.entity.ghost;

import com.picman.config.GhostHouseConfig;
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

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

final class GhostNavigator {
    private GhostNavigator() {
    }

    static void ensureOnWalkableTile(
            Maze maze,
            GridPosition position,
            int spawnCol,
            int spawnRow,
            GhostMode mode,
            Supplier<Direction> getDirection,
            Consumer<Direction> setDirection) {
        if (maze.isWalkable(position.getCol(), position.getRow())) {
            return;
        }
        position.snapToCell(spawnCol, spawnRow);
        setDirection.accept(mode == GhostMode.LEAVING ? Direction.DOWN : null);
    }

    static void updateLeaving(
            Maze maze,
            GridPosition position,
            Supplier<Direction> getDirection,
            Consumer<Direction> setDirection,
            Consumer<GhostMode> setMode) {
        setDirection.accept(Direction.DOWN);
        if (GhostMover.advance(maze, position, getDirection.get()) && position.getRow() >= GhostHouseConfig.EXIT_ROW) {
            setMode.accept(GhostMode.ACTIVE);
            setDirection.accept(Direction.DOWN);
        }
    }

    static void updateChase(
            Maze maze,
            Pacman pacman,
            List<Ghost> allGhosts,
            GridPosition position,
            GhostMode mode,
            Supplier<Direction> getDirection,
            Consumer<Direction> setDirection,
            GhostAI chaseAi,
            GhostAI frightenedAi) {
        replanAtIntersection(maze, pacman, allGhosts, position, mode, getDirection, setDirection, chaseAi, frightenedAi);
        if (getDirection.get() != null) {
            GhostMover.advance(maze, position, getDirection.get());
        }
    }

    private static void replanAtIntersection(
            Maze maze,
            Pacman pacman,
            List<Ghost> allGhosts,
            GridPosition position,
            GhostMode mode,
            Supplier<Direction> getDirection,
            Consumer<Direction> setDirection,
            GhostAI chaseAi,
            GhostAI frightenedAi) {
        if (!GridMath.isAtCellCenter(position.getCenterX(), position.getCenterY())) {
            return;
        }

        int col = position.getCol();
        int row = position.getRow();
        if (!TurnPlanner.needsDirectionChoice(maze, col, row, getDirection.get())) {
            return;
        }

        GhostAI activeAi = mode == GhostMode.FRIGHTENED ? frightenedAi : chaseAi;
        GhostAIContext context = GhostAIContext.of(pacman, allGhosts);
        Direction next = activeAi.chooseDirection(maze, col, row, getDirection.get(), context);
        if (next == null) {
            GhostMover.snapToCell(position);
            return;
        }
        setDirection.accept(next);
    }
}
