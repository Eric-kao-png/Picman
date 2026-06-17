package com.picman.model;

import java.util.Optional;

public enum CellType {
    EMPTY(0),
    WALL(1),
    COIN(2),
    POWER_COIN(3),
    EXTRA_LIFE_ITEM(4),
    TEMP_POWER_COIN(5),
    PICKAXE_ITEM(6);

    private final int code;

    CellType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static CellType fromCode(int code) {
        for (CellType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        return EMPTY;
    }

    public Optional<CollectibleType> asCollectible() {
        return switch (this) {
            case COIN -> Optional.of(CollectibleType.COIN);
            case POWER_COIN, TEMP_POWER_COIN -> Optional.of(CollectibleType.POWER_COIN);
            case EXTRA_LIFE_ITEM -> Optional.of(CollectibleType.EXTRA_LIFE_ITEM);
            case PICKAXE_ITEM -> Optional.of(CollectibleType.PICKAXE_ITEM);
            default -> Optional.empty();
        };
    }
}
