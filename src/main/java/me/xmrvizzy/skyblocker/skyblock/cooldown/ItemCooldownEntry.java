package me.xmrvizzy.skyblocker.skyblock.cooldown;

import net.minecraft.util.math.MathHelper;

public class ItemCooldownEntry {
    private final long cooldown;
    private final long startTime;

    public ItemCooldownEntry(int cooldown) {
        this.cooldown = cooldown;
        this.startTime = System.currentTimeMillis();
    }

    public boolean isOnCooldown() {
        return System.currentTimeMillis() < (this.startTime + this.cooldown);
    }

    public int getRemainingTime() {
        long time = (this.startTime + this.cooldown) - System.currentTimeMillis();
        return time <= 0 ? 0 : (int) time;
    }

    public float getRemainingCooldownPercent() {
        return this.isOnCooldown() ? ((float) this.getRemainingTime()) / ((float) cooldown) : 0.0F;
    }

    public int getItemBarStep() {
        return Math.round(13.0F - getRemainingCooldownPercent() * 13.0F);
    }

    public int getItemBarColor() {
        float f = Math.max(0.0F, ((float) this.cooldown - (float) this.getRemainingTime()) / (float) this.cooldown);
        return MathHelper.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }
}
