# 如何新增幽靈類型

本文件說明如何在 Picman 遊戲中新增新的幽靈類型。

## 架構概覽

遊戲現在使用 **動態幽靈註冊系統**，使得新增幽靈類型變得簡單且靈活：

- **GhostRegistry**: 中央註冊表，管理所有幽靈類型、其生成配置和工廠
- **GhostKind**: 幽靈名稱常量和工具方法
- **GhostSpawnInfo**: 預設幽靈初始化器
- **GhostAssembly**: 根據註冊表建立幽靈

## 新增幽靈的步驟

### 1. 建立幽靈實體類

在 `src/main/java/com/picman/model/entity/ghost/` 中建立新的幽靈類別（例如 `PurpleGhost.java`）：

```java
package com.picman.model.entity.ghost;

import com.picman.config.GhostSpawn;
import com.picman.model.ai.GhostAI;

public class PurpleGhost extends Ghost {
    public PurpleGhost(GhostSpawn spawn) {
        super(spawn);
    }
}
```

### 2. 建立幽靈工廠

在 `src/main/java/com/picman/model/entity/ghostFactory/` 中建立新的工廠類（例如 `PurpleGhostFactory.java`）：

```java
package com.picman.model.entity.ghostFactory;

import com.picman.model.entity.GhostKind;
import com.picman.model.entity.ghost.Ghost;
import com.picman.model.entity.ghost.PurpleGhost;

public class PurpleGhostFactory implements GhostFactory {
    @Override
    public Ghost createGhost() {
        return new PurpleGhost(GhostKind.spawn(GhostKind.PURPLE));
    }
}
```

### 3. 在 GhostKind 中定義幽靈名稱

編輯 `GhostKind.java`，新增幽靈名稱常數：

```java
public class GhostKind {
    public static final String RED = "RED";
    public static final String PINK = "PINK";
    public static final String CYAN = "CYAN";
    public static final String ORANGE = "ORANGE";
    public static final String PURPLE = "PURPLE";  // 新增
    
    // ... 其他方法
}
```

### 4. 建立幽靈生成配置

建立一個 `GhostSpawn` 配置物件，指定：
- 初始位置 (col, row)
- 初始方向
- 顏色
- AI 行為

例如：

```java
new GhostSpawn(
    13, 12,                           // col, row
    Direction.DOWN,                   // 初始方向
    RenderTheme.GHOST_PURPLE,         // 顏色（需在 RenderTheme 中定義）
    GhostAIRegistry.YOUR_AI_BEHAVIOR  // AI 行為（需在 GhostAIRegistry 中定義）
)
```

### 5. 註冊幽靈到系統

在 `GhostSpawnInfo.initializeDefaultGhosts()` 中新增註冊（或在應用啟動時呼叫）：

```java
registry.registerGhost(
    GhostKind.PURPLE,
    new GhostSpawn(13, 12, Direction.DOWN, RenderTheme.GHOST_PURPLE, GhostAIRegistry.YOUR_AI),
    new PurpleGhostFactory()
);
```

## 範例：完整新增一隻紫色幽靈

### 步驟 1: 新增顏色到 RenderTheme

編輯 `src/main/java/com/picman/config/RenderTheme.java`：

```java
public static final Color GHOST_PURPLE = new Color(150, 0, 150);
```

### 步驟 2: 定義 AI 行為（如果需要）

在 `src/main/java/com/picman/model/ai/GhostAIRegistry.java` 中新增 AI：

```java
public static final GhostAI PURPLE_RANDOM = new RandomGhostAI();
```

### 步驟 3: 建立 PurpleGhost 類

### 步驟 4: 建立 PurpleGhostFactory 類

### 步驟 5: 更新 GhostKind

```java
public static final String PURPLE = "PURPLE";
```

### 步驟 6: 在 GhostSpawnInfo 中註冊

```java
registry.registerGhost(
    GhostKind.PURPLE,
    new GhostSpawn(13, 12, Direction.DOWN, RenderTheme.GHOST_PURPLE, GhostAIRegistry.PURPLE_RANDOM),
    new PurpleGhostFactory()
);
```

## 動態註冊

如果想在執行時動態新增幽靈（例如不同難度有不同幽靈），可以：

```java
GhostRegistry registry = GhostRegistry.getInstance();
registry.registerGhost(
    "CUSTOM_GHOST",
    spawnConfig,
    factory
);
```

但請確保在呼叫 `GhostAssembly.createAll()` 之前進行註冊。

## 注意事項

1. **幽靈名稱必須唯一**: 不能註冊相同名稱的幽靈兩次
2. **索引順序很重要**: 幽靈會按註冊順序編號（0, 1, 2, ...）
3. **GhostAIContext.redGhost()**: 目前仍硬編碼為 RED 幽靈，如需更改請修改該方法
4. **GhostReleaseScheduler**: 使用幽靈索引管理釋放順序，新增幽靈會自動加入釋放隊列

## 遷移指南

舊代碼如果直接使用 `GhostKind.RED` 等列舉值需要改為：
- 舊: `GhostKind.RED.index()` → 新: `GhostKind.index(GhostKind.RED)`
- 舊: `GhostKind.RED.spawn()` → 新: `GhostKind.spawn(GhostKind.RED)`
