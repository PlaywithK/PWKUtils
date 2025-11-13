package pwk.utils.commands;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;

public class TPSCommand {
    private static long lastTickTime = System.currentTimeMillis();
    private static double tps = 20.0;

    public static void registerCommands() {
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            long now = System.currentTimeMillis();
            long delta = now - lastTickTime;
            tps = 1000.0 / delta;
            if (tps > 20.0) tps = 20.0;
            lastTickTime = now;
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("tps").executes(context -> {
                ServerPlayerEntity player = context.getSource().getPlayer();
                player.sendMessage(Text.literal("§8[§3PWKUtils§8] §cTPS: §6" + String.format("%.2f", tps)), false);
                return 1;
            }));
        });
    }
}