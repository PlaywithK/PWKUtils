package pwk.utils.mixin;

import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import pwk.utils.ConfigManager;

@Mixin(ServerPlayNetworkHandler.class)
public class PlayerLeaveMixin {

    @Shadow
    public ServerPlayerEntity player;

    @Redirect(
            method = "onDisconnected",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/PlayerManager;broadcast(Lnet/minecraft/text/Text;Z)V"
            )
    )
    private void redirectLeaveMessage(PlayerManager manager, Text message, boolean overlay) {
        if (ConfigManager.shouldChangeJoinLeave()) {
            String msg = ConfigManager.getLeaveMessage(player.getName().getString());
            manager.broadcast(Text.literal(msg), false);
        } else {
            manager.broadcast(message, false);
        }
    }
}
