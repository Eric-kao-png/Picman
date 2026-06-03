package com.picman.model.ai;

import com.picman.model.entity.Ghost;
import com.picman.model.entity.GhostDefinitions;
import com.picman.model.entity.Pacman;

import java.util.List;

/**
 * 幽靈 AI 決策所需的遊戲情境（玩家與其他幽靈位置）。
 */
public record GhostAIContext(Pacman pacman, List<Ghost> ghosts) {
    public Ghost redGhost() {
        return ghosts.get(GhostDefinitions.RED_GHOST_INDEX);
    }
}
