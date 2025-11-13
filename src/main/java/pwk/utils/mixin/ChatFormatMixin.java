package pwk.utils.mixin;

import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ChatFormatMixin {

    @Inject(method = "onChatMessage", at = @At("HEAD"), cancellable = true)
    private void onChatMessage(ChatMessageC2SPacket packet, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        String msg = packet.chatMessage();

        // Eigene Formatierung: Name: Nachricht
        Text formatted = Text.literal(player.getName().getString() + ": " + msg);

        // Sende an alle Spieler
        player.getServer().getPlayerManager().broadcast(formatted, false);

        // Vanilla-Handling unterdrücken
        ci.cancel();
    }
}
