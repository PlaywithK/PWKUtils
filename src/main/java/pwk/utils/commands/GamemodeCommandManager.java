package pwk.utils.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class GamemodeCommandManager {
    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                    literal("gm")
                            .requires(source -> source.hasPermissionLevel(2)) // <-- Nur OPs dürfen den Befehl nutzen
                            .then(argument("mode", IntegerArgumentType.integer(0, 3))
                                    .executes(context -> {
                                        int modeInt = IntegerArgumentType.getInteger(context, "mode");
                                        ServerPlayerEntity player = context.getSource().getPlayer();

                                        GameMode mode = switch (modeInt) {
                                            case 0 -> GameMode.SURVIVAL;
                                            case 1 -> GameMode.CREATIVE;
                                            case 2 -> GameMode.ADVENTURE;
                                            case 3 -> GameMode.SPECTATOR;
                                            default -> GameMode.SURVIVAL;
                                        };

                                        player.changeGameMode(mode);
                                        player.sendMessage(Text.literal("§8[§3PWKUtils§8] §7Dein Spielmodus wurde auf §6" + mode + " §7gesetzt!"), false);
                                        return 1;
                                    })
                            )
            );
        });
    }
}