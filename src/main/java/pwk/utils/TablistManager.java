package pwk.utils;

import net.minecraft.network.packet.s2c.play.PlayerListHeaderS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class TablistManager {

    public static void start(MinecraftServer server) {
        ServerTickEvents.START_SERVER_TICK.register(tickServer -> {
            if (tickServer.getTicks() % 20 == 0) {
                updateTablist(server);
            }
        });
    }

    private static void updateTablist(MinecraftServer server) {
        int online = server.getPlayerManager().getCurrentPlayerCount();
        int max    = server.getPlayerManager().getMaxPlayerCount();

        Text header = Text.literal(ConfigManager.getTablistHeader());
        Text footer = Text.literal(ConfigManager.getTablistFooter(online, max));

        PlayerListHeaderS2CPacket packet = new PlayerListHeaderS2CPacket(header, footer);

        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            player.networkHandler.sendPacket(packet);
        }
    }
}
