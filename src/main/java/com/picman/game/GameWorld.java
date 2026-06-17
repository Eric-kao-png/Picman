package com.picman.game;

import com.picman.model.Maze;
import com.picman.model.entity.Pacman;
import com.picman.model.entity.ghost.Ghost;
import com.picman.model.entity.ghostFactory.GhostAssembly;

import java.util.List;

/**
 * 遊戲世界：封裝所有遊戲實體（地圖、Pac-Man、幽靈群）。
 *
 * <p>此類只負責持有與重置實體狀態，不含任何遊戲邏輯。
 * 是 {@link com.picman.Game} Facade 的核心資料層。
 */
public class GameWorld {
    private final Maze maze = new Maze();
    private final Pacman pacman = new Pacman();
    private final List<Ghost> ghosts = GhostAssembly.createAll();

    public Maze getMaze() {
        return maze;
    }

    public Pacman getPacman() {
        return pacman;
    }

    public List<Ghost> getGhosts() {
        return ghosts;
    }

    /** 重置所有實體至初始狀態。 */
    public void reset() {
        maze.reset();
        pacman.reset();
    }
}
