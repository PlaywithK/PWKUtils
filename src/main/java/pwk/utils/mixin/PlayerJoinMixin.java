package pwk.utils.mixin;

import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

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
            instance.broadcast(Text.literal("§a+ Willkommen, " + player.getName().getString() + "!"), false);
        }
}
