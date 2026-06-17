package com.picman.model.entity.ghost;

import com.picman.config.GameConfig;
import com.picman.config.GhostSpawn;
import com.picman.model.Maze;
import com.picman.model.entity.GhostMode;
import com.picman.model.entity.Pacman;

import java.util.List;

/**
 * 白色幽靈 - 速度是普通幽靈的 1.5 倍。
 * 在玩家分數 > 2000 時出生（由 Game 類控制）。
 */
public class WhiteGhost extends AbstractGhost {
    public WhiteGhost(GhostSpawn spawn) {
        super(spawn);
    }

    @Override
    public void update(Maze maze, Pacman pacman, List<Ghost> allGhosts) {
        GhostNavigator.ensureOnWalkableTile(
                maze, getPosition(), getSpawnColProtected(), getSpawnRowProtected(), getMode(),
                this::getDirectionProtected, this::setDirectionProtected);

        switch (getMode()) {
            case WAITING -> {
            }
            case LEAVING -> GhostNavigator.updateLeavingWithSpeed(
                    maze, getPosition(), this::getDirectionProtected, this::setDirectionProtected,
                    this::setModeProtected, GameConfig.GHOST_SPEED_FAST);
            case ACTIVE, FRIGHTENED -> GhostNavigator.updateChaseWithSpeed(
                    maze, pacman, allGhosts, getPosition(), getMode(),
                    this::getDirectionProtected, this::setDirectionProtected, 
                    getAIProtected(), getFrightenedAIProtected(),
                    GameConfig.GHOST_SPEED_FAST);
        }
    }
}

