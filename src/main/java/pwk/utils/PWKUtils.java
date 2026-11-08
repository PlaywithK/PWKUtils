package pwk.utils;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pwk.utils.commands.AutoWhitelistCommand;
import pwk.utils.commands.GamemodeCommand;
import pwk.utils.commands.TPSCommand;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.event.ServerChatEvent;

@Mod(PWKUtils.MOD_ID)
public class PWKUtils {
	public static final String MOD_ID = "pwkutils";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final PrefixManager PREFIX_MANAGER = new PrefixManager();

	public PWKUtils() {
		ConfigManager.loadConfig();
		AutoWhitelistConfig.loadConfig();
		LocalizationManager.load();
		new WelcomeMessageManager();

		NeoForge.EVENT_BUS.register(this);
		NeoForge.EVENT_BUS.addListener(TPSCommand::onServerTick);
		NeoForge.EVENT_BUS.addListener(TablistManager::onServerTick);

		NeoForge.EVENT_BUS.register(this);
		NeoForge.EVENT_BUS.addListener(TPSCommand::onServerTick);

		LOGGER.info("[PWKUtils] Mod initialized.");
	}

	@SubscribeEvent
	public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			PREFIX_MANAGER.applyPrefix(player);
		}
	}

	@SubscribeEvent
	public void onRegisterCommands(RegisterCommandsEvent event) {
		GamemodeCommand.register(event.getDispatcher());
		TPSCommand.register(event.getDispatcher());
		AutoWhitelistCommand.register(event.getDispatcher());
	}

	@SubscribeEvent
	public void onChat(ServerChatEvent event) {
		if (!ConfigManager.isChatFormattingAllowed()) return;

		ServerPlayer player = event.getPlayer();
//? if >=1.21.9 {
		/*MinecraftServer server = player.level().getServer();
		 *///?} else {
		MinecraftServer server = player.getServer();
//?}
		if (server == null) return;

		String formattedMsg = ConfigManager.getChatFormat(
				PREFIX_MANAGER.getFormattedName(player),
				event.getRawText()
		);

		event.setCanceled(true);
		server.getPlayerList().broadcastSystemMessage(Component.literal(formattedMsg), false);
	}
}