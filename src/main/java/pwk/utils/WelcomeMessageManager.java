package pwk.utils;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class WelcomeMessageManager {

    public WelcomeMessageManager() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.getPlayer();
            server.execute(() -> sendWelcomeMessage(player));
        });
    }

    private void sendWelcomeMessage(ServerPlayerEntity player) {
        if (!ConfigManager.shouldShowWelcomeMessage()) return;

        MinecraftServer server = player.getServer();
        int onlinePlayers = server.getCurrentPlayerCount();

        String rawMessage = ConfigManager.getWelcomeMessage();

        String[] lines = rawMessage.split("\n");
        for (String line : lines) {
            player.sendMessage(Text.literal(line), false);
        }
    }
}