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
    public static void create(Minecraft mc) {
        if (mc.player == null || !mc.player.isCreative()) return;

        ItemStack stack = new ItemStack(Items.SPLASH_POTION);
        List<MobEffectInstance> effects = new ArrayList<>();

        // === 变态正面效果（血量上限提高等） ===
        effects.add(new MobEffectInstance(MobEffects.HEALTH_BOOST, 6000, 255)); // 255级生命提升，血量上限突破天际
        effects.add(new MobEffectInstance(MobEffects.ABSORPTION, 6000, 255)); // 255级伤害吸收，金心拉满
        effects.add(new MobEffectInstance(MobEffects.RESISTANCE, 6000, 4)); // 5级抗性提升（最高级）
        
        // === 全套负面折磨 ===
        effects.add(new MobEffectInstance(MobEffects.BLINDNESS, 6000, 255)); // 失明
        effects.add(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 6000, 255)); // 极度缓慢
        effects.add(new MobEffectInstance(MobEffects.CONFUSION, 6000, 255)); // 极度反胃（屏幕疯狂扭曲）
        effects.add(new MobEffectInstance(MobEffects.POISON, 6000, 10)); // 中毒10级（持续扣血，但不致死）
        effects.add(new MobEffectInstance(MobEffects.WITHER, 6000, 10)); // 凋零10级（扣血到半颗心）
        effects.add(new MobEffectInstance(MobEffects.WEAKNESS, 6000, 255)); // 虚弱
        effects.add(new MobEffectInstance(MobEffects.LEVITATION, 6000, 5)); // 漂浮（让人一直往天上飞）
        effects.add(new MobEffectInstance(MobEffects.UNLUCK, 6000, 255)); // 霉运

        PotionContents contents = new PotionContents(
            java.util.Optional.empty(),
            java.util.Optional.empty(),
            effects,
            java.util.Optional.empty()
        );

        stack.set(DataComponents.POTION_CONTENTS, contents);
        stack.set(DataComponents.CUSTOM_NAME, Component.literal("§fSplash Potion of §5§lTROLLING"));

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