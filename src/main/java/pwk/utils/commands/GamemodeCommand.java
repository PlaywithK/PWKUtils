package pwk.utils.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.MinecraftServer;
import pwk.utils.LocalizationManager;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class GamemodeCommand {
    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("gm")
                    .requires(src -> src.hasPermissionLevel(2))
                    .then(argument("mode", IntegerArgumentType.integer(0, 3))
                            .executes(context -> {
                                int modeInt = IntegerArgumentType.getInteger(context, "mode");
                                ServerPlayerEntity player = context.getSource().getPlayer();

                                GameMode mode = getGameMode(modeInt);
                                player.changeGameMode(mode);

                                player.sendMessage(Text.literal(LocalizationManager.get("gamemode.self", mode)), false);
                                return 1;
                            })

                            .then(argument("target", StringArgumentType.word())
                                    .suggests((context, builder) -> {
                                        for (ServerPlayerEntity p : context.getSource().getServer()
                                                .getPlayerManager().getPlayerList()) {
                                            builder.suggest(p.getGameProfile().getName());
                                        }
                                        return CompletableFuture.completedFuture(builder.build());
                                    })

                                    .executes(context -> {
                                        int modeInt = IntegerArgumentType.getInteger(context, "mode");
                                        String targetName = StringArgumentType.getString(context, "target");

                                        MinecraftServer server = context.getSource().getServer();
                                        ServerPlayerEntity target = server.getPlayerManager().getPlayer(targetName);

                                        if (target == null) {
                                            context.getSource().sendError(Text.literal(LocalizationManager.get("gamemode.notfound", targetName)));;
                                            return 0;
                                        }

                                        GameMode mode = getGameMode(modeInt);
                                        target.changeGameMode(mode);

                                        context.getSource().sendMessage(Text.literal(LocalizationManager.get("gamemode.setother", target.getName().getString(), mode)));
                                        target.sendMessage(Text.literal(LocalizationManager.get("gamemode.target", mode)));
                                        return 1;
                                    })
                            )
                    )
            );
        });
    }

    private static GameMode getGameMode(int modeInt) {
        return switch (modeInt) {
            case 0 -> GameMode.SURVIVAL;
            case 1 -> GameMode.CREATIVE;
            case 2 -> GameMode.ADVENTURE;
            case 3 -> GameMode.SPECTATOR;
            default -> GameMode.SURVIVAL;
        };
    }
}