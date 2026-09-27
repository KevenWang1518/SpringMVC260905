package com.wyjun.controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * CYBERPUNK MEGAPOLIS — Web-Swing Edition  (Java / Swing 单文件版)
 * 直接导入 IDEA，运行 main 即可。
 */
public class CyberpunkSwing extends JPanel
        implements ActionListener, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    // ==================== 常量 ====================
    static final int W = 1280, H = 720;
    static final double FOCAL = H * 0.90;
    static final double NEAR = 0.20;

    static final double GRAVITY      = -30.0;
    static final double RUN_SPEED    = 12.0;
    static final double SPRINT_SPEED = 19.0;
    static final double JUMP_VEL     = 14.5;
    static final double AIR_ACCEL    = 24.0;
    static final double MAX_FALL     = -65.0;
    static final double P_RAD        = 0.55;
    static final double P_H          = 1.8;

    static final double ROPE_MIN = 4.0;
    static final double ROPE_MAX = 130.0;
    static final double REEL_SPEED = 16.0;

    // ==================== 状态 ====================
    enum Scene { MENU, PLAY, PAUSE }
    Scene scene = Scene.MENU;

    enum Quality { HIGH, MEDIUM, LOW }
    Quality quality = Quality.HIGH;

    enum TimeOfDay { DUSK, NIGHT, NOON }
    TimeOfDay timeOfDay = TimeOfDay.DUSK;

    boolean femaleChar = false;

    // ==================== 建筑 ====================
    static class Building {
        double x, z, w, d, h;
        float hue;
        boolean neonTop;
        double seed;
    }
    final ArrayList<Building> buildings = new ArrayList<>();
    final HashMap<Long, ArrayList<Building>> grid = new HashMap<>();
    static final double CELL = 24.0;

    // ==================== 玩家 ====================
    double px, py, pz;
    double vx, vy, vz;
    boolean onGround;
    boolean wallRunning;
    double wallNx, wallNz;
    double wallRunTime;
    double playerYaw;
    double playerAnim;      // 动画计时
    double playerLean;      // 身体倾斜

    // 绳索
    boolean ropeOn;
    double ropeX, ropeY, ropeZ;
    double ropeLen, ropeTargetLen;
    boolean reeling;

    // 相机
    double camX, camY, camZ, camYaw, camPitch;

    // 输入
    boolean kW, kA, kS, kD, kShift, kSpace, kCtrl;
    boolean mouseLDown, mouseRDown;
    int mouseX = W / 2, mouseY = H / 2;

    // 统计
    double maxSpeed, distance;
    double lastPx, lastPz;
    String stateLabel = "IDLE";
    String toast = "";
    long toastUntil;
    int fps = 60;
    long fpsAccum; int fpsCount;

    // 时间
    javax.swing.Timer timer;
    long lastNano;
    Random rng = new Random(20240607L);

    // 屏幕点击区域（菜单）
    final Rectangle rCharMale  = new Rectangle(370, 300, 220, 150);
    final Rectangle rCharFem   = new Rectangle(690, 300, 220, 150);
    final Rectangle rQH = new Rectangle(370, 490, 130, 40);
    final Rectangle rQM = new Rectangle(510, 490, 130, 40);
    final Rectangle rQL = new Rectangle(650, 490, 130, 40);
    final Rectangle rTD = new Rectangle(370, 570, 130, 40);
    final Rectangle rTN = new Rectangle(510, 570, 130, 40);
    final Rectangle rTNo= new Rectangle(650, 570, 130, 40);
    final Rectangle rStart = new Rectangle(440, 640, 400, 56);
    final Rectangle rResume = new Rectangle(440, 340, 400, 56);
    final Rectangle rMenu   = new Rectangle(440, 410, 400, 56);

    // ==================== main ====================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("CYBERPUNK MEGAPOLIS — Web-Swing Edition");
            CyberpunkSwing panel = new CyberpunkSwing();
            f.setContentPane(panel);
            f.pack();
            f.setResizable(false);
            f.setLocationRelativeTo(null);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setVisible(true);
            panel.requestFocusInWindow();
            panel.startLoop();
        });
    }

    // ==================== 构造 ====================
    public CyberpunkSwing() {
        setPreferredSize(new Dimension(W, H));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);

        generateCity();
        respawn();
        updateCameraBasis();
        // 初始化相机位置
        camX = px; camY = py + 4; camZ = pz - 8;
        camYaw = 0; camPitch = 0;
    }

    void startLoop() {
        timer = new javax.swing.Timer(8, this);
        lastNano = System.nanoTime();
        timer.start();
    }

    // ==================== 城市生成 ====================
    void generateCity() {
        buildings.clear();
        grid.clear();
        int n = 9;
        double block = 46, road = 15;
        double origin = -(n * (block + road)) / 2.0;

        for (int bx = 0; bx < n; bx++) {
            for (int bz = 0; bz < n; bz++) {
                double cx = origin + bx * (block + road) + block / 2.0;
                double cz = origin + bz * (block + road) + block / 2.0;
                int count = 1 + rng.nextInt(3);
                for (int i = 0; i < count; i++) {
                    Building b = new Building();
                    b.w = 11 + rng.nextDouble() * 16;
                    b.d = 11 + rng.nextDouble() * 16;
                    double maxOffX = (block - b.w) / 2.0 - 1.5;
                    double maxOffZ = (block - b.d) / 2.0 - 1.5;
                    b.x = cx + (rng.nextDouble() - 0.5) * 2 * maxOffX;
                    b.z = cz + (rng.nextDouble() - 0.5) * 2 * maxOffZ;
                    double dist = Math.hypot(b.x, b.z);
                    double maxH = Math.max(28, 130 - dist * 0.75);
                    b.h = 18 + rng.nextDouble() * maxH;
                    b.hue = 0.52f + rng.nextFloat() * 0.38f;
                    b.neonTop = rng.nextFloat() < 0.55f;
                    b.seed = rng.nextDouble() * 100;
                    buildings.add(b);
                }
            }
        }
        // 空间哈希
        for (Building b : buildings) {
            int minGx = (int) Math.floor((b.x - b.w / 2) / CELL);
            int maxGx = (int) Math.floor((b.x + b.w / 2) / CELL);
            int minGz = (int) Math.floor((b.z - b.d / 2) / CELL);
            int maxGz = (int) Math.floor((b.z + b.d / 2) / CELL);
            for (int gx = minGx; gx <= maxGx; gx++)
                for (int gz = minGz; gz <= maxGz; gz++) {
                    long key = ((long) gx << 32) | (gz & 0xffffffffL);
                    grid.computeIfAbsent(key, k -> new ArrayList<>()).add(b);
                }
        }
    }

    ArrayList<Building> nearby(double x, double z, double r) {
        HashSet<Building> set = new LinkedHashSet<>();
        int minGx = (int) Math.floor((x - r) / CELL);
        int maxGx = (int) Math.floor((x + r) / CELL);
        int minGz = (int) Math.floor((z - r) / CELL);
        int maxGz = (int) Math.floor((z + r) / CELL);
        for (int gx = minGx; gx <= maxGx; gx++)
            for (int gz = minGz; gz <= maxGz; gz++) {
                long key = ((long) gx << 32) | (gz & 0xffffffffL);
                ArrayList<Building> l = grid.get(key);
                if (l != null) set.addAll(l);
            }
        return new ArrayList<>(set);
    }

    // ==================== 重生 / 救援 ====================
    void respawn() {
        // 找最高建筑顶部
        Building best = null;
        double bh = -1;
        for (Building b : buildings) {
            if (b.h > bh) { bh = b.h; best = b; }
        }
        if (best != null) {
            px = best.x; pz = best.z; py = best.h + 1.0;
        } else {
            px = 0; py = 60; pz = 0;
        }
        vx = vy = vz = 0;
        ropeOn = false;
        playerYaw = 0;
        onGround = false;
        wallRunning = false;
        distance = 0;
        lastPx = px; lastPz = pz;
    }

    // ==================== 主循环 ====================
    @Override public void actionPerformed(ActionEvent e) {
        long now = System.nanoTime();
        double dt = (now - lastNano) / 1_000_000_000.0;
        lastNano = now;
        if (dt > 0.05) dt = 0.05;
        if (dt <= 0) return;

        // FPS
        fpsAccum += (long)(dt * 1e9); fpsCount++;
        if (fpsAccum > 500_000_000L) {
            fps = (int) Math.round(fpsCount / (fpsAccum / 1e9));
            fpsAccum = 0; fpsCount = 0;
        }

        if (scene == Scene.PLAY) update(dt);
        repaint();
    }

    // ==================== 更新 ====================
    void update(double dt) {
        // ---------- 相机目标方向 ----------
        double mouseNX = (mouseX - W / 2.0) / (W / 2.0);
        double mouseNY = (mouseY - H / 2.0) / (H / 2.0);

        double speed = Math.sqrt(vx * vx + vz * vz);
        double baseYaw = playerYaw;
        if (speed > 2.5) baseYaw = Math.atan2(vx, vz);

        double targetYaw   = baseYaw + mouseNX * 1.15;
        double targetPitch = -mouseNY * 0.85 + clamp(-vy * 0.006, -0.35, 0.35);
        targetPitch = clamp(targetPitch, -1.15, 1.15);

        double camK = 1 - Math.exp(-dt * 6.0);
        camYaw = lerpAngle(camYaw, targetYaw, camK);
        camPitch += (targetPitch - camPitch) * camK;

        // ---------- 移动输入（相机相对） ----------
        double fwdX = Math.sin(camYaw), fwdZ = Math.cos(camYaw);
        double rightX = Math.cos(camYaw), rightZ = -Math.sin(camYaw);
        double mx = 0, mz = 0;
        if (kW) { mx += fwdX; mz += fwdZ; }
        if (kS) { mx -= fwdX; mz -= fwdZ; }
        if (kD) { mx += rightX; mz += rightZ; }
        if (kA) { mx -= rightX; mz -= rightZ; }
        double mlen = Math.hypot(mx, mz);
        if (mlen > 0.001) { mx /= mlen; mz /= mlen; }

        // ---------- 跳跃 / 墙跳 / 脱绳 ----------
        if (kSpace) {
            if (ropeOn) {
                ropeOn = false;
                vy += 2.0; // 释放小加成
                showToast("ROPE RELEASED");
            } else if (wallRunning) {
                vy = JUMP_VEL * 0.95;
                vx += wallNx * 13;
                vz += wallNz * 13;
                wallRunning = false;
                showToast("WALL JUMP");
            } else if (onGround) {
                vy = JUMP_VEL;
                onGround = false;
                showToast("JUMP");
            }
            kSpace = false;
        }

        // ---------- 物理子步进（防穿模） ----------
        double sp = Math.sqrt(vx * vx + vy * vy + vz * vz);
        int steps = (int) Math.ceil(sp * dt / 0.4);
        steps = clampInt(steps, 1, 12);
        double sdt = dt / steps;

        for (int s = 0; s < steps; s++) {
            substep(sdt, mx, mz);
        }

        // ---------- 绳索约束 ----------
        if (ropeOn) {
            double dx = px - ropeX, dy = py + 1.0 - ropeY, dz = pz - ropeZ;
            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (dist > ropeLen) {
                double nx = dx / dist, ny = dy / dist, nz = dz / dist;
                px = ropeX + nx * ropeLen;
                py = ropeY + ny * ropeLen - 1.0;
                pz = ropeZ + nz * ropeLen;
                double vr = vx * nx + vy * ny + vz * nz;
                if (vr > 0) {
                    vx -= nx * vr;
                    vy -= ny * vr;
                    vz -= nz * vr;
                }
            }
            // 收绳
            if (reeling) ropeTargetLen = Math.max(ROPE_MIN, ropeTargetLen - REEL_SPEED * dt);
            ropeTargetLen = clamp(ropeTargetLen, ROPE_MIN, ROPE_MAX);
            ropeLen += (ropeTargetLen - ropeLen) * Math.min(1, dt * 8.0);
        }

        // ---------- 墙跑计时 ----------
        if (wallRunning) {
            wallRunTime -= dt;
            if (wallRunTime <= 0 || onGround) wallRunning = false;
        }

        // ---------- 接地 ----------
        if (py <= 0.001) {
            py = 0;
            if (vy < 0) {
                if (vy < -35) {
                    showToast("HARD LANDING");
                    vy = 0;
                } else vy = 0;
            }
            onGround = true;
            wallRunning = false;
        } else onGround = false;

        // ---------- 摩擦力 / 空中阻力 ----------
        if (onGround && mlen < 0.01) {
            double f = Math.exp(-dt * 9.0);
            vx *= f; vz *= f;
        }

        // ---------- 跌落救援 ----------
        if (py < -40) {
            showToast("RESCUE — 传送至最近楼顶");
            rescue();
        }

        // ---------- 距离 & 最高速度 ----------
        double ddx = px - lastPx, ddz = pz - lastPz;
        distance += Math.hypot(ddx, ddz);
        lastPx = px; lastPz = pz;

        double kmh = Math.hypot(vx, vz) * 3.6;
        if (kmh > maxSpeed) maxSpeed = kmh;

        // ---------- 状态标签 ----------
        if (ropeOn) stateLabel = reeling ? "REELING" : "SWINGING";
        else if (wallRunning) stateLabel = "WALL RUN";
        else if (kCtrl && !onGround) stateLabel = "DIVING";
        else if (!onGround && kShift) stateLabel = "FLYING";
        else if (!onGround) stateLabel = "AIRBORNE";
        else if (Math.hypot(vx, vz) > 6) stateLabel = "RUNNING";
        else stateLabel = "IDLE";

        // ---------- 相机位置 ----------
        double camDist = 6.5 + Math.hypot(vx, vz) * 0.10;
        double tx = px - Math.sin(camYaw) * camDist;
        double tz = pz - Math.cos(camYaw) * camDist;
        double ty = py + 2.6 + vy * 0.02;
        ty = Math.max(ty, 1.2);

        camX += (tx - camX) * Math.min(1, dt * 7.0);
        camY += (ty - camY) * Math.min(1, dt * 5.0);
        camZ += (tz - camZ) * Math.min(1, dt * 7.0);

        // 相机避障
        avoidCameraCollision();

        updateCameraBasis();

        // ---------- 玩家朝向 ----------
        if (Math.hypot(vx, vz) > 1.5) {
            playerYaw = lerpAngle(playerYaw, Math.atan2(vx, vz), Math.min(1, dt * 10));
        }
        playerLean += ((-Math.hypot(vx, vz) * 0.006) - playerLean) * Math.min(1, dt * 5);
        playerAnim += dt * (2 + Math.hypot(vx, vz) * 0.6);

        // ---------- 提示过期 ----------
        if (System.currentTimeMillis() > toastUntil) toast = "";
    }

    void substep(double dt, double mx, double mz) {
        boolean swinging = ropeOn;

        // 重力
        vy += GRAVITY * dt;
        if (kCtrl && !onGround) vy -= 22.0 * dt;          // 俯冲
        if (wallRunning)      vy += 20.0 * dt;             // 墙跑抵消重力
        if (swinging)         vy += 6.0 * dt;              // 摆荡时略减重力感
        if (vy < MAX_FALL) vy = MAX_FALL;

        // 水平加速
        if (onGround) {
            double target = kShift ? SPRINT_SPEED : RUN_SPEED;
            double k = Math.min(1, dt * 12.0);
            vx += (mx * target - vx) * k;
            vz += (mz * target - vz) * k;
        } else if (swinging) {
            // 摆荡时的空中控制
            vx += mx * 14.0 * dt;
            vz += mz * 14.0 * dt;
            if (kShift) { vx += Math.sin(playerYaw) * 12 * dt; vz += Math.cos(playerYaw) * 12 * dt; }
        } else {
            vx += mx * AIR_ACCEL * dt;
            vz += mz * AIR_ACCEL * dt;
            if (kShift) { vx += mx * 22 * dt; vz += mz * 22 * dt; }
            // 水平速度上限
            double hs = Math.hypot(vx, vz);
            double maxHs = SPRINT_SPEED * 1.9;
            if (hs > maxHs) { vx *= maxHs / hs; vz *= maxHs / hs; }
        }

        // 位置积分
        px += vx * dt;
        py += vy * dt;
        pz += vz * dt;

        // 建筑碰撞
        resolveCollisions(dt);
    }

    void resolveCollisions(double dt) {
        double pMinX = px - P_RAD, pMaxX = px + P_RAD;
        double pMinY = py,           pMaxY = py + P_H;
        double pMinZ = pz - P_RAD, pMaxZ = pz + P_RAD;

        ArrayList<Building> near = nearby(px, pz, 6.0);
        boolean hitWall = false;

        for (Building b : near) {
            double bMinX = b.x - b.w / 2, bMaxX = b.x + b.w / 2;
            double bMinY = 0,             bMaxY = b.h;
            double bMinZ = b.z - b.d / 2, bMaxZ = b.z + b.d / 2;

            double oX = Math.min(pMaxX, bMaxX) - Math.max(pMinX, bMinX);
            double oY = Math.min(pMaxY, bMaxY) - Math.max(pMinY, bMinY);
            double oZ = Math.min(pMaxZ, bMaxZ) - Math.max(pMinZ, bMinZ);
            if (oX <= 0 || oY <= 0 || oZ <= 0) continue;

            // 最小穿透轴
            if (oY <= oX && oY <= oZ) {
                // 竖直解决
                double playerCenterY = py + P_H / 2;
                double bCenterY = b.h / 2;
                if (playerCenterY > bCenterY) {
                    py += oY; if (vy < 0) vy = 0;
                } else {
                    py -= oY;
                    if (vy > 0) vy = 0;
                    if (!onGround) {
                        // 落在楼顶
                        onGround = true;
                    }
                }
            } else if (oX <= oZ) {
                if (px < b.x) { px -= oX; if (vx > 0) vx = 0; }
                else          { px += oX; if (vx < 0) vx = 0; }
                hitWall = true;
                wallNx = (px < b.x) ? -1 : 1; wallNz = 0;
            } else {
                if (pz < b.z) { pz -= oZ; if (vz > 0) vz = 0; }
                else          { pz += oZ; if (vz < 0) vz = 0; }
                hitWall = true;
                wallNx = 0; wallNz = (pz < b.z) ? -1 : 1;
            }
            // 更新
            pMinX = px - P_RAD; pMaxX = px + P_RAD;
            pMinY = py;         pMaxY = py + P_H;
            pMinZ = pz - P_RAD; pMaxZ = pz + P_RAD;
        }

        // 墙跑判定
        if (hitWall && !onGround && !ropeOn) {
            double hs = Math.hypot(vx, vz);
            if (hs > 5.0) {
                if (!wallRunning) { wallRunning = true; wallRunTime = 2.2; showToast("WALL RUN"); }
                // 贴墙
                vx += wallNx * 6 * dt;
                vz += wallNz * 6 * dt;
            }
        }
    }

    void rescue() {
        ArrayList<Building> near = nearby(px, pz, 80);
        Building best = null;
        double bestD = 1e9;
        for (Building b : near) {
            double d = Math.hypot(b.x - px, b.z - pz) + Math.abs(b.h - 50) * 0.1;
            if (d < bestD) { bestD = d; best = b; }
        }
        if (best == null && !buildings.isEmpty()) best = buildings.get(rng.nextInt(buildings.size()));
        if (best != null) {
            px = best.x; pz = best.z; py = best.h + 1.0;
        } else {
            px = 0; py = 80; pz = 0;
        }
        vx = vy = vz = 0;
        ropeOn = false;
        wallRunning = false;
        lastPx = px; lastPz = pz;
    }

    void avoidCameraCollision() {
        double camPx = camX, camPy = camY, camPz = camZ;
        for (Building b : nearby(camPx, camPz, 4)) {
            double bx0 = b.x - b.w / 2, bx1 = b.x + b.w / 2;
            double bz0 = b.z - b.d / 2, bz1 = b.z + b.d / 2;
            double by0 = 0, by1 = b.h;
            if (camPx > bx0 - 0.4 && camPx < bx1 + 0.4
                    && camPz > bz0 - 0.4 && camPz < bz1 + 0.4
                    && camPy < by1 + 0.4 && camPy > by0 - 0.4) {
                // 拉近
                double dx = camPx - px, dy = camPy - (py + 1.2), dz = camPz - pz;
                double len = Math.sqrt(dx * dx + dy * dy + dz * dz);
                if (len > 0.001) {
                    camX = px + dx / len * 1.6;
                    camY = py + 1.2 + dy / len * 1.6;
                    camZ = pz + dz / len * 1.6;
                }
                break;
            }
        }
    }

    // ==================== 相机基向量 ====================
    double fx, fy, fz, rx, ry, rz, ux, uy, uz;
    double cYaw, sYaw, cPit, sPit;

    void updateCameraBasis() {
        cYaw = Math.cos(camYaw); sYaw = Math.sin(camYaw);
        cPit = Math.cos(camPitch); sPit = Math.sin(camPitch);
        fx = sYaw * cPit; fy = sPit; fz = cYaw * cPit;
        rx = cYaw; ry = 0; rz = -sYaw;
        ux = -sPit * sYaw;
        uy = cPit;
        uz = -sPit * cYaw;
    }

    // ==================== 绳索发射 ====================
    void tryAttachRope() {
        if (ropeOn) return;
        double ox = camX, oy = camY, oz = camZ;
        double best = 1e9, bx = 0, by = 0, bz = 0;
        boolean found = false;

        ArrayList<Building> near = nearby(px, pz, 130);
        for (Building b : near) {
            double[][] corners = {
                    {b.x - b.w / 2, b.h, b.z - b.d / 2},
                    {b.x + b.w / 2, b.h, b.z - b.d / 2},
                    {b.x + b.w / 2, b.h, b.z + b.d / 2},
                    {b.x - b.w / 2, b.h, b.z + b.d / 2},
                    {b.x, b.h, b.z}
            };
            for (double[] c : corners) {
                double dx = c[0] - ox, dy = c[1] - oy, dz = c[2] - oz;
                double along = dx * fx + dy * fy + dz * fz;
                if (along < 2 || along > 140) continue;
                double px_ = dx - fx * along, py_ = dy - fy * along, pz_ = dz - fz * along;
                double perp = Math.sqrt(px_ * px_ + py_ * py_ + pz_ * pz_);
                double score = perp + along * 0.06;
                if (perp < 22 && score < best) {
                    best = score; bx = c[0]; by = c[1]; bz = c[2];
                    found = true;
                }
            }
        }
        if (found) {
            ropeOn = true;
            ropeX = bx; ropeY = by; ropeZ = bz;
            double dx = px - bx, dy = (py + 1.0) - by, dz = pz - bz;
            ropeLen = Math.max(ROPE_MIN, Math.sqrt(dx * dx + dy * dy + dz * dz));
            ropeTargetLen = ropeLen;
            showToast("ROPE ATTACHED");
        } else {
            showToast("NO ANCHOR");
        }
    }

    // ==================== 渲染 ====================
    @Override protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;

        if (scene == Scene.MENU) { drawMenu(g); return; }

        boolean aa = quality != Quality.LOW;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                aa ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        drawSky(g);
        drawGround(g);
        drawCity(g);
        drawRope(g);
        drawPlayer(g);
        drawHUD(g);

        if (scene == Scene.PAUSE) drawPause(g);
    }

    // ---------- 天空 ----------
    void drawSky(Graphics2D g) {
        Color cTop, cHorizon, cBottom;
        switch (timeOfDay) {
            case DUSK:
                cTop = new Color(24, 12, 48);
                cHorizon = new Color(255, 108, 92);
                cBottom = new Color(20, 8, 26);
                break;
            case NIGHT:
                cTop = new Color(4, 6, 20);
                cHorizon = new Color(20, 30, 70);
                cBottom = new Color(2, 2, 8);
                break;
            default: // NOON
                cTop = new Color(70, 140, 220);
                cHorizon = new Color(180, 210, 240);
                cBottom = new Color(40, 60, 80);
                break;
        }
        // 修复整数除法警告：H / 2.0
        int horizonY = (int) (H / 2.0 + FOCAL * Math.tan(camPitch));
        horizonY = clampInt(horizonY, -500, H + 500);

        // 修复 topEnd > 0 始终为 true 警告，简化逻辑
        int topEnd = Math.max(1, horizonY);
        if (topEnd > 0) {
            GradientPaint gp = new GradientPaint(0, 0, cTop, 0, topEnd, cHorizon);
            g.setPaint(gp);
            g.fillRect(0, 0, W, topEnd);
        }
        if (horizonY < H) {
            GradientPaint gp = new GradientPaint(0, horizonY, cHorizon, 0, H, cBottom);
            g.setPaint(gp);
            g.fillRect(0, horizonY, W, H - horizonY);
        }

        // 太阳 / 月亮
        if (timeOfDay == TimeOfDay.DUSK) {
            double sunX = W * 0.72, sunY = horizonY - 40;
            RadialGradientPaint rg = new RadialGradientPaint(
                    new Point((int) sunX, (int) sunY), 160,
                    new float[]{0f, 0.35f, 1f},
                    new Color[]{new Color(255, 220, 160, 255),
                                new Color(255, 130, 90, 140),
                                new Color(255, 100, 60, 0)});
            g.setPaint(rg);
            g.fillOval((int) sunX - 160, (int) sunY - 160, 320, 320);
        } else if (timeOfDay == TimeOfDay.NIGHT) {
            g.setColor(new Color(220, 230, 255, 200));
            g.fillOval((int) (W * 0.78) - 30, horizonY - 180, 60, 60);
        }
    }

    // ---------- 地面 ----------
    void drawGround(Graphics2D g) {
        double R = 3000;
        double[][] corners = {
                {-R, 0, -R}, {R, 0, -R}, {R, 0, R}, {-R, 0, R}
        };
        ArrayList<double[]> cam = new ArrayList<>();
        for (double[] c : corners) cam.add(toCam(c[0], c[1], c[2]));

        Color groundCol;
        switch (timeOfDay) {
            case DUSK:  groundCol = new Color(22, 14, 28); break;
            case NIGHT: groundCol = new Color(8, 10, 18); break;
            default:    groundCol = new Color(48, 52, 60); break;
        }

        ArrayList<double[]> clipped = clipNear(cam);
        if (clipped.size() >= 3) {
            int n = clipped.size();
            int[] xs = new int[n], ys = new int[n];
            for (int i = 0; i < n; i++) {
                double[] p = clipped.get(i);
                xs[i] = (int) clamp(W / 2.0 + (p[0] / p[2]) * FOCAL, -1e5, 1e5);
                ys[i] = (int) clamp(H / 2.0 - (p[1] / p[2]) * FOCAL, -1e5, 1e5);
            }
            g.setColor(groundCol);
            g.fillPolygon(xs, ys, n);
        }

        // 网格线
        if (quality != Quality.LOW) {
            Color lineCol = new Color(80, 200, 255, 40);
            int spacing = 40;
            int range = 320;
            int cx = (int) (Math.floor(camX / spacing) * spacing);
            int cz = (int) (Math.floor(camZ / spacing) * spacing);
            g.setStroke(new BasicStroke(1.0f));
            for (int i = -range; i <= range; i += spacing) {
                drawGroundLine(g, cx + i, camZ - range, cx + i, camZ + range, lineCol);
                drawGroundLine(g, camX - range, cz + i, camX + range, cz + i, lineCol);
            }
        }
    }

    void drawGroundLine(Graphics2D g, double x1, double z1, double x2, double z2, Color col) {
        double w = 0.5;
        double dx = x2 - x1, dz = z2 - z1;
        double len = Math.hypot(dx, dz);
        if (len < 0.001) return;
        double nx = -dz / len * w, nz = dx / len * w;
        double[][] quad = {
                {x1 + nx, 0.05, z1 + nz},
                {x2 + nx, 0.05, z2 + nz},
                {x2 - nx, 0.05, z2 - nz},
                {x1 - nx, 0.05, z1 - nz}
        };
        ArrayList<double[]> cam = new ArrayList<>();
        for (double[] c : quad) cam.add(toCam(c[0], c[1], c[2]));
        ArrayList<double[]> cl = clipNear(cam);
        if (cl.size() < 3) return;
        int n = cl.size();
        int[] xs = new int[n], ys = new int[n];
        for (int i = 0; i < n; i++) {
            double[] p = cl.get(i);
            xs[i] = (int) clamp(W / 2.0 + (p[0] / p[2]) * FOCAL, -1e5, 1e5);
            ys[i] = (int) clamp(H / 2.0 - (p[1] / p[2]) * FOCAL, -1e5, 1e5);
        }
        g.setColor(col);
        g.fillPolygon(xs, ys, n);
    }

    // ---------- 城市 ----------
    void drawCity(Graphics2D g) {
        ArrayList<Building> list = new ArrayList<>();
        double cullDist = quality == Quality.LOW ? 180 : (quality == Quality.MEDIUM ? 300 : 450);
        for (Building b : buildings) {
            double dx = b.x - camX, dz = b.z - camZ;
            double d = Math.hypot(dx, dz);
            if (d > cullDist) continue;
            // 视锥剔除（粗）
            double dot = dx * fx + dz * fz;
            if (dot < -b.w - b.d) continue;
            list.add(b);
        }
        list.sort((a, b) -> {
            double da = (a.x - camX) * (a.x - camX) + (a.z - camZ) * (a.z - camZ);
            double db = (b.x - camX) * (b.x - camX) + (b.z - camZ) * (b.z - camZ);
            return Double.compare(db, da);
        });

        for (Building b : list) drawBuilding(g, b);
    }

    void drawBuilding(Graphics2D g, Building b) {
        double x0 = b.x - b.w / 2, x1 = b.x + b.w / 2;
        double z0 = b.z - b.d / 2, z1 = b.z + b.d / 2;
        double y0 = 0, y1 = b.h;

        // 相机空间 8 个顶点
        double[][] cs = new double[8][];
        cs[0] = toCam(x0, y0, z0);
        cs[1] = toCam(x1, y0, z0);
        cs[2] = toCam(x1, y0, z1);
        cs[3] = toCam(x0, y0, z1);
        cs[4] = toCam(x0, y1, z0);
        cs[5] = toCam(x1, y1, z0);
        cs[6] = toCam(x1, y1, z1);
        cs[7] = toCam(x0, y1, z1);

        // 光照（黄昏暖光从 -X / +Z 方向来）
        double lx = -0.6, ly = 0.7, lz = 0.4;

        // 顶面
        double[] topN = {0, 1, 0};
        if (vis(b, 4, 5, 6, 7, topN)) {
            Color top = baseColor(b, 1.0f, 1.35f);
            drawPoly(g, cs, new int[]{4, 5, 6, 7}, top, null, 0);
        }
        // 侧面 -Z
        double[] nZneg = {0, 0, -1};
        if (vis(b, 0, 1, 5, 4, nZneg)) {
            Color c = baseColor(b, shade(lx, ly, lz, 0, 0, -1), 1.0f);
            drawPoly(g, cs, new int[]{0, 1, 5, 4}, c, null, 0);
            drawNeonVertical(g, cs, 0, 4, b);
            drawNeonVertical(g, cs, 1, 5, b);
        }
        // 侧面 +Z
        double[] nZpos = {0, 0, 1};
        if (vis(b, 3, 2, 6, 7, nZpos)) {
            Color c = baseColor(b, shade(lx, ly, lz, 0, 0, 1), 1.0f);
            drawPoly(g, cs, new int[]{3, 2, 6, 7}, c, null, 0);
            drawNeonVertical(g, cs, 3, 7, b);
            drawNeonVertical(g, cs, 2, 6, b);
        }
        // 侧面 -X
        double[] nXneg = {-1, 0, 0};
        if (vis(b, 0, 3, 7, 4, nXneg)) {
            Color c = baseColor(b, shade(lx, ly, lz, -1, 0, 0), 1.0f);
            drawPoly(g, cs, new int[]{0, 3, 7, 4}, c, null, 0);
            drawNeonVertical(g, cs, 0, 4, b);
            drawNeonVertical(g, cs, 3, 7, b);
        }
        // 侧面 +X
        double[] nXpos = {1, 0, 0};
        if (vis(b, 1, 2, 6, 5, nXpos)) {
            Color c = baseColor(b, shade(lx, ly, lz, 1, 0, 0), 1.0f);
            drawPoly(g, cs, new int[]{1, 2, 6, 5}, c, null, 0);
            drawNeonVertical(g, cs, 1, 5, b);
            drawNeonVertical(g, cs, 2, 6, b);
        }

        // 楼顶霓虹描边
        if (b.neonTop) {
            Color neon = neonColor(b);
            g.setStroke(new BasicStroke(quality == Quality.LOW ? 1.5f : 2.2f));
            drawSeg3D(g, cs, 4, 5, neon);
            drawSeg3D(g, cs, 5, 6, neon);
            drawSeg3D(g, cs, 6, 7, neon);
            drawSeg3D(g, cs, 7, 4, neon);
        }
    }

    double shade(double lx, double ly, double lz, double nx, double ny, double nz) {
        double d = lx * nx + ly * ny + lz * nz;
        return 0.35 + 0.65 * Math.max(0, d);
    }

    // 修复编译错误：将参数类型从 float 改为 double，以匹配 shade() 返回值
    Color baseColor(Building b, double mul, float bright) {
        // 修复警告：局部变量 base 冗余，直接 return
        return Color.getHSBColor(b.hue, 0.45f, (float) (0.15 * bright * mul));
    }

    Color neonColor(Building b) {
        return Color.getHSBColor(b.hue > 0.72f ? 0.92f : 0.52f, 0.9f, 1.0f);
    }

    boolean vis(Building b, int a, int c, int d, int e, double[] n) {
        // 面中心
        double cx = (cornerX(b, a) + cornerX(b, d)) / 2;
        double cy = (cornerY(b, a) + cornerY(b, c)) / 2;
        double cz = (cornerZ(b, a) + cornerZ(b, d)) / 2;
        return (camX - cx) * n[0] + (camY - cy) * n[1] + (camZ - cz) * n[2] > 0;
    }

    double cornerX(Building b, int i) {
        switch (i) {
            case 0: case 4: return b.x - b.w / 2;
            case 1: case 5: return b.x + b.w / 2;
            case 2: case 6: return b.x + b.w / 2;
            default:        return b.x - b.w / 2;
        }
    }
    double cornerY(Building b, int i) { return i >= 4 ? b.h : 0; }
    double cornerZ(Building b, int i) {
        switch (i) {
            case 0: case 1: case 4: case 5: return b.z - b.d / 2;
            default:                        return b.z + b.d / 2;
        }
    }

    void drawNeonVertical(Graphics2D g, double[][] cs, int a, int b, Building bb) {
        if (quality == Quality.LOW) return;
        double dist = Math.hypot(bb.x - camX, bb.z - camZ);
        if (dist > 200) return;
        Color neon = new Color(neonColor(bb).getRed(), neonColor(bb).getGreen(), neonColor(bb).getBlue(), 90);
        g.setStroke(new BasicStroke(1.0f));
        drawSeg3D(g, cs, a, b, neon);
    }

    void drawSeg3D(Graphics2D g, double[][] cs, int a, int b, Color col) {
        double[] pa = cs[a], pb = cs[b];
        if (pa[2] < NEAR || pb[2] < NEAR) return;
        int x1 = (int) clamp(W / 2.0 + (pa[0] / pa[2]) * FOCAL, -1e5, 1e5);
        int y1 = (int) clamp(H / 2.0 - (pa[1] / pa[2]) * FOCAL, -1e5, 1e5);
        int x2 = (int) clamp(W / 2.0 + (pb[0] / pb[2]) * FOCAL, -1e5, 1e5);
        int y2 = (int) clamp(H / 2.0 - (pb[1] / pb[2]) * FOCAL, -1e5, 1e5);
        g.setColor(col);
        g.drawLine(x1, y1, x2, y2);
    }

    void drawPoly(Graphics2D g, double[][] cs, int[] idx, Color fill, Color stroke, float sw) {
        ArrayList<double[]> poly = new ArrayList<>();
        for (int i : idx) poly.add(cs[i]);
        ArrayList<double[]> cl = clipNear(poly);
        if (cl.size() < 3) return;
        int n = cl.size();
        int[] xs = new int[n], ys = new int[n];
        for (int i = 0; i < n; i++) {
            double[] p = cl.get(i);
            xs[i] = (int) clamp(W / 2.0 + (p[0] / p[2]) * FOCAL, -1e5, 1e5);
            ys[i] = (int) clamp(H / 2.0 - (p[1] / p[2]) * FOCAL, -1e5, 1e5);
        }
        if (fill != null) { g.setColor(fill); g.fillPolygon(xs, ys, n); }
        if (stroke != null) {
            g.setColor(stroke);
            g.setStroke(new BasicStroke(sw));
            g.drawPolygon(xs, ys, n);
        }
    }

    // ---------- 绳索 ----------
    void drawRope(Graphics2D g) {
        if (!ropeOn) return;
        double[] a = toCam(px, py + 1.25, pz);
        double[] b = toCam(ropeX, ropeY, ropeZ);
        if (a[2] < NEAR || b[2] < NEAR) return;
        int x1 = (int) (W / 2.0 + (a[0] / a[2]) * FOCAL);
        int y1 = (int) (H / 2.0 - (a[1] / a[2]) * FOCAL);
        int x2 = (int) (W / 2.0 + (b[0] / b[2]) * FOCAL);
        int y2 = (int) (H / 2.0 - (b[1] / b[2]) * FOCAL);

        // 外发光
        g.setStroke(new BasicStroke(6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(new Color(0, 255, 255, 60));
        g.drawLine(x1, y1, x2, y2);
        g.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(new Color(200, 255, 255, 220));
        g.drawLine(x1, y1, x2, y2);

        // 锚点
        g.setColor(new Color(0, 255, 220, 200));
        g.fillOval(x2 - 6, y2 - 6, 12, 12);
        g.setColor(new Color(0, 255, 220, 60));
        g.fillOval(x2 - 14, y2 - 14, 28, 28);
    }

    // ---------- 玩家 ----------
    void drawPlayer(Graphics2D g) {
        double cy = py + 0.95;
        double[] c = toCam(px, cy, pz);
        if (c[2] < NEAR) return;
        double dist = c[2];
        double scrX = W / 2.0 + (c[0] / dist) * FOCAL;
        double scrY = H / 2.0 - (c[1] / dist) * FOCAL;
        double scale = FOCAL / dist;

        double bodyH = 1.9 * scale;
        double bodyW = 0.55 * scale;
        double headR = 0.24 * scale;

        int bx = (int) scrX;
        int by = (int) scrY;

        Color suit = femaleChar ? new Color(255, 60, 160) : new Color(0, 220, 255);
        Color suitDark = femaleChar ? new Color(120, 20, 80) : new Color(0, 90, 120);
        Color skin = new Color(255, 210, 180);

        // 阴影
        g.setColor(new Color(0, 0, 0, 80));
        g.fillOval(bx - (int)(bodyW * 0.9), by + (int)(bodyH * 0.42),
                   (int)(bodyW * 1.8), (int)(bodyW * 0.5));

        // 腿
        g.setColor(suitDark);
        int legLen = (int)(bodyH * 0.45);
        g.setStroke(new BasicStroke(Math.max(2f, (float)(bodyW * 0.28f)),
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        double legSwing = Math.sin(playerAnim * 2.0) * bodyW * 0.5;
        g.drawLine(bx, by + (int)(bodyH * 0.05), bx - (int)legSwing, by + legLen);
        g.drawLine(bx, by + (int)(bodyH * 0.05), bx + (int)legSwing, by + legLen);

        // 躯干
        g.setColor(suit);
        g.fillRoundRect(bx - (int)(bodyW * 0.42), by - (int)(bodyH * 0.22),
                        (int)(bodyW * 0.84), (int)(bodyH * 0.42), (int)(bodyW * 0.4), (int)(bodyW * 0.4));

        // 手臂
        double armSwing = Math.sin(playerAnim * 2.0 + 1.2) * bodyW * 0.6;
        double armRaise = ropeOn ? bodyH * 0.30 : 0;
        g.setStroke(new BasicStroke(Math.max(2f, (float)(bodyW * 0.22f)),
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(suitDark);
        g.drawLine(bx - (int)(bodyW * 0.3), by - (int)(bodyH * 0.1),
                   bx - (int)(bodyW * 0.3 + armSwing), by + (int)(bodyH * 0.1 - armRaise));
        g.drawLine(bx + (int)(bodyW * 0.3), by - (int)(bodyH * 0.1),
                   bx + (int)(bodyW * 0.3 - armSwing), by + (int)(bodyH * 0.1 - armRaise));

        // 头
        g.setColor(skin);
        g.fillOval(bx - (int) headR, by - (int)(bodyH * 0.34) - (int) headR,
                   (int)(headR * 2), (int)(headR * 2));

        // 头盔/护目镜
        g.setColor(suit);
        g.fillArc(bx - (int) headR, by - (int)(bodyH * 0.34) - (int) headR,
                  (int)(headR * 2), (int)(headR * 2), 0, 180);
        g.setColor(new Color(255, 255, 255, 200));
        g.fillRect(bx - (int)(headR * 0.7), by - (int)(bodyH * 0.34) - (int)(headR * 0.2),
                   (int)(headR * 1.4), (int)(headR * 0.35));

        // 速度拖尾
        double spd = Math.hypot(vx, vz);
        if (spd > 15 && quality != Quality.LOW) {
            g.setColor(new Color(suit.getRed(), suit.getGreen(), suit.getBlue(), 50));
            g.setStroke(new BasicStroke(Math.max(1f, (float)(bodyW * 0.5f)),
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (int i = 1; i <= 4; i++) {
                int tx = bx - (int)(vx * scale * 0.06 * i);
                int ty = by - (int)(vy * scale * 0.06 * i);
                g.drawLine(bx, by, tx, ty);
            }
        }
    }

    // ---------- HUD ----------
    void drawHUD(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Font mono = new Font("Consolas", Font.BOLD, 14);
        Font monoBig = new Font("Consolas", Font.BOLD, 26);
        Font monoSm = new Font("Consolas", Font.PLAIN, 12);

        double speed = Math.hypot(vx, vz) * 3.6;

        // 左上：速度
        g.setFont(monoSm);
        g.setColor(new Color(120, 220, 255));
        g.drawString("SPEED", 24, 36);
        g.setFont(monoBig);
        g.setColor(new Color(0, 255, 230));
        g.drawString(String.format("%.0f", speed), 24, 68);
        g.setFont(monoSm);
        g.setColor(new Color(120, 220, 255));
        g.drawString("km/h", 105, 68);

        // 速度条
        int barW = 220;
        g.setColor(new Color(0, 255, 230, 40));
        g.fillRect(24, 80, barW, 6);
        double ratio = Math.min(1, speed / 200.0);
        g.setColor(new Color(0, 255, 230, 220));
        g.fillRect(24, 80, (int)(barW * ratio), 6);
        g.setColor(new Color(255, 60, 160, 220));
        g.fillRect(24, 80, (int)(barW * Math.min(1, speed / 320.0)), 3);

        // 高度
        g.setFont(monoSm);
        g.setColor(new Color(120, 220, 255));
        g.drawString("ALT", 24, 112);
        g.setFont(mono);
        g.setColor(Color.WHITE);
        g.drawString(String.format("%.0f m", py), 60, 112);

        // 右上
        g.setFont(monoSm);
        g.setColor(new Color(120, 220, 255));
        int rx = W - 24;
        g.drawString("DIST", rx - 110, 36);
        g.setFont(mono);
        g.setColor(Color.WHITE);
        g.drawString(String.format("%.0f m", distance), rx - 60, 36);

        g.setFont(monoSm);
        g.setColor(new Color(120, 220, 255));
        g.drawString("BEST", rx - 110, 60);
        g.setFont(mono);
        g.setColor(new Color(255, 220, 100));
        g.drawString(String.format("%.0f km/h", maxSpeed), rx - 60, 60);

        g.setFont(monoSm);
        g.setColor(new Color(120, 220, 255));
        g.drawString("FPS", rx - 110, 84);
        g.setColor(fps > 50 ? new Color(120, 255, 150) : (fps > 30 ? Color.YELLOW : Color.RED));
        g.setFont(mono);
        g.drawString("" + fps, rx - 60, 84);

        // 状态芯片
        String label = stateLabel;
        Font chipFont = new Font("Consolas", Font.BOLD, 16);
        g.setFont(chipFont);
        FontMetrics fm = g.getFontMetrics();
        int cw = fm.stringWidth(label) + 40;
        int cx = W / 2 - cw / 2;
        int cy = H - 60;
        g.setColor(new Color(0, 20, 30, 180));
        g.fillRoundRect(cx, cy, cw, 34, 12, 12);
        g.setColor(new Color(0, 255, 230, 200));
        g.setStroke(new BasicStroke(1.5f));
        g.drawRoundRect(cx, cy, cw, 34, 12, 12);
        g.setColor(new Color(0, 255, 230));
        g.drawString(label, cx + 20, cy + 23);

        // 绳长
        if (ropeOn) {
            g.setFont(monoSm);
            g.setColor(new Color(200, 255, 255, 220));
            g.drawString(String.format("LINE %.1f m", ropeLen), W / 2 - 40, cy - 14);
        }

        // 十字准星
        int chx = W / 2, chy = H / 2;
        g.setColor(new Color(0, 255, 230, 180));
        g.setStroke(new BasicStroke(1.5f));
        g.drawLine(chx - 10, chy, chx - 3, chy);
        g.drawLine(chx + 3, chy, chx + 10, chy);
        g.drawLine(chx, chy - 10, chx, chy - 3);
        g.drawLine(chx, chy + 3, chx, chy + 10);
        g.setColor(new Color(0, 255, 230, 60));
        g.drawOval(chx - 3, chy - 3, 6, 6);

        // 提示
        if (!toast.isEmpty()) {
            g.setFont(new Font("Consolas", Font.BOLD, 18));
            FontMetrics fm2 = g.getFontMetrics();
            int tw = fm2.stringWidth(toast) + 32;
            int tx = W / 2 - tw / 2;
            int ty = H / 2 - 120;
            long remain = toastUntil - System.currentTimeMillis();
            int alpha = (int) Math.min(220, remain);
            g.setColor(new Color(0, 20, 30, Math.max(0, Math.min(200, alpha))));
            g.fillRoundRect(tx, ty, tw, 32, 10, 10);
            g.setColor(new Color(0, 255, 230, Math.max(0, Math.min(255, alpha + 35))));
            g.drawString(toast, tx + 16, ty + 22);
        }
    }

    // ---------- 菜单 ----------
    void drawMenu(Graphics2D g) {
        // 背景
        GradientPaint bg = new GradientPaint(0, 0, new Color(8, 4, 18), 0, H, new Color(30, 6, 40));
        g.setPaint(bg);
        g.fillRect(0, 0, W, H);

        // 扫描线
        g.setColor(new Color(0, 255, 230, 8));
        for (int y = 0; y < H; y += 3) g.drawLine(0, y, W, y);

        // 标题
        g.setFont(new Font("Consolas", Font.BOLD, 52));
        g.setColor(new Color(255, 40, 160));
        g.drawString("CYBERPUNK MEGAPOLIS", 90, 130);
        g.setColor(new Color(0, 255, 230));
        g.drawString("CYBERPUNK MEGAPOLIS", 88, 128);

        g.setFont(new Font("Consolas", Font.BOLD, 22));
        g.setColor(new Color(180, 220, 255));
        g.drawString("WEB-SWING EDITION", 92, 168);

        // 角色选择
        g.setFont(new Font("Consolas", Font.BOLD, 16));
        g.setColor(new Color(120, 220, 255));
        g.drawString("角色 / OPERATOR", 370, 290);

        drawCharCard(g, rCharMale, "VEX", "均衡 · 高跳跃", new Color(0, 220, 255), !femaleChar);
        drawCharCard(g, rCharFem, "NYX", "轻盈 · 高速度", new Color(255, 60, 160), femaleChar);

        // 画质
        g.setColor(new Color(120, 220, 255));
        g.drawString("画质 / QUALITY", 370, 480);
        drawSeg(g, rQH, "HIGH",   quality == Quality.HIGH);
        drawSeg(g, rQM, "MEDIUM", quality == Quality.MEDIUM);
        drawSeg(g, rQL, "LOW",    quality == Quality.LOW);

        // 时段
        g.setColor(new Color(120, 220, 255));
        g.drawString("时段 / TIME OF DAY", 370, 560);
        drawSeg(g, rTD, "黄昏", timeOfDay == TimeOfDay.DUSK);
        drawSeg(g, rTN, "午夜", timeOfDay == TimeOfDay.NIGHT);
        drawSeg(g, rTNo,"正午", timeOfDay == TimeOfDay.NOON);

        // 开始按钮
        g.setColor(new Color(0, 255, 230, 30));
        g.fillRoundRect(rStart.x, rStart.y, rStart.width, rStart.height, 14, 14);
        g.setColor(new Color(0, 255, 230));
        g.setStroke(new BasicStroke(2f));
        g.drawRoundRect(rStart.x, rStart.y, rStart.width, rStart.height, 14, 14);
        g.setFont(new Font("Consolas", Font.BOLD, 22));
        FontMetrics fm = g.getFontMetrics();
        String st = "开始摆荡 / START SWING";
        g.drawString(st, rStart.x + (rStart.width - fm.stringWidth(st)) / 2, rStart.y + 38);

        // 操作说明
        g.setFont(new Font("Consolas", Font.PLAIN, 13));
        g.setColor(new Color(160, 190, 220));
        String[] ctr = {
                "W A S D   移动 / 空中姿态",
                "SHIFT     冲刺 / 空中飞掠",
                "SPACE     跳跃 / 脱绳 / 墙跳",
                "鼠标左键   发射蛛丝并挂接",
                "鼠标右键   收绳拉近",
                "滚轮       调节绳长",
                "CTRL      俯冲",
                "ESC       暂停"
        };
        for (int i = 0; i < ctr.length; i++) {
            g.drawString(ctr[i], 900, 300 + i * 26);
        }
    }

    void drawCharCard(Graphics2D g, Rectangle r, String name, String desc, Color neon, boolean selected) {
        g.setColor(selected ? new Color(0, 30, 40, 220) : new Color(10, 15, 25, 200));
        g.fillRoundRect(r.x, r.y, r.width, r.height, 14, 14);
        g.setColor(selected ? neon : new Color(70, 100, 130));
        g.setStroke(new BasicStroke(selected ? 2.5f : 1.2f));
        g.drawRoundRect(r.x, r.y, r.width, r.height, 14, 14);

        // 头像
        int ax = r.x + r.width / 2;
        int ay = r.y + 56;
        g.setColor(neon);
        g.fillOval(ax - 22, ay - 30, 44, 44);
        g.setColor(new Color(15, 20, 32));
        g.fillOval(ax - 14, ay - 22, 28, 28);
        g.setColor(neon);
        g.fillRect(ax - 12, ay - 14, 24, 6);

        g.setFont(new Font("Consolas", Font.BOLD, 22));
        FontMetrics fm = g.getFontMetrics();
        g.setColor(Color.WHITE);
        g.drawString(name, ax - fm.stringWidth(name) / 2, r.y + 108);

        g.setFont(new Font("Consolas", Font.PLAIN, 12));
        fm = g.getFontMetrics();
        g.setColor(new Color(170, 200, 230));
        g.drawString(desc, ax - fm.stringWidth(desc) / 2, r.y + 130);
    }

    void drawSeg(Graphics2D g, Rectangle r, String text, boolean active) {
        g.setColor(active ? new Color(0, 60, 80, 220) : new Color(15, 20, 32, 200));
        g.fillRoundRect(r.x, r.y, r.width, r.height, 8, 8);
        g.setColor(active ? new Color(0, 255, 230) : new Color(70, 100, 130));
        g.setStroke(new BasicStroke(active ? 2f : 1f));
        g.drawRoundRect(r.x, r.y, r.width, r.height, 8, 8);

        g.setFont(new Font("Consolas", Font.BOLD, 13));
        FontMetrics fm = g.getFontMetrics();
        g.setColor(active ? new Color(0, 255, 230) : new Color(150, 180, 210));
        g.drawString(text, r.x + (r.width - fm.stringWidth(text)) / 2, r.y + 25);
    }

    void drawPause(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, W, H);

        g.setFont(new Font("Consolas", Font.BOLD, 42));
        g.setColor(new Color(0, 255, 230));
        String t = "已暂停 / PAUSED";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(t, W / 2 - fm.stringWidth(t) / 2, 220);

        g.setColor(new Color(0, 255, 230, 30));
        g.fillRoundRect(rResume.x, rResume.y, rResume.width, rResume.height, 12, 12);
        g.setColor(new Color(0, 255, 230));
        g.setStroke(new BasicStroke(2f));
        g.drawRoundRect(rResume.x, rResume.y, rResume.width, rResume.height, 12, 12);
        g.setFont(new Font("Consolas", Font.BOLD, 20));
        fm = g.getFontMetrics();
        String s1 = "继续 / RESUME";
        g.drawString(s1, rResume.x + (rResume.width - fm.stringWidth(s1)) / 2, rResume.y + 37);

        g.setColor(new Color(255, 60, 160, 30));
        g.fillRoundRect(rMenu.x, rMenu.y, rMenu.width, rMenu.height, 12, 12);
        g.setColor(new Color(255, 60, 160));
        g.drawRoundRect(rMenu.x, rMenu.y, rMenu.width, rMenu.height, 12, 12);
        g.setColor(new Color(255, 200, 230));
        String s2 = "返回菜单 / MENU";
        g.drawString(s2, rMenu.x + (rMenu.width - fm.stringWidth(s2)) / 2, rMenu.y + 37);
    }

    // ==================== 投影工具 ====================
    double[] toCam(double wx, double wy, double wz) {
        double dx = wx - camX, dy = wy - camY, dz = wz - camZ;
        return new double[]{
                dx * rx + dy * ry + dz * rz,
                dx * ux + dy * uy + dz * uz,
                dx * fx + dy * fy + dz * fz
        };
    }

    ArrayList<double[]> clipNear(ArrayList<double[]> poly) {
        ArrayList<double[]> out = new ArrayList<>();
        int n = poly.size();
        if (n == 0) return out;
        for (int i = 0; i < n; i++) {
            double[] a = poly.get(i);
            double[] b = poly.get((i + 1) % n);
            boolean ain = a[2] > NEAR;
            boolean bin = b[2] > NEAR;
            if (ain) out.add(a);
            if (ain != bin) {
                double t = (NEAR - a[2]) / (b[2] - a[2]);
                out.add(new double[]{
                        a[0] + (b[0] - a[0]) * t,
                        a[1] + (b[1] - a[1]) * t,
                        NEAR
                });
            }
        }
        return out;
    }

    // ==================== 工具 ====================
    static double clamp(double v, double a, double b) { return v < a ? a : (v > b ? b : v); }
    static int clampInt(int v, int a, int b) { return v < a ? a : (v > b ? b : v); }

    static double lerpAngle(double a, double b, double t) {
        double d = b - a;
        while (d > Math.PI) d -= 2 * Math.PI;
        while (d < -Math.PI) d += 2 * Math.PI;
        return a + d * t;
    }

    void showToast(String s) {
        toast = s;
        toastUntil = System.currentTimeMillis() + 1400;
    }

    // ==================== 输入 ====================
    @Override public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode();
        switch (k) {
            case KeyEvent.VK_W: kW = true; break;
            case KeyEvent.VK_A: kA = true; break;
            case KeyEvent.VK_S: kS = true; break;
            case KeyEvent.VK_D: kD = true; break;
            case KeyEvent.VK_SHIFT: kShift = true; break;
            case KeyEvent.VK_SPACE: kSpace = true; break;
            case KeyEvent.VK_CONTROL: kCtrl = true; break;
            case KeyEvent.VK_C: kCtrl = true; break;
            case KeyEvent.VK_ESCAPE:
                if (scene == Scene.PLAY) scene = Scene.PAUSE;
                else if (scene == Scene.PAUSE) scene = Scene.PLAY;
                break;
        }
    }

    @Override public void keyReleased(KeyEvent e) {
        int k = e.getKeyCode();
        switch (k) {
            case KeyEvent.VK_W: kW = false; break;
            case KeyEvent.VK_A: kA = false; break;
            case KeyEvent.VK_S: kS = false; break;
            case KeyEvent.VK_D: kD = false; break;
            case KeyEvent.VK_SHIFT: kShift = false; break;
            case KeyEvent.VK_SPACE: kSpace = false; break;
            case KeyEvent.VK_CONTROL: kCtrl = false; break;
            case KeyEvent.VK_C: kCtrl = false; break;
        }
    }

    @Override public void keyTyped(KeyEvent e) {}

    @Override public void mousePressed(MouseEvent e) {
        if (scene == Scene.MENU) { handleMenuClick(e); return; }
        if (scene == Scene.PAUSE) { handlePauseClick(e); return; }

        if (SwingUtilities.isLeftMouseButton(e)) {
            mouseLDown = true;
            tryAttachRope();
        }
        if (SwingUtilities.isRightMouseButton(e)) {
            mouseRDown = true;
            reeling = true;
        }
    }

    @Override public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) mouseLDown = false;
        if (SwingUtilities.isRightMouseButton(e)) {
            mouseRDown = false;
            reeling = false;
        }
    }

    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    @Override public void mouseMoved(MouseEvent e) {
        mouseX = e.getX(); mouseY = e.getY();
    }

    @Override public void mouseDragged(MouseEvent e) {
        mouseX = e.getX(); mouseY = e.getY();
    }

    @Override public void mouseWheelMoved(MouseWheelEvent e) {
        int rot = e.getWheelRotation();
        if (ropeOn) {
            ropeTargetLen = clamp(ropeTargetLen + rot * 3.0, ROPE_MIN, ROPE_MAX);
        } else {
            // 未挂绳时滚轮调整镜头高度/距离
            camPitch = clamp(camPitch + rot * 0.03, -1.15, 1.15);
        }
    }

    void handleMenuClick(MouseEvent e) {
        int mx = e.getX(), my = e.getY();
        if (rCharMale.contains(mx, my)) femaleChar = false;
        else if (rCharFem.contains(mx, my)) femaleChar = true;
        else if (rQH.contains(mx, my)) quality = Quality.HIGH;
        else if (rQM.contains(mx, my)) quality = Quality.MEDIUM;
        else if (rQL.contains(mx, my)) quality = Quality.LOW;
        else if (rTD.contains(mx, my)) timeOfDay = TimeOfDay.DUSK;
        else if (rTN.contains(mx, my)) timeOfDay = TimeOfDay.NIGHT;
        else if (rTNo.contains(mx, my)) timeOfDay = TimeOfDay.NOON;
        else if (rStart.contains(mx, my)) {
            scene = Scene.PLAY;
            respawn();
            updateCameraBasis();
            camYaw = 0; camPitch = 0;
            camX = px; camY = py + 3; camZ = pz - 8;
            requestFocusInWindow();
            lastNano = System.nanoTime();
        }
    }

    void handlePauseClick(MouseEvent e) {
        int mx = e.getX(), my = e.getY();
        if (rResume.contains(mx, my)) {
            scene = Scene.PLAY;
            lastNano = System.nanoTime();
            requestFocusInWindow();
        } else if (rMenu.contains(mx, my)) {
            scene = Scene.MENU;
        }
    }
}