package pwk.utils.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pwk.utils.PWKUtils;
import java.util.UUID;

@Mixin(PlayerList.class)
public class PlayerOpMixin {

    //? if >=1.21.9 {
    @Inject(method = "op(Lnet/minecraft/server/players/NameAndId;)V", at = @At("TAIL"))
    private void pwkutils$onOp(net.minecraft.server.players.NameAndId profile, CallbackInfo ci) {
        pwkutils$refreshPrefix(profile.id());
    }

    @Inject(method = "deop(Lnet/minecraft/server/players/NameAndId;)V", at = @At("TAIL"))
    private void pwkutils$onDeop(net.minecraft.server.players.NameAndId profile, CallbackInfo ci) {
        pwkutils$refreshPrefix(profile.id());
    }
    //?} else {
    /*@Inject(method = "op(Lcom/mojang/authlib/GameProfile;)V", at = @At("TAIL"))
    private void pwkutils$onOp(GameProfile profile, CallbackInfo ci) {
        pwkutils$refreshPrefix(profile.getId());
    }

    @Inject(method = "deop(Lcom/mojang/authlib/GameProfile;)V", at = @At("TAIL"))
    private void pwkutils$onDeop(GameProfile profile, CallbackInfo ci) {
        pwkutils$refreshPrefix(profile.getId());
    }
    *///?}

    @Unique
    private void pwkutils$refreshPrefix(UUID id) {
        PlayerList self = (PlayerList) (Object) this;
        ServerPlayer player = self.getPlayer(id);
        if (player != null) {
            PWKUtils.PREFIX_MANAGER.applyPrefix(player);
        }
    }
}