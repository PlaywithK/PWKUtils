package pwk.utils.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.UserWhiteListEntry;
import pwk.utils.AutoWhitelistConfig;
import pwk.utils.LocalizationManager;
import java.util.concurrent.CompletableFuture;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class AutoWhitelistCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("autowhitelist")
                        .requires(src ->
                                        //? if >=1.21.11 {
        /*src.permissions().hasPermission(
                net.minecraft.server.permissions.Permissions.COMMANDS_GAMEMASTER)
        *///?} else {
                                        src.hasPermission(2)
                                //?}
                        )

                .then(literal("add")
                        .then(argument("name", StringArgumentType.word())
                                .suggests((context, builder) -> {
                                    for (ServerPlayer p : context.getSource().getServer()
                                            .getPlayerList().getPlayers()) {
                                        //? if >=1.21.11 {
                                        /*builder.suggest(p.getGameProfile().name());
                                         *///?} else {
                                        builder.suggest(p.getGameProfile().getName());
//?}
                                    }
                                    return CompletableFuture.completedFuture(builder.build());
                                })
                                .executes(ctx -> {
                                    String name = StringArgumentType.getString(ctx, "name");

                                    AutoWhitelistConfig cfg = AutoWhitelistConfig.loadConfig();
                                    if (!cfg.players.contains(name)) {
                                        cfg.players.add(name);
                                        cfg.save();
                                    }

                                    send(ctx.getSource(), LocalizationManager.get("command.autowhitelist.add", name));
                                    return 1;
                                })
                        )
                )

                .then(literal("list")
                        .executes(ctx -> {
                            AutoWhitelistConfig cfg = AutoWhitelistConfig.loadConfig();

                            if (cfg.players.isEmpty()) {
                                send(ctx.getSource(), LocalizationManager.get("command.autowhitelist.list.empty"));
                                return 1;
                            }

                            send(ctx.getSource(), LocalizationManager.get("command.autowhitelist.list.header"));

                            for (String name : cfg.players) {
                                send(ctx.getSource(), "§7 - §a" + name);
                            }

                            return 1;
                        })
                )

                .then(literal("clear")
                        .executes(ctx -> {
                            AutoWhitelistConfig cfg = AutoWhitelistConfig.loadConfig();
                            int size = cfg.players.size();

                            cfg.players.clear();
                            cfg.save();

                            send(ctx.getSource(), LocalizationManager.get("command.autowhitelist.clear", size));
                            return 1;
                        })
                )

                .then(literal("run")
                        .executes(ctx -> {
                            AutoWhitelistConfig cfg = AutoWhitelistConfig.loadConfig();
                            var server = ctx.getSource().getServer();

                            int count = 0;

                            for (String playerName : cfg.players) {
//? if >=1.21.11 {
                                /*var profile = server.services().nameToIdCache().get(playerName);
*///?} else {
                                var profile = server.getProfileCache().get(playerName);
                                 //?}

                                if (profile.isEmpty()) {
                                    send(ctx.getSource(), LocalizationManager.get("command.autowhitelist.run.notfound", playerName));
                                    continue;
                                }

                                server.getPlayerList().getWhiteList()
                                        .add(new UserWhiteListEntry(profile.get()));
                                count++;
                            }

                            send(ctx.getSource(), LocalizationManager.get("command.autowhitelist.run.success", count));
                            return 1;
                        })
                )
        );
    }

    private static void send(CommandSourceStack source, String message) {
        source.sendSuccess(() -> Component.literal(message), false);
    }
}