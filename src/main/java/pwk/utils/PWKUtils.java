package pwk.utils;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pwk.utils.commands.GamemodeCommand;
import pwk.utils.commands.TPSCommand;
import pwk.utils.commands.AutoWhitelistCommand;

public class PWKUtils implements ModInitializer {
	public static final String MOD_ID = "pwkutils";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final PrefixManager PREFIX_MANAGER = new PrefixManager();

	@Override
	public void onInitialize() {
		ConfigManager.loadConfig();
		AutoWhitelistConfig.loadConfig();

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayerEntity player = handler.player;
			PREFIX_MANAGER.applyPrefix(player);
		});

		GamemodeCommand.registerCommands();
		TPSCommand.registerCommands();

		AutoWhitelistCommand.registerCommands();
		new WelcomeMessageManager();
		LocalizationManager.load();

		LOGGER.info("[PWKUtils] Mod initialized.");
		ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStarted);
	}

	private void onServerStarted(MinecraftServer server) {
		TablistManager.start(server);
	}
}