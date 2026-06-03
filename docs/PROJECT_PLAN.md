# Picman 專案規劃

> Java Swing 版吃豆人（Pac-Man）遊戲開發計畫  
> 最後更新：2026-06-03

---

## 一、專案概述

本專案目標是以 **Java + Swing** 實作一款 2D 吃豆人遊戲。採漸進式開發：先完成最小可玩版本（MVP），再逐步擴充功能與打磨體驗。

**MVP 核心體驗：** 玩家在一張固定迷宮中操控吃豆人移動、收集普通金幣賺取分數，同時躲避一隻用最簡單邏輯追擊的幽靈；生命耗盡則 Game Over，金幣全部收集完則過關。

### MVP 五大限制（明確邊界）

| # | 限制 | 說明 |
|---|------|------|
| 1 | **一張地圖** | 程式內嵌單一迷宮陣列，無關卡切換、無外部地圖檔 |
| 2 | **一隻幽靈** | 僅一隻，貪婪 Manhattan 追擊，無進階 AI |
| 3 | **普通金幣** | 僅一種可收集物，無能量豆、水果等特殊道具 |
| 4 | **無音效** | 不播放背景音樂或音效 |
| 5 | **無動畫** | 不製作精靈幀動畫；以靜態色塊 / 基本幾何圖形繪製 |

---

## 二、MVP 範圍

### 包含

| 功能 | 說明 |
|------|------|
| Swing 視窗 + 遊戲迴圈 | `JFrame` + `JPanel` + `javax.swing.Timer` |
| **一張固定地圖** | 程式內嵌單一迷宮，過關後顯示 WIN，不重載下一關 |
| 吃豆人移動 | 四方向、不穿牆、純格子移動 |
| **普通金幣 + 分數** | 走過即收集，僅一種金幣，無能量豆 |
| **一隻幽靈** | 貪婪 Manhattan 追擊（最簡單邏輯） |
| 碰撞處理 | 碰到幽靈扣生命；命用完 Game Over |
| 過關條件 | 金幣全部收集完 |
| HUD | 分數、生命數（文字顯示即可） |
| **靜態繪製** | 色塊 / 圓形 / 矩形，無幀動畫、無貼圖序列 |

### 不包含（後續版本再加）

- **第二張以上地圖**、關卡切換、JSON / 文字檔載入地圖
- 能量豆、frightened 模式、反吃幽靈、水果等特殊道具
- 第二隻以上幽靈、四幽靈原作 AI
- **音效、背景音樂**
- **精靈動畫**（嘴開合、幽靈擺動、死亡動畫等）
- 左右隧道穿牆、選單、高分榜
- 像素平滑移動、A* / BFS 等進階尋路

### MVP 驗收標準

- [ ] 方向鍵可在迷宮中順暢移動，不穿牆
- [ ] 走過金幣會消失並加分
- [ ] 全程僅使用同一張地圖
- [ ] 畫面為靜態圖形，無動畫、無音效
- [ ] 幽靈會追擊玩家
- [ ] 被幽靈碰到扣生命，生命歸零顯示 Game Over
- [ ] 豆子全部收集完顯示過關（或重開）

---

## 三、技術選型

| 項目 | 選擇 |
|------|------|
| 語言 | Java（JDK 17+） |
| UI / 渲染 | **Swing**（`JFrame` + `JPanel` + `paintComponent`） |
| 遊戲迴圈 | `javax.swing.Timer`（建議 tick 間隔約 60ms，可調） |
| 建構工具 | Maven 或 Gradle（單模組即可） |
| 地圖資料 | 單一二維陣列（`int[][]`），**硬編碼在 `Maze` 或 `Constants` 中** |
| 視覺表現 | `Graphics2D` 填色繪製，**不使用圖片資源與動畫幀** |
| 音效 | **無**（MVP 不引入 `javax.sound` 等 API） |
| 測試 | 邏輯層可寫 JUnit；UI 以手動測試為主 |

---

## 四、架構概覽

```
鍵盤輸入 → Game Loop → Update（移動、AI、碰撞）→ GameState
                ↓
              Render（paintComponent）
```

### 遊戲狀態機

```
PLAYING → GAME_OVER（生命 ≤ 0）
        → WIN（豆子清零）
        → PAUSED（可選，後加）
```

### 每 tick 流程

1. 讀取鍵盤輸入（方向鍵）
2. 更新吃豆人位置（含預輸入轉向，若已實作）
3. 嘗試吃豆子、更新分數
4. 幽靈選方向並移動
5. 碰撞檢測（幽靈 vs 吃豆人）
6. 勝負判定
7. 重繪畫面

---

## 五、目錄結構（建議）

