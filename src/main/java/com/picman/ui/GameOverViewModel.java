package com.picman.ui;

import com.picman.model.GameStatus;

/** Game Over 畫面所需的唯讀統計資料。 */
public record GameOverViewModel(
        GameStatus status,
        int score,
        int pelletsCollected,
        int ghostsEaten,
        int elapsedSeconds) {
}
