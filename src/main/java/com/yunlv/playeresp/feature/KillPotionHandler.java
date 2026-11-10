package com.yunlv.playeresp.feature;

import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;

import java.util.List;

public class KillPotionHandler {
    public static void create(Minecraft mc, boolean lingering) {
        if (mc.player == null || !mc.player.isCreative()) return;

        ItemStack stack = new ItemStack(lingering ? Items.LINGERING_POTION : Items.SPLASH_POTION);

        // 120 级瞬间治疗会导致治疗量溢出为负数，从而秒杀任何玩家
        MobEffectInstance instantHealth = new MobEffectInstance(MobEffects.INSTANT_HEALTH, 1, 120);

        PotionContents contents = new PotionContents(
            java.util.Optional.empty(),
            java.util.Optional.empty(),
            List.of(instantHealth),
            java.util.Optional.empty()
        );

        stack.set(DataComponents.POTION_CONTENTS, contents);
        stack.set(DataComponents.CUSTOM_NAME, Component.literal(
            "§f" + (lingering ? "Lingering" : "Splash") + " Potion of §4§lINSTANT DEATH"
        ));

        placeStackInHotbar(mc, stack);
    }

    private static void placeStackInHotbar(Minecraft mc, ItemStack stack) {
        for (int i = 0; i < 9; i++) {
            if (!mc.player.getInventory().getItem(i).isEmpty()) continue;
            mc.player.connection.send(new ServerboundSetCreativeModeSlotPacket(36 + i, stack));
            return;
        }
    }
}