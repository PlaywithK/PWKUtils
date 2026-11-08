package pwk.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class WelcomeMessageManager {

    public WelcomeMessageManager() {
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            //? if >=1.21.9 {
            MinecraftServer server = player.level().getServer();
             //?} else {
            /*MinecraftServer server = player.getServer();
            *///?}
            if (server == null) return;
            server.execute(() -> sendWelcomeMessage(player));
        }
    }

    private void sendWelcomeMessage(ServerPlayer player) {
        if (!ConfigManager.shouldShowWelcomeMessage()) return;

        String rawMessage = ConfigManager.getWelcomeMessage();

        String[] lines = rawMessage.split("\n");
        for (String line : lines) {
            player.sendSystemMessage(Component.literal(line));
        }
    }
}