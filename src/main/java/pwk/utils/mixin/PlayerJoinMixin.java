package pwk.utils.mixin;

import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import pwk.utils.ConfigManager;
import pwk.utils.PWKUtils;

@Mixin(PlayerManager.class)
public class PlayerJoinMixin {

        @Redirect(
                method = "onPlayerConnect",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/server/PlayerManager;broadcast(Lnet/minecraft/text/Text;Z)V"
                )
        )
        private void redirectJoinMessage(PlayerManager instance, Text message, boolean overlay, ClientConnection connection, ServerPlayerEntity player) {
                if (ConfigManager.shouldChangeJoinLeave()) {
                        String msg = ConfigManager.getJoinMessage(PWKUtils.PREFIX_MANAGER.getFormattedName(player));
                        instance.broadcast(Text.literal(msg), false);
                } else {
                        instance.broadcast(message, overlay);
                }
        }
}
