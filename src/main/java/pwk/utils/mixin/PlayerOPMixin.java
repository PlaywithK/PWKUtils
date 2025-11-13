package pwk.utils.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.command.OpCommand;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pwk.utils.PWKUtils;

import java.util.Collection;

@Mixin(OpCommand.class)
public class PlayerOPMixin {

    @Inject(method = "op", at = @At("TAIL"))
    private static void onOp(ServerCommandSource source, Collection<GameProfile> targets, CallbackInfoReturnable<Integer> cir) {
        MinecraftServer server = source.getServer();
        PlayerManager pm = server.getPlayerManager();

        for (GameProfile profile : targets) {
            ServerPlayerEntity player = pm.getPlayer(profile.getId());
            if (player != null) {
                PWKUtils.PREFIX_MANAGER.removePrefix(player);
                PWKUtils.PREFIX_MANAGER.applyPrefix(player);
            }
        }
    }
}
