package com.yunlv.playeresp.feature;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.ArrayList;
import java.util.List;

public class ChestCollector {

    private static final List<BlockPos> containers = new ArrayList<>();
    private static int tickCounter = 0;

    public static void tick(Minecraft mc) {
        if (++tickCounter < 20) return;
        tickCounter = 0;

        containers.clear();
        if (!FeatureManager.chestEsp || mc.player == null || mc.level == null) return;

        ChunkPos center = mc.player.chunkPosition();
        int radius = 4;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                LevelChunk chunk = mc.level.getChunkSource()
                    .getChunk(center.x + dx, center.z + dz, false);
                if (chunk == null) continue;

                for (BlockEntity be : chunk.getBlockEntities().values()) {
                    if (isContainer(be)) {
                        containers.add(be.getBlockPos());
                    }
                }
            }
        }
    }

    private static boolean isContainer(BlockEntity be) {
        return be instanceof ChestBlockEntity
            || be instanceof BarrelBlockEntity
            || be instanceof ShulkerBoxBlockEntity
            || be instanceof AbstractFurnaceBlockEntity
            || be instanceof HopperBlockEntity
            || be instanceof DispenserBlockEntity
            || be instanceof DropperBlockEntity
            || be instanceof BrewingStandBlockEntity;
    }

    public static List<BlockPos> getContainers() {
        return containers;
    }

    public static void clear() {
        containers.clear();
    }
}