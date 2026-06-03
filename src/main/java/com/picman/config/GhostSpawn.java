package com.picman.config;

import com.picman.util.Direction;

import java.awt.Color;

public record GhostSpawn(int col, int row, Direction initialDirection, Color color) {
}
