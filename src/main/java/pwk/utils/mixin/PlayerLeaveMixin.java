package pwk.utils.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import pwk.utils.ConfigManager;
import pwk.utils.PWKUtils;

@Mixin(ServerGamePacketListenerImpl.class)
public class PlayerLeaveMixin {

    @Shadow
    public ServerPlayer player;

    @Redirect(
            method = "removePlayerFromWorld",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V"
            )
    )
    private void pwkutils$redirectLeaveMessage(PlayerList manager, Component message, boolean overlay) {
        if (ConfigManager.shouldChangeJoinLeave()) {
            String msg = ConfigManager.getLeaveMessage(PWKUtils.PREFIX_MANAGER.getFormattedName(player));
            manager.broadcastSystemMessage(Component.literal(msg), false);
        } else {
            manager.broadcastSystemMessage(message, false);
        }
    }
}