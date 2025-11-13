package pwk.utils.mixin;

import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pwk.utils.ConfigManager;
import pwk.utils.PWKUtils;
import pwk.utils.PrefixManager;

@Mixin(ServerPlayNetworkHandler.class)
public class ChatFormatMixin {

    @Shadow
    public ServerPlayerEntity player;

    @Inject(method = "onChatMessage", at = @At("HEAD"), cancellable = true)
    private void onChatMessage(ChatMessageC2SPacket packet, CallbackInfo ci) {
        if (!ConfigManager.isChatFormattingAllowed()) return;

        String msg = packet.chatMessage();
        String formattedMsg = ConfigManager.getChatFormat(
                PWKUtils.PREFIX_MANAGER.getFormattedName(player),
                msg
        );

        player.getServer().getPlayerManager().broadcast(Text.literal(formattedMsg), false);
        ci.cancel();
    }
}

