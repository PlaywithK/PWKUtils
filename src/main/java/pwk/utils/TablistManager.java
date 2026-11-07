package pwk.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

public class TablistManager {

    public static void onServerTick(ServerTickEvent.Pre event) {
        MinecraftServer server = event.getServer();
        if (server.getTickCount() % 20 == 0) {
            updateTablist(server);
        }
    }

    private static void updateTablist(MinecraftServer server) {
        int online = server.getPlayerList().getPlayerCount();
        int max    = server.getPlayerList().getMaxPlayers();

        Component header = Component.literal(ConfigManager.getTablistHeader());
        Component footer = Component.literal(ConfigManager.getTablistFooter(online, max));
        ClientboundTabListPacket packet = new ClientboundTabListPacket(header, footer);

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            player.connection.send(packet);
        }
    }
}