package com.picman.model.entity;

import com.picman.config.GhostSpawn;
import com.picman.model.entity.ghostFactory.GhostFactory;
import com.picman.model.entity.ghost.Ghost;

import java.util.*;

/**
 * 動態幽靈註冊管理，支援動態增加幽靈類型。
 * 提供幽靈的生成資訊、工廠和索引管理。
 */
public class GhostRegistry {
    private static final GhostRegistry INSTANCE = new GhostRegistry();
    
    private final List<String> ghostNames = new ArrayList<>();
    private final Map<String, GhostSpawn> spawnConfigs = new LinkedHashMap<>();
    private final Map<String, GhostFactory> factories = new LinkedHashMap<>();
    
    private GhostRegistry() {
    }
    
    public static GhostRegistry getInstance() {
        return INSTANCE;
    }
    
    /**
     * 註冊一個新的幽靈類型。
     * @param name 幽靈名稱（例如 "RED", "PINK"）
     * @param spawn 幽靈的生成配置
     * @param factory 幽靈的工廠
     */
    public void registerGhost(String name, GhostSpawn spawn, GhostFactory factory) {
        if (ghostNames.contains(name)) {
            throw new IllegalArgumentException("Ghost type '" + name + "' is already registered");
        }
        ghostNames.add(name);
        spawnConfigs.put(name, spawn);
        factories.put(name, factory);
    }
    
    /**
     * 獲取幽靈的生成配置。
     */
    public GhostSpawn getSpawn(String ghostName) {
        if (!spawnConfigs.containsKey(ghostName)) {
            throw new IllegalArgumentException("Ghost type '" + ghostName + "' not found");
        }
        return spawnConfigs.get(ghostName);
    }
    
    /**
     * 獲取幽靈的索引。
     */
    public int getIndex(String ghostName) {
        int index = ghostNames.indexOf(ghostName);
        if (index == -1) {
            throw new IllegalArgumentException("Ghost type '" + ghostName + "' not found");
        }
        return index;
    }
    
    /**
     * 根據索引獲取幽靈名稱。
     */
    public String getNameByIndex(int index) {
        if (index < 0 || index >= ghostNames.size()) {
            throw new IndexOutOfBoundsException("Ghost index " + index + " out of bounds");
        }
        return ghostNames.get(index);
    }
    
    /**
     * 獲取所有已註冊的幽靈名稱。
     */
    public List<String> getAllGhostNames() {
        return new ArrayList<>(ghostNames);
    }
    
    /**
     * 創建所有已註冊的幽靈。
     */
    public List<Ghost> createAllGhosts() {
        return factories.values().stream()
                .map(GhostFactory::createGhost)
                .toList();
    }
    
    /**
     * 獲取已註冊幽靈的總數。
     */
    public int size() {
        return ghostNames.size();
    }
    
    /**
     * 重置註冊表（測試時使用）。
     */
    public void reset() {
        ghostNames.clear();
        spawnConfigs.clear();
        factories.clear();
    }
}