```
picman/
├── pom.xml 或 build.gradle
├── docs/
│   └── PROJECT_PLAN.md          # 本文件
└── src/main/java/com/xxx/picman/
    ├── Main.java                 # 程式入口
    ├── GamePanel.java            # Swing 繪製、Timer、鍵盤事件
    ├── Game.java                 # 狀態機、update / render 協調
    ├── model/
    │   ├── Maze.java             # 地圖、牆判定、吃豆
    │   ├── Pacman.java           # 吃豆人位置與方向
    │   ├── Ghost.java            # 幽靈位置、方向、選路邏輯
    │   └── GameState.java        # 分數、生命
    ├── input/
    │   └── KeyBindings.java      # 鍵盤對應
    └── util/
        ├── Direction.java        # UP / DOWN / LEFT / RIGHT
        └── Constants.java        # TILE_SIZE、地圖尺寸、速度等
```

> MVP 階段不需要獨立的 `GhostAI.java`，幽靈選路邏輯可直接放在 `Ghost` 或 `Game.update()` 中。

---

## 六、地圖與金幣

### 單一地圖

- MVP **只有一張地圖**，以 `int[][]` 常數寫死在程式中
- 不需要地圖載入器、關卡索引、過關後換圖
- 過關（`WIN`）或 Game Over 後，可按 `R` **重設同一張地圖**再玩

### 格子類型

```
0 = 空（可走，無金幣）
1 = 牆
2 = 普通金幣
```

### 規則

- 吃豆人進入 `(col, row)` 且值為 `2` → 設為 `0`，`score += 10`
- `maze.countDots() == 0` → 進入 `WIN` 狀態
- **僅類型 `2` 一種可收集物**，無能量豆（類型 `3` 等留待後續）

### 地圖尺寸建議

- 原作比例約 28×31，**MVP 使用一張小迷宮即可**（如 **21×17** 或 **19×15**），方便手寫陣列、快速除錯。

---

## 七、幽靈 AI（MVP：最單純追擊）

不使用 A* 或 BFS。在岔路採 **貪婪 Manhattan 距離 + 平手隨機**：

1. 列出所有可走方向（**排除立即反向**）
2. 若只有 1 條路 → 走那條
3. 若多條路：
   - 計算每個方向「走一步後」到 Pac-Man 的 Manhattan 距離
   - 選距離最小的方向
   - 距離相同時隨機選一個（避免在角落來回抖動）

```java
// 概念偽代碼
List<Direction> options = maze.getValidDirections(ghost, excludeReverse: true);
if (options.isEmpty()) return;
Direction best = options.stream()
    .min(comparing(d -> manhattan(nextCell(ghost, d), pacman.cell)))
    .orElse(random(options));
ghost.setDirection(best);
```

---

## 八、移動模型

### 方案 A：純格子移動（MVP 推薦）

- 每 tick 整格移動一格
- 碰撞、吃豆、AI 皆以 `(col, row)` 計算
- **實作最快，建議 MVP 先用此方案**

### 方案 B：像素平滑移動（後續優化）

- 邏輯仍用格子，畫面以像素插值
- 轉向需等對齊格子中心
- 手感更好，但需額外處理「對齊中心才允許轉向」

---

## 九、類別職責

| 類別 | 職責 |
|------|------|
| `Main` | 建立 `JFrame`、啟動遊戲 |
| `GamePanel` | 接收鍵盤、驅動 Timer、呼叫 `game.update()` 與 `game.render(g)` |
| `Game` | 狀態機、協調各實體更新、勝負判定 |
| `Maze` | 地圖資料、牆判定、吃豆、剩餘豆子計數 |
| `Pacman` | 位置、目前方向、下一個意圖方向（預輸入，可選） |
| `Ghost` | 位置、方向、`chooseDirection(maze, pacman)` |
| `GameState` | 分數、生命（預設 3） |
| `Constants` | `TILE_SIZE`、地圖寬高、速度參數 |

---

## 十、簡化遊戲迴圈（偽代碼）

```
Timer tick:
  if state != PLAYING:
    repaint
    return

  pacman.applyBufferedDirection(maze)   // 預輸入轉向（可選）
  pacman.move(maze)
  maze.tryEatDot(pacman) → 更新 score

  ghost.chooseDirection(maze, pacman)
  ghost.move(maze)

  if ghost.collides(pacman) && !pacman.invincible:
    lives--
    重置雙方位置
    可選：短暫無敵（如 2 秒）

  if lives == 0: GAME_OVER
  if maze.noDotsLeft(): WIN

  repaint()
```

---

## 十一、開發流程（分階段）

### 階段 0：專案初始化（約 0.5 天）

- [ ] 建立 Maven / Gradle 專案
- [ ] 空視窗 + 遊戲迴圈（空白 `JPanel` 能持續刷新）
- [ ] 定義常數：`TILE_SIZE`（如 16px）、地圖寬高

