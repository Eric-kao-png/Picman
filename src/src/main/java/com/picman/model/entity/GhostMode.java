package com.picman.model.entity;

public enum GhostMode {
    /** 在房內等待放行 */
    WAITING,
    /** 已放行，沿出口離開房間 */
    LEAVING,
    /** 一般追擊 */
    ACTIVE,
    /** 被大金幣影響，可被玩家吃掉 */
    FRIGHTENED
}
