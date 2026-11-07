package pwk.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PrefixManager {
    private final Map<UUID, String> prefixes = new HashMap<>();

    public void setPrefix(ServerPlayer player, String prefix) {
        prefixes.put(player.getUUID(), prefix);
        updateTeam(player);
    }

    public void applyPrefix(ServerPlayer player) {
        String prefix;
        //? if >=1.21.11 {
    if (player.createCommandSourceStack().permissions().hasPermission(
            net.minecraft.server.permissions.Permissions.COMMANDS_OWNER)) {
    //?} else {
        /*if (player.hasPermissions(4)) {
            *///?}
            prefix = ConfigManager.getOpPrefix();
        } else {
            prefix = ConfigManager.getNonOpPrefix();
        }
        setPrefix(player, prefix);
    }

    public String getPrefix(ServerPlayer player) {
        return prefixes.getOrDefault(player.getUUID(), "");
    }

    public void removePrefix(ServerPlayer player) {
        prefixes.remove(player.getUUID());

//? if >=1.21.11 {
        MinecraftServer server = player.level().getServer();
         //?} else {
        /*MinecraftServer server = player.getServer();
*///?}
        if (server == null) return;

        Scoreboard scoreboard = server.getScoreboard();
        PlayerTeam team = scoreboard.getPlayersTeam(player.getScoreboardName());
        if (team != null) {
            scoreboard.removePlayerFromTeam(player.getScoreboardName(), team);
            if (team.getPlayers().isEmpty()) {
                scoreboard.removePlayerTeam(team);
            }
        }
    }

    public String getFormattedName(ServerPlayer player) {
        return getPrefix(player) + player.getName().getString();
    }

    private void updateTeam(ServerPlayer player) {
//? if >=1.21.11 {
        MinecraftServer server = player.level().getServer();
         //?} else {
        /*MinecraftServer server = player.getServer();
*///?}
        if (server == null) return;

        Scoreboard scoreboard = server.getScoreboard();
        String teamName = "pwk_" + player.getScoreboardName();

        if (teamName.length() > 16) {
            teamName = teamName.substring(0, 16);
        }

        PlayerTeam team = scoreboard.getPlayerTeam(teamName);
        if (team == null) {
            team = scoreboard.addPlayerTeam(teamName);
        }

        team.setPlayerPrefix(Component.literal(getPrefix(player)));

        PlayerTeam currentTeam = scoreboard.getPlayersTeam(player.getScoreboardName());
        if (currentTeam == null || !currentTeam.equals(team)) {
            scoreboard.addPlayerToTeam(player.getScoreboardName(), team);
        }
    }
}