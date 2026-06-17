package com.picman;

import com.picman.game.CollectibleCollector;
import com.picman.game.GameLoop;
import com.picman.game.GameWorld;
import com.picman.game.ItemSpawnScheduler;
import com.picman.model.GameSession;
import com.picman.model.GameStatus;
import com.picman.model.entity.GhostReleaseScheduler;
import com.picman.model.entity.GhostSpawnInfo;
import com.picman.render.GameRenderer;
import com.picman.render.ViewLayout;
import com.picman.util.Direction;

import java.awt.Graphics2D;

/**
 * 遊戲 Facade：對外提供簡潔的操作介面，對內 delegate 給各子系統。
 *
 * <p>此類不包含任何遊戲邏輯本身，所有細節由以下子系統負責：
 * <ul>
 *   <li>{@link GameWorld}             — 持有所有遊戲實體（maze、pacman、ghosts）</li>
 *   <li>{@link GameLoop}              — 每 tick 的完整更新管線</li>
 *   <li>{@link com.picman.game.GhostOrchestrator} — 幽靈狀態同步與 AI 更新</li>
 *   <li>{@link com.picman.game.GameCollisionHandler} — 碰撞解算</li>
 *   <li>{@link ItemSpawnScheduler}    — 道具生成排程</li>
 *   <li>{@link CollectibleCollector}  — 收集物副作用</li>
 *   <li>{@link GameSession}           — 分數、生命、計時等場次狀態</li>
 *   <li>{@link GameRenderer}          — 繪製</li>
 * </ul>
 */
public class Game {
    static {
        GhostSpawnInfo.initializeDefaultGhosts();
    }

    private final GameWorld world = new GameWorld();
    private final GhostReleaseScheduler ghostReleaseScheduler = new GhostReleaseScheduler();
    private final GameSession session = new GameSession();
    private final ItemSpawnScheduler itemSpawnScheduler = new ItemSpawnScheduler();
    private final CollectibleCollector collectibleCollector = new CollectibleCollector(itemSpawnScheduler);
    private final GameLoop gameLoop = new GameLoop();
    private final GameRenderer renderer = new GameRenderer();

    public Game() {
        ghostReleaseScheduler.reset(world.getGhosts());
        itemSpawnScheduler.reset();
    }

    // ── 更新 ──────────────────────────────────────────────────────────────────

    public void update() {
        gameLoop.tick(world, session, ghostReleaseScheduler, itemSpawnScheduler, collectibleCollector);
    }

    // ── 渲染 ──────────────────────────────────────────────────────────────────

    public void render(Graphics2D g) {
        renderer.render(g, world.getMaze(), world.getPacman(), world.getGhosts(), session);
    }

    // ── 輸入 ──────────────────────────────────────────────────────────────────

    public void setActiveDirection(Direction direction) {
        if (session.isPlaying()) {
            world.getPacman().setActiveDirection(direction);
        }
    }

    // ── 生命週期 ─────────────────────────────────────────────────────────────

    public void restart() {
        world.reset();
        ghostReleaseScheduler.reset(world.getGhosts());
        itemSpawnScheduler.reset();
        session.reset();
    }

    // ── 狀態查詢（供 GamePanel / GameOverViewModel 使用）──────────────────────

    public GameStatus getStatus() {
        return session.getStatus();
    }

    public int getScore() {
        return session.getScore();
    }

    public int getPelletsCollected() {
        return session.getPelletsCollected();
    }

    public int getGhostsEaten() {
        return session.getGhostsEaten();
    }

    public int getElapsedSeconds() {
        return session.getElapsedSeconds();
    }

    // ── 版面尺寸（供 GamePanel 初始化用）─────────────────────────────────────

    public int getPanelWidth() {
        return ViewLayout.panelWidth(world.getMaze());
    }

    public int getPanelHeight() {
        return ViewLayout.panelHeight(world.getMaze());
    }
}
