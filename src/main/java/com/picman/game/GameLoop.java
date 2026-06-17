package com.picman.game;

import com.picman.model.GameSession;
import com.picman.model.entity.GhostReleaseScheduler;
import com.picman.movement.GridMovement;

/**
 * 遊戲主迴圈：封裝每個 tick 的完整更新管線。
 *
 * <p>原本散落在 {@code Game.update()} 中的協調邏輯集中至此，
 * 讓 {@link com.picman.Game} Facade 只需 delegate 給此類即可。
 *
 * <p>更新順序：
 * <ol>
 *   <li>計時器 tick（elapsed、invincibility、powered、pickaxe、broken walls）</li>
 *   <li>Pac-Man 移動（含前後 eject）</li>
 *   <li>道具生成與收集</li>
 *   <li>幽靈釋放 → 狀態同步 → AI 更新</li>
 *   <li>碰撞解算</li>
 *   <li>勝利判定</li>
 * </ol>
 */
public class GameLoop {
    private final GhostOrchestrator ghostOrchestrator = new GhostOrchestrator();
    private final GameCollisionHandler collisionHandler = new GameCollisionHandler();

    /**
     * 執行一個 tick 的完整遊戲邏輯。
     *
     * @param world            遊戲世界（maze / pacman / ghosts）
     * @param session          場次狀態（分數、生命、計時等）
     * @param releaseScheduler 幽靈釋放排程
     * @param itemScheduler    道具生成排程
     * @param collectibleCollector 收集物處理器
     */
    public void tick(
            GameWorld world,
            GameSession session,
            GhostReleaseScheduler releaseScheduler,
            ItemSpawnScheduler itemScheduler,
            CollectibleCollector collectibleCollector) {

        if (!session.isPlaying()) {
            return;
        }

        // 1. 計時器
        session.tickElapsed();
        session.tickInvincibility();
        session.tickPowered();
        session.tickPickaxe();
        world.getMaze().tickBrokenWalls();

        // 2. Pac-Man 移動
        GridMovement.ejectFromSolidCell(world.getMaze(), world.getPacman().getPosition());
        world.getPacman().update(world.getMaze(), session.isPickaxeActive());
        GridMovement.ejectFromSolidCell(world.getMaze(), world.getPacman().getPosition());

        // 3. 道具生成與收集
        itemScheduler.tick(world.getMaze(), session);
        collectibleCollector.collectAt(world.getMaze(), world.getPacman(), session);

        // 4. 幽靈更新（釋放 → 狀態同步 → AI）
        ghostOrchestrator.tick(world, session, releaseScheduler);

        // 5. 碰撞解算
        collisionHandler.resolve(world.getPacman(), world.getGhosts(), session, releaseScheduler);

        // 6. 勝利判定
        if (world.getMaze().noCoinsLeft()) {
            session.onAllCoinsCollected();
        }
    }
}
