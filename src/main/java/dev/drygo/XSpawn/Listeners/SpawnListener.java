package dev.drygo.XSpawn.Listeners;

import dev.drygo.XSpawn.Managers.SpawnManager;
import dev.drygo.XSpawn.XSpawn;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class SpawnListener implements Listener {

    private final XSpawn plugin;

    public SpawnListener(XSpawn plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Location spawn = SpawnManager.getFirstSpawn();
        if (player.hasPlayedBefore()) return;
        if (spawn != null) {
            player.teleport(spawn);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        Location bedSpawn = player.getBedSpawnLocation();
        if (bedSpawn != null && plugin.getConfig().getBoolean("", false)) {
            event.setRespawnLocation(bedSpawn);
            return;
        }

        Location spawn = SpawnManager.getSpawnFor(player);
        if (spawn != null) {
            event.setRespawnLocation(spawn);
        }
    }
}
