package pwk.utils.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.command.DeOpCommand;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pwk.utils.PWKUtils;

import java.util.Collection;

@Mixin(DeOpCommand.class)
public class PlayerDeOPMixin {

    @Inject(method = "deop", at = @At("TAIL"))
    private static void onDeop(ServerCommandSource source, Collection<GameProfile> targets, CallbackInfoReturnable<Integer> cir) {
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
