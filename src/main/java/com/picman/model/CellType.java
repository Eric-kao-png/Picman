package com.picman.model;

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
}
