package me.xmrvizzy.skyblocker.skyblock.cooldown;

import me.xmrvizzy.skyblocker.events.ClientPlayerBlockBreakEvent;
import me.xmrvizzy.skyblocker.utils.ItemUtils;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ItemCooldowns {
    private static final String JUNGLE_AXE_ID = "JUNGLE_AXE";
    private static final String TREECAPITATOR_ID = "TREECAPITATOR_AXE";
    private static final String GRAPPLING_HOOK_ID = "GRAPPLING_HOOK";
    private static final List<String> BAT_ARMOR_IDS = Arrays.asList("BAT_PERSON_LEGGINGS", "BAT_PERSON_CHESTPLATE", "BAT_PERSON_HELMET");

    private static final Map<String, ItemCooldownEntry> itemCooldowns = new HashMap<>();

    public static void init() {
        ClientPlayerBlockBreakEvent.AFTER.register(ItemCooldowns::afterBlockBreak);
        UseItemCallback.EVENT.register(ItemCooldowns::onItemInteract);
    }

    public static void afterBlockBreak(BlockPos pos, PlayerEntity player) {
        String usedItemId = ItemUtils.getItemId(player.getMainHandStack());
        if (usedItemId == null) return;

        if (usedItemId.equals(JUNGLE_AXE_ID)) {
            if (!isItemOnCooldown(JUNGLE_AXE_ID)) {
                itemCooldowns.put(JUNGLE_AXE_ID, new ItemCooldownEntry(2000));
            }
        }
        else if (usedItemId.equals(TREECAPITATOR_ID)) {
            if (!isItemOnCooldown(TREECAPITATOR_ID)) {
                itemCooldowns.put(TREECAPITATOR_ID, new ItemCooldownEntry(2000));
            }
        }
    }

    private static TypedActionResult<ItemStack> onItemInteract(PlayerEntity player, World world, Hand hand) {
        String usedItemId = ItemUtils.getItemId(player.getMainHandStack());

        if (usedItemId != null && usedItemId.equals(GRAPPLING_HOOK_ID) && player.fishHook != null) {
            if (!isItemOnCooldown(GRAPPLING_HOOK_ID) && isPlayerWearingBatArmor(player)) {
                itemCooldowns.put(GRAPPLING_HOOK_ID, new ItemCooldownEntry(2000));
            }
        }

        return TypedActionResult.pass(ItemStack.EMPTY);
    }

    public static boolean isItemOnCooldown(ItemStack itemStack) {
        return isItemOnCooldown(ItemUtils.getItemId(itemStack));
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

    public static ItemCooldownEntry getItemCooldownEntry(ItemStack itemStack) {
        return itemCooldowns.get(ItemUtils.getItemId(itemStack));
    }

    private static boolean isPlayerWearingBatArmor(PlayerEntity player) {
        for (ItemStack stack : player.getArmorItems()) {
            String itemId = ItemUtils.getItemId(stack);
            if (!BAT_ARMOR_IDS.contains(itemId)) {
                return false;
            }
        }
        return true;
    }
}
