package com.picman.model.entity;

import com.picman.movement.GridMath;

public class GridPosition {
    private double centerX;
    private double centerY;

    public GridPosition(int col, int row) {
        snapToCell(col, row);
    }

    public int getCol() {
        return GridMath.cellIndex(centerX);
    }

    public int getRow() {
        return GridMath.cellIndex(centerY);
    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public void setCenter(double centerX, double centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public void setCenterX(double centerX) {
        this.centerX = centerX;
    }

    public void setCenterY(double centerY) {
        this.centerY = centerY;
    }

    public void snapToCell(int col, int row) {
        centerX = GridMath.cellCenter(col);
        centerY = GridMath.cellCenter(row);
    }
}
