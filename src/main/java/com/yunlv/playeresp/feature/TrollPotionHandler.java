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

import java.util.ArrayList;
import java.util.List;

public class TrollPotionHandler {
    public static void create(Minecraft mc, boolean lingering) {
        if (mc.player == null || !mc.player.isCreative()) return;

        ItemStack stack = new ItemStack(lingering ? Items.LINGERING_POTION : Items.SPLASH_POTION);
        List<MobEffectInstance> effects = new ArrayList<>();

        // 变态正面效果
        effects.add(new MobEffectInstance(MobEffects.HEALTH_BOOST, 6000, 255));
        effects.add(new MobEffectInstance(MobEffects.ABSORPTION, 6000, 255));
        effects.add(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 6000, 4));

        // 全套负面折磨
        effects.add(new MobEffectInstance(MobEffects.BLINDNESS, 6000, 255));
        effects.add(new MobEffectInstance(MobEffects.SLOWNESS, 6000, 255));
        effects.add(new MobEffectInstance(MobEffects.NAUSEA, 6000, 255));
        effects.add(new MobEffectInstance(MobEffects.POISON, 6000, 10));
        effects.add(new MobEffectInstance(MobEffects.WITHER, 6000, 10));
        effects.add(new MobEffectInstance(MobEffects.WEAKNESS, 6000, 255));
        effects.add(new MobEffectInstance(MobEffects.LEVITATION, 6000, 5));
        effects.add(new MobEffectInstance(MobEffects.UNLUCK, 6000, 255));

        PotionContents contents = new PotionContents(
            java.util.Optional.empty(),
            java.util.Optional.empty(),
            effects,
            java.util.Optional.empty()
        );

        stack.set(DataComponents.POTION_CONTENTS, contents);
        stack.set(DataComponents.CUSTOM_NAME, Component.literal(
            "§f" + (lingering ? "Lingering" : "Splash") + " Potion of §5§lTROLLING"
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