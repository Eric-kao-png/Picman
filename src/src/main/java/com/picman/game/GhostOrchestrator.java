package com.picman.game;

import com.picman.model.GameSession;
import com.picman.model.entity.GhostKind;
import com.picman.model.entity.GhostMode;
import com.picman.model.entity.GhostReleaseScheduler;
import com.picman.model.entity.Pacman;
import com.picman.model.entity.ghost.Ghost;
import com.picman.model.Maze;

import java.util.List;

/**
 * 幽靈總指揮：集中管理幽靈的狀態轉換與每幀更新。
 *
 * <p>負責的三件事：
 * <ol>
 *   <li>依 {@link GameSession#isPowered()} 同步所有幽靈的 Frightened 狀態。</li>
 *   <li>依分數門檻特殊釋放白色幽靈。</li>
 *   <li>驅動每隻幽靈的 {@code update()}。</li>
 * </ol>
 *
 * <p>這些邏輯原散落在 {@code Game.update()} 內，提取至此可讓 {@link com.picman.Game}
 * 成為純 Facade，不再直接操作幽靈清單細節。
 */
public class GhostOrchestrator {

    private static final int WHITE_GHOST_SCORE_THRESHOLD = 2000;

    /**
     * 每 tick 執行幽靈相關的狀態同步與 AI 更新。
     *
     * @param world   目前的遊戲世界（含 maze、pacman、ghosts）
     * @param session 目前的遊戲場次狀態
     * @param releaseScheduler 幽靈釋放排程器
     */
    public void tick(GameWorld world, GameSession session, GhostReleaseScheduler releaseScheduler) {
        List<Ghost> ghosts = world.getGhosts();
        Maze maze = world.getMaze();
        Pacman pacman = world.getPacman();

        releaseScheduler.tick(ghosts);
        releaseWhiteGhostIfScoreThreshold(ghosts, session);
        syncFrightenedState(ghosts, session);

        for (Ghost ghost : ghosts) {
            ghost.update(maze, pacman, ghosts);
        }
    }

    /**
     * 當分數超過門檻時，將白色幽靈從幽靈屋中釋放（僅釋放一次）。
     */
    private void releaseWhiteGhostIfScoreThreshold(List<Ghost> ghosts, GameSession session) {
        if (session.getScore() <= WHITE_GHOST_SCORE_THRESHOLD) {
            return;
        }
        int whiteGhostIndex = GhostKind.index(GhostKind.WHITE);
        if (whiteGhostIndex < 0 || whiteGhostIndex >= ghosts.size()) {
            return;
        }
        Ghost whiteGhost = ghosts.get(whiteGhostIndex);
        if (whiteGhost.getMode() == GhostMode.WAITING) {
            whiteGhost.releaseFromHouse();
        }
    }

    /**
     * 依 Powered 狀態批次同步所有幽靈的 Frightened 旗標。
     */
    private void syncFrightenedState(List<Ghost> ghosts, GameSession session) {
        if (session.isPowered()) {
            ghosts.forEach(Ghost::enterFrightened);
        } else {
            ghosts.forEach(Ghost::exitFrightened);
        }
    }
}
