package dev.drygo.XSpawn.API;

import dev.drygo.XSpawn.Managers.SpawnManager;
import dev.drygo.XTeams.Models.Team;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class XSpawnAPI {

    public static void setFirstSpawn(Location location) {
        SpawnManager.setFirstSpawn(location);
    }

    public static Location getFirstSpawn() {
        return SpawnManager.getFirstSpawn();
    }

    public static void removeFirstSpawn() {
        SpawnManager.removeFirstSpawn();
    }

    public static void setPlayerSpawn(String playerName, Location location) {
        SpawnManager.setPlayerSpawn(playerName, location);
    }

    public static Location getPlayerSpawn(String playerName) {
        return SpawnManager.getPlayerSpawn(playerName);
    }

    public static void removePlayerSpawn(String playerName) {
        SpawnManager.removePlayerSpawn(playerName);
    }

    public static void setTeamSpawn(Team team, Location location) {
        SpawnManager.setTeamSpawn(team, location);
    }

    public static Location getTeamSpawn(Team team) {
        return SpawnManager.getTeamSpawn(team);
    }

    public static void removeTeamSpawn(Team team) {
        SpawnManager.removeTeamSpawn(team);
    }

    public static Location getSpawnFor(Player player) {
        return SpawnManager.getSpawnFor(player);
    }
}
