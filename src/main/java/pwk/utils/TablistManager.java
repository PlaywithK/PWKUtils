package pwk.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
//? if >=1.20.5 {
import net.neoforged.neoforge.event.tick.ServerTickEvent;
//?} else {
/*import net.neoforged.neoforge.event.TickEvent;
 *///?}


public class TablistManager {

    //? if >=1.20.5 {
    public static void onServerTick(ServerTickEvent.Pre event) {
//?} else {
    /*public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
*///?}
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