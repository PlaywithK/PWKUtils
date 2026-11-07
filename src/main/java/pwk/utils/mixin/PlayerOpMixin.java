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

@Mixin(PlayerList.class)
public class PlayerOpMixin {

    @Inject(method = "op(Lcom/mojang/authlib/GameProfile;)V", at = @At("TAIL"))
    private void pwkutils$onOp(GameProfile profile, CallbackInfo ci) {
        pwkutils$refreshPrefix(profile);
    }

    @Inject(method = "deop(Lcom/mojang/authlib/GameProfile;)V", at = @At("TAIL"))
    private void pwkutils$onDeop(GameProfile profile, CallbackInfo ci) {
        pwkutils$refreshPrefix(profile);
    }

    @Unique
    private void pwkutils$refreshPrefix(GameProfile profile) {
        PlayerList self = (PlayerList) (Object) this;
        ServerPlayer player = self.getPlayer(profile.getId());
        if (player != null) {
            PWKUtils.PREFIX_MANAGER.applyPrefix(player);
        }
    }
}