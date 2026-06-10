package com.picman.model.ai;

import com.picman.model.entity.GhostKind;
import com.picman.model.entity.Pacman;
import com.picman.model.entity.ghost.Ghost;

import java.util.List;

/**
 * 幽靈 AI 決策所需的遊戲情境（玩家與其他幽靈位置）。
 */
public record GhostAIContext(Pacman pacman, List<Ghost> ghosts) {
    public static GhostAIContext of(Pacman pacman, List<Ghost> ghosts) {
        return new GhostAIContext(pacman, ghosts);
    }

    public Ghost redGhost() {
        return ghosts.get(GhostKind.RED.index());
    }
}
