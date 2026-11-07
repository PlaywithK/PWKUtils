package pwk.utils.mixin;

import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import pwk.utils.ConfigManager;
import pwk.utils.PWKUtils;

@Mixin(PlayerList.class)
public class PlayerJoinMixin {

        @Redirect(
                method = "placeNewPlayer",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V"
                )
        )
        private void pwkutils$redirectJoinMessage(PlayerList instance, Component message, boolean overlay,
                                                  Connection connection, ServerPlayer player,
                                                  CommonListenerCookie cookie) {
                if (ConfigManager.shouldChangeJoinLeave()) {
                        String msg = ConfigManager.getJoinMessage(PWKUtils.PREFIX_MANAGER.getFormattedName(player));
                        instance.broadcastSystemMessage(Component.literal(msg), false);
                } else {
                        instance.broadcastSystemMessage(message, overlay);
                }
        }
}