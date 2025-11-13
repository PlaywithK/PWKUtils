package pwk.utils;

import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PrefixManager {
    private final Map<UUID, String> prefixes = new HashMap<>();

    public void setPrefix(ServerPlayerEntity player, String prefix) {
        prefixes.put(player.getUuid(), prefix);
        updateTeam(player);
    }

    public void applyPrefix(ServerPlayerEntity player) {
        String prefix;
        if (player.hasPermissionLevel(4)) {
            prefix = ConfigManager.getOpPrefix();
        } else {
            prefix = ConfigManager.getNonOpPrefix();
        }
        setPrefix(player, prefix);
    }


    public String getPrefix(ServerPlayerEntity player) {
        return prefixes.getOrDefault(player.getUuid(), "");
    }

    public void removePrefix(ServerPlayerEntity player) {
        prefixes.remove(player.getUuid());

        MinecraftServer server = player.getServer();
        if (server == null) return;

        Scoreboard scoreboard = server.getScoreboard();
        Team team = scoreboard.getPlayerTeam(player.getEntityName());
        if (team != null) {
            scoreboard.removePlayerFromTeam(player.getEntityName(), team);
            if (team.getPlayerList().isEmpty()) {
                scoreboard.removeTeam(team);
            }
        }
    }

    public String getFormattedName(ServerPlayerEntity player) {
        return getPrefix(player) + player.getName().getString();
    }

    private void updateTeam(ServerPlayerEntity player) {
        MinecraftServer server = player.getServer();
        if (server == null) return;

        Scoreboard scoreboard = server.getScoreboard();
        String teamName = "pwk_" + player.getEntityName();

        if (teamName.length() > 16) {
            teamName = teamName.substring(0, 16);
        }

        Team team = scoreboard.getTeam(teamName);
        if (team == null) {
            team = scoreboard.addTeam(teamName);
        }

        team.setPrefix(net.minecraft.text.Text.literal(getPrefix(player)));

        Team currentTeam = scoreboard.getPlayerTeam(player.getEntityName());
        if (currentTeam == null || !currentTeam.equals(team)) {
            scoreboard.addPlayerToTeam(player.getEntityName(), team);
        }
    }
}
