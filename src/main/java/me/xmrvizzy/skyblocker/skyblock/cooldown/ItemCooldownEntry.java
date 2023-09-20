package me.xmrvizzy.skyblocker.skyblock.cooldown;

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
}
