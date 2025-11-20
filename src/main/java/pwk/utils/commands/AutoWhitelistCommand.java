package pwk.utils.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import pwk.utils.AutoWhitelistConfig;
import pwk.utils.LocalizationManager;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;

public class AutoWhitelistCommand {

    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("autowhitelist")
                    .requires(src -> src.hasPermissionLevel(2))
                    .then(literal("add")
                            .then(argument("name", StringArgumentType.word())
                                    .suggests((context, builder) -> {
                                        for (ServerPlayerEntity p : context.getSource().getServer()
                                                .getPlayerManager().getPlayerList()) {
                                            builder.suggest(p.getGameProfile().getName());
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

                                        //ctx.getSource().sendMessage(Text.literal("§8[§3PWKUtils§8] §a" + name + " §7wurde zur Projekt-Whitelist hinzugefügt."));
                                        ctx.getSource().sendMessage(Text.literal(LocalizationManager.get("command.autowhitelist.add", name)));
                                        return 1;
                                    })
                            )
                    )

                    .then(literal("list")
                            .executes(ctx -> {
                                AutoWhitelistConfig cfg = AutoWhitelistConfig.loadConfig();

                                if (cfg.players.isEmpty()) {
                                    //ctx.getSource().sendMessage(Text.literal("§8[§3PWKUtils§8] §7Keine Spieler in der Auto-Whitelist."));
                                    ctx.getSource().sendMessage(Text.literal(LocalizationManager.get("command.autowhitelist.list.empty")));
                                    return 1;
                                }

                                //ctx.getSource().sendMessage(Text.literal("§8[§3PWKUtils§8] §7Spieler in der Auto-Whitelist:"));
                                ctx.getSource().sendMessage(Text.literal(LocalizationManager.get("command.autowhitelist.list.header")));

                                for (String name : cfg.players) {
                                    ctx.getSource().sendMessage(Text.literal("§7 - §a" + name));
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

                                //ctx.getSource().sendMessage(Text.literal("§8[§3PWKUtils§8] §7Auto-Whitelist geleert (§a" + size + "§7 Einträge entfernt)."));
                                ctx.getSource().sendMessage(Text.literal(LocalizationManager.get("command.autowhitelist.clear", size)));
                                return 1;
                            })
                    )

                    .then(literal("run")
                            .executes(ctx -> {
                                AutoWhitelistConfig cfg = AutoWhitelistConfig.loadConfig();
                                var server = ctx.getSource().getServer();

                                int count = 0;

                                for (String playerName : cfg.players) {
                                    var profile = server.getUserCache().findByName(playerName);

                                    if (profile.isEmpty()) {
                                        //ctx.getSource().sendMessage(Text.literal("§8[§3PWKUtils§8] §cKonnte Spieler §4" + playerName + " §cnicht finden."));
                                        ctx.getSource().sendMessage(Text.literal(LocalizationManager.get("command.autowhitelist.run.notfound", playerName)));
                                        continue;
                                    }
                                    var entry = new net.minecraft.server.WhitelistEntry(profile.get());

                                    server.getPlayerManager().getWhitelist().add(entry);
                                    count++;
                                }

                                //ctx.getSource().sendMessage(Text.literal("§8[§3PWKUtils§8] §a" + count + " §7Spieler wurden automatisch gewhitelistet."));
                                ctx.getSource().sendMessage(Text.literal(LocalizationManager.get("command.autowhitelist.run.success", count)));

                                return 1;
                            })
                    )
            );
        });
    }
}