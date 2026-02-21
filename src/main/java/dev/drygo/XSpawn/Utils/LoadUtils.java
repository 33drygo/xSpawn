package dev.drygo.XSpawn.Utils;

import dev.drygo.XSpawn.Handlers.XSpawnCommand;
import dev.drygo.XSpawn.Handlers.XSpawnTabCompleter;
import dev.drygo.XSpawn.Listeners.SpawnListener;
import dev.drygo.XSpawn.Managers.ConfigManager;
import dev.drygo.XSpawn.Managers.SpawnManager;
import dev.drygo.XSpawn.XSpawn;
import org.bukkit.Bukkit;

import java.util.Objects;

public class LoadUtils {
    private  static XSpawn plugin;

    public static void loadFeatures() {
        loadFiles();
        loadCommand();
        loadListeners();
        loadXTeams();
    }

    public static void loadFiles() {
        ConfigManager.loadConfig();
        ConfigManager.reloadMessages();
        ConfigManager.setPrefix(ConfigManager.getMessageConfig().getString("prefix"));
        SpawnManager.loadSpawns();
    }
    private static void loadCommand() {
        if (plugin.getCommand("xspawn") != null) {
            plugin.getLogger().info("✅ Plugin command /xspawn successfully registered.");
            Objects.requireNonNull(plugin.getCommand("xspawn")).setExecutor(new XSpawnCommand(plugin));
            Objects.requireNonNull(plugin.getCommand("xspawn")).setTabCompleter(new XSpawnTabCompleter(plugin));
        } else {
            plugin.getLogger().severe("❌ Error: /xspawn command is no registered in plugin.yml");
        }
    }

    private static void loadListeners() {
        plugin.getServer().getPluginManager().registerEvents(new SpawnListener(plugin), plugin);
    }

    private static void loadXTeams() {
        if (Bukkit.getPluginManager().getPlugin("xTeams") != null) {
            plugin.getLogger().info("✅ xTeams detected. xTeams hook successfully loaded.");
            plugin.workingXTeams = true;
        }
    }

    public static void init(XSpawn plugin) {
        LoadUtils.plugin = plugin;
    }
}
