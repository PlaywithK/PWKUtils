package pwk.utils.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import pwk.utils.LocalizationManager;
import java.util.concurrent.CompletableFuture;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class GamemodeCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("gm")
                .requires(src ->
                                //? if >=1.21.11 {
                /*src.permissions().hasPermission(
                        net.minecraft.server.permissions.Permissions.COMMANDS_GAMEMASTER)
                *///?} else {
                                src.hasPermission(2)
                        //?}
                )
                .then(argument("mode", IntegerArgumentType.integer(0, 3))
                        .executes(context -> {
                            int modeInt = IntegerArgumentType.getInteger(context, "mode");
                            ServerPlayer player = context.getSource().getPlayerOrException();

                            GameType mode = getGameMode(modeInt);
                            player.setGameMode(mode);

                            player.sendSystemMessage(Component.literal(
                                    LocalizationManager.get("gamemode.self", mode)));
                            return 1;
                        })

                        .then(argument("target", StringArgumentType.word())
                                .suggests((context, builder) -> {
                                    for (ServerPlayer p : context.getSource().getServer()
                                            .getPlayerList().getPlayers()) {
                                        //? if >=1.21.9 {
                                        /*builder.suggest(p.getGameProfile().name());
                                         *///?} else {
                                        builder.suggest(p.getGameProfile().getName());
                                        //?}
                                    }
                                    return CompletableFuture.completedFuture(builder.build());
                                })

                                .executes(context -> {
                                    int modeInt = IntegerArgumentType.getInteger(context, "mode");
                                    String targetName = StringArgumentType.getString(context, "target");

                                    MinecraftServer server = context.getSource().getServer();
                                    ServerPlayer target = server.getPlayerList().getPlayerByName(targetName);

                                    if (target == null) {
                                        context.getSource().sendFailure(Component.literal(
                                                LocalizationManager.get("gamemode.notfound", targetName)));
                                        return 0;
                                    }

                                    GameType mode = getGameMode(modeInt);
                                    target.setGameMode(mode);

                                    context.getSource().sendSuccess(() -> Component.literal(
                                            LocalizationManager.get("gamemode.setother",
                                                    target.getName().getString(), mode)), false);
                                    target.sendSystemMessage(Component.literal(
                                            LocalizationManager.get("gamemode.target", mode)));
                                    return 1;
                                })
                        )
                )
        );
    }

    private static GameType getGameMode(int modeInt) {
        return switch (modeInt) {
            case 0 -> GameType.SURVIVAL;
            case 1 -> GameType.CREATIVE;
            case 2 -> GameType.ADVENTURE;
            case 3 -> GameType.SPECTATOR;
            default -> GameType.SURVIVAL;
        };
    }
}