### 階段 1：迷宮與移動（2–3 天）

- [ ] 二維陣列表示地圖
- [ ] 繪製牆壁與豆子
- [ ] 吃豆人格子移動 + 方向鍵
- [ ] 牆壁碰撞（下一格是牆則不動）

**驗收：** 能在迷宮中移動，不穿牆。

### 階段 2：豆子與分數（約 1 天）

- [ ] 走過豆子 → 消失 + 加分
- [ ] HUD 顯示分數、生命
- [ ] 豆子清零 → 過關

### 階段 3：一隻幽靈（2–3 天）

- [ ] 幽靈出生點、與吃豆人相同的移動規則
- [ ] 貪婪 Manhattan 追擊 AI
- [ ] 碰撞 → 扣生命、幽靈回出生點
- [ ] 可選：短暫無敵時間

**驗收：** 能被追、會扣命、有 Game Over。

### 階段 4：收尾與打磨（約 1 天）

- [ ] `R` 鍵或按鈕重開
- [ ] 調整速度：吃豆人略快於幽靈
- [ ] 基本 Game Over / WIN 畫面文字

### 建議時程（業餘節奏）

| 里程碑 | 約略時間 |
|--------|----------|
| 可動的吃豆人 + 迷宮 | 3 天 |
| MVP 可玩（豆子 + 幽靈 + 生命） | 1 週 |
| 手感打磨（平滑移動等） | +3–5 天 |

---

## 十二、建議初始常數

| 常數 | 建議值 |
|------|--------|
| 地圖大小 | 21 × 17 |
| 格子像素 | 16 px |
| 初始生命 | 3 |
| 小豆分數 | 10 |
| 吃豆人速度 | 每 tick 移動 1 格 |
| 幽靈速度 | 每 2 tick 移動 1 格（略慢於玩家） |
| Timer 間隔 | 60 ms（可調） |

---

## 十三、關鍵技術點（易踩坑）

1. **格子座標 vs 像素座標**  
   邏輯用 `(col, row)`，渲染用 `col * TILE_SIZE`。若改平滑移動，需在格子中心對齊時才允許轉向。

2. **碰撞判定**  
   - 牆：下一格是否為牆  
   - 豆子：當前格類型  
   - 幽靈：同格重疊，或 Manhattan 距離 ≤ 1（依移動模型而定）

3. **預輸入轉向（可選，改善手感）**  
   玩家提前按下下一方向，到達可轉彎的格子時自動轉向——原作手感關鍵之一。

4. **幽靈不反向**  
   選路時排除與目前方向相反的路，避免在長走廊來回抖動。

---

## 十四、Git 分支策略（建議）

```
main                    ← 穩定可玩版本
├── feat/project-setup
├── feat/maze
├── feat/pacman-movement
├── feat/dots-score
└── feat/ghost-basic
```

每個分支合併前確保：能編譯、能執行、該階段驗收通過。

---

## 十五、後續擴展順序

完成 MVP 後，建議按以下順序擴充：

1. 像素平滑移動 + 轉角預輸入（手感）
2. 左右隧道穿牆
3. 第二隻幽靈
4. 能量豆 + frightened 模式
5. 四隻幽靈 + 進階 AI（Blinky / Pinky / Inky / Clyde 策略）
6. 關卡檔 / JSON 地圖載入
7. 音效、動畫、選單 UI

### 進階幽靈 AI 參考（非 MVP）

| 幽靈 | 簡化策略 |
|------|----------|
| Blinky（紅） | 直接追 Pac-Man 所在格 |
| Pinky（粉） | 追 Pac-Man 前方 4 格 |
| Inky（青） | 結合 Blinky 位置與 Pac-Man |
| Clyde（橙） | 遠時追、近時亂走 |

共用規則：岔路選「到目標 Manhattan 距離最短」且不反向的路。

---

## 十六、修訂紀錄

| 日期 | 變更 |
|------|------|
| 2026-06-03 | 初版：Swing MVP、一隻幽靈、貪婪尋路、普通豆子 |

---

## 附錄：完整版規劃（超出 MVP 的原始構想）

以下為最初討論的完整吃豆人構想，供日後擴展參考，**不屬於 MVP 範圍**。

| 階段 | 內容 |
|------|------|
| MVP（完整版構想） | 迷宮、移動、豆子、四幽靈、碰撞、生命/分數、一關 |
| v1 | 能量豆、frightened、吃幽靈、關卡切換 |
| v2 | 音效、UI 選單、暫停、重開、高分榜 |

渲染方案曾比較 Swing 與 JavaFX；**本專案已確定使用 Swing**。
