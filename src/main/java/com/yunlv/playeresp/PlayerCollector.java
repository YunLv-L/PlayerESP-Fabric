package com.yunlv.playeresp;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import java.util.ArrayList;
import java.util.List;

public class PlayerCollector {
    private static final List<Player> players = new ArrayList<>();

    public static void update(Minecraft client) {
        players.clear();
        if (client.level == null || client.player == null) return;

        for (Player player : client.level.players()) {
            if (player == client.player) continue;
            if (PlayerEspMod.config.filterCreative && player.isCreative()) continue;
            if (PlayerEspMod.config.filterSleeping && player.isSleeping()) continue;
            if (PlayerEspMod.config.filterInvisible && player.isInvisible()) continue;
            players.add(player);
        }
    }

    public static void clear() {
        players.clear();
    }

    public static List<Player> getPlayers() {
        return players;
    }
}