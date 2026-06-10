package com.picman.model;

import com.picman.config.RenderTheme;

import java.awt.Color;

public enum ItemType {
    EXTRA_LIFE(CellType.EXTRA_LIFE_ITEM, RenderTheme.ITEM_EXTRA_LIFE),
    TEMP_POWER_COIN(CellType.TEMP_POWER_COIN, RenderTheme.POWER_COIN);

    private final CellType cellType;
    private final Color color;

    ItemType(CellType cellType, Color color) {
        this.cellType = cellType;
        this.color = color;
    }

    public CellType getCellType() {
        return cellType;
    }

    public Color getColor() {
        return color;
    }

    public static ItemType fromCellType(CellType cellType) {
        for (ItemType type : values()) {
            if (type.cellType == cellType) {
                return type;
            }
        }
        return null;
    }
}
