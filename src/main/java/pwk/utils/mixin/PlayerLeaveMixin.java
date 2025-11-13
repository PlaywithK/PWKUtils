package pwk.utils.mixin;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class PlayerLeaveMixin {

    @Shadow
    public ServerPlayerEntity player;

    @Inject(method = "onDisconnected", at = @At("HEAD"), cancellable = true)
    private void onPlayerDisconnect(Text reason, CallbackInfo ci) {
        player.getServer().getPlayerManager().broadcast(
                Text.literal("§c- " + player.getName().getString() + " hat den Server verlassen."), false
        );
        ci.cancel();
    }
}
