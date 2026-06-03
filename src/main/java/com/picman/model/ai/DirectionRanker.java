package com.picman.model.ai;

import com.picman.util.Direction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;

/**
 * 依分數在候選方向中挑選最佳者（支援最小化／最大化）。
 */
final class DirectionRanker {
    private DirectionRanker() {
    }

    static Direction selectMinimum(
            int col,
            int row,
            List<Direction> options,
            ToIntBiFunction<Integer, Integer> scoreAtCell,
            Direction currentDirection) {
        return selectByCell(col, row, options, scoreAtCell, currentDirection, Comparator.naturalOrder());
    }

    static Direction selectMaximumByDirection(
            int col,
            int row,
            List<Direction> options,
            ToIntFunction<Direction> scoreForDirection,
            Direction currentDirection) {
        return selectByDirection(options, scoreForDirection, currentDirection, Comparator.reverseOrder());
    }

    private static Direction selectByCell(
            int col,
            int row,
            List<Direction> options,
            ToIntBiFunction<Integer, Integer> scoreAtCell,
            Direction currentDirection,
            Comparator<Integer> scoreOrder) {
        List<Direction> bestOptions = new ArrayList<>();
        Integer bestScore = null;

        for (Direction candidate : options) {
            int score = scoreAtCell.applyAsInt(col + candidate.dx, row + candidate.dy);
            if (bestScore == null || scoreOrder.compare(score, bestScore) < 0) {
                bestScore = score;
                bestOptions.clear();
                bestOptions.add(candidate);
            } else if (scoreOrder.compare(score, bestScore) == 0) {
                bestOptions.add(candidate);
            }
        }
        return pickBest(bestOptions, currentDirection);
    }

    private static Direction selectByDirection(
            List<Direction> options,
            ToIntFunction<Direction> scoreForDirection,
            Direction currentDirection,
            Comparator<Integer> scoreOrder) {
        List<Direction> bestOptions = new ArrayList<>();
        Integer bestScore = null;

        for (Direction candidate : options) {
            int score = scoreForDirection.applyAsInt(candidate);
            if (bestScore == null || scoreOrder.compare(score, bestScore) < 0) {
                bestScore = score;
                bestOptions.clear();
                bestOptions.add(candidate);
            } else if (scoreOrder.compare(score, bestScore) == 0) {
                bestOptions.add(candidate);
            }
        }
        return pickBest(bestOptions, currentDirection);
    }

    private static Direction pickBest(List<Direction> bestOptions, Direction currentDirection) {
        if (bestOptions.isEmpty()) {
            return null;
        }
        if (currentDirection != null && bestOptions.contains(currentDirection)) {
            return currentDirection;
        }
        return bestOptions.get(0);
    }
}
