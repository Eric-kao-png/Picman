package com.picman.movement;

import com.picman.model.Maze;
import com.picman.util.Direction;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

public final class MazePathfinder {
    public static final int UNREACHABLE = -1;

    private MazePathfinder() {
    }

    /**
     * 從起點對所有可走格子做 BFS，回傳各格到起點的步數；不可達為 {@link #UNREACHABLE}。
     */
    public static int[][] distancesFrom(Maze maze, int startCol, int startRow) {
        int width = maze.getWidth();
        int height = maze.getHeight();
        int[][] distances = new int[height][width];
        for (int[] row : distances) {
            Arrays.fill(row, UNREACHABLE);
        }

        if (!inBounds(maze, startCol, startRow)) {
            return distances;
        }

        Queue<int[]> queue = new ArrayDeque<>();
        if (maze.isWalkable(startCol, startRow)) {
            distances[startRow][startCol] = 0;
            queue.add(new int[]{startCol, startRow});
        } else {
            seedWalkableNeighbors(maze, distances, queue, startCol, startRow, 1);
        }

        if (queue.isEmpty()) {
            return distances;
        }

        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int col = cell[0];
            int row = cell[1];
            int steps = distances[row][col];

            for (Direction direction : Direction.values()) {
                int[] target = SideTunnel.resolveTarget(maze, col, row, direction);
                if (target == null) {
                    continue;
                }
                int nextCol = target[0];
                int nextRow = target[1];
                if (!maze.isWalkable(nextCol, nextRow)) {
                    continue;
                }
                if (distances[nextRow][nextCol] != UNREACHABLE) {
                    continue;
                }
                distances[nextRow][nextCol] = steps + 1;
                queue.add(new int[]{nextCol, nextRow});
            }
        }
        return distances;
    }

    public static int stepsTo(int[][] distances, int col, int row) {
        if (row < 0 || row >= distances.length || col < 0 || col >= distances[0].length) {
            return Integer.MAX_VALUE;
        }
        int steps = distances[row][col];
        return steps == UNREACHABLE ? Integer.MAX_VALUE : steps;
    }

    private static boolean inBounds(Maze maze, int col, int row) {
        return col >= 0 && col < maze.getWidth() && row >= 0 && row < maze.getHeight();
    }

    private static void seedWalkableNeighbors(
            Maze maze,
            int[][] distances,
            Queue<int[]> queue,
            int col,
            int row,
            int initialSteps) {
        for (Direction direction : Direction.values()) {
            int nextCol = col + direction.dx;
            int nextRow = row + direction.dy;
            if (!inBounds(maze, nextCol, nextRow) || !maze.isWalkable(nextCol, nextRow)) {
                continue;
            }
            if (distances[nextRow][nextCol] != UNREACHABLE) {
                continue;
            }
            distances[nextRow][nextCol] = initialSteps;
            queue.add(new int[]{nextCol, nextRow});
        }
    }
}
