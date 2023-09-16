package me.xmrvizzy.skyblocker.skyblock.cooldown;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.util.HashMap;
import java.util.Map;

public class ItemCooldowns {
    private static final String JUNGLE_AXE_ID = "JUNGLE_AXE";
    private static final Map<String, ItemCooldownEntry> itemCooldowns = new HashMap<>();

    public static void init() {
    }

    public static void afterBlockBreak(PlayerEntity player) {
        if (getItemId(player.getMainHandStack()).equals(JUNGLE_AXE_ID)) {
            if (!isItemOnCooldown(JUNGLE_AXE_ID)) {
                itemCooldowns.put(JUNGLE_AXE_ID, new ItemCooldownEntry(2000));
            }
        }
    }

    public static boolean isItemOnCooldown(ItemStack item) {
        return isItemOnCooldown(getItemId(item));
    }

    private static boolean isItemOnCooldown(String itemId) {
        if (itemCooldowns.containsKey(itemId)) {
            ItemCooldownEntry cooldownEntry = itemCooldowns.get(itemId);
            if (cooldownEntry.isOnCooldown()) {
                return true;
            }
            else {
                itemCooldowns.remove(cooldownEntry);
                return false;
            }
        }
        return false;
    }

    public static ItemCooldownEntry getItemCooldownEntry(ItemStack item) {
        return itemCooldowns.get(getItemId(item));
    }

    private static String getItemId(ItemStack item) {
        NbtCompound nbt = item.getNbt();
        if (nbt != null && nbt.contains("ExtraAttributes")) {
            NbtCompound extraAttributes = nbt.getCompound("ExtraAttributes");
            if (extraAttributes.contains("id")) {
                return extraAttributes.getString("id");
            }
        }

        return null;
    }
}
