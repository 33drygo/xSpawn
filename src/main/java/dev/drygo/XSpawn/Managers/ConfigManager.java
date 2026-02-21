package dev.drygo.XSpawn.Managers;

import dev.drygo.XSpawn.Utils.ChatUtils;
import dev.drygo.XSpawn.XSpawn;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigManager {
    private static XSpawn plugin;
    private static FileConfiguration messagesConfig;

    public static void init(XSpawn plugin) {
        ConfigManager.plugin = plugin;
    }

    public static void loadConfig() {
        try {
            plugin.saveDefaultConfig();
            plugin.reloadConfig();
            plugin.getLogger().info("✅ The config.yml file successfully loaded.");
        } catch (Exception e) {
            plugin.getLogger().severe("❌ Failed on loading config.yml: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static void reloadMessages() {
        try {
            File messagesFile = new File(plugin.getDataFolder(), "messages.yml");
            if (!messagesFile.exists()) {
                plugin.saveResource("messages.yml", false);
                plugin.getLogger().info("✅ The messages.yml file did not exist, it has been created.");
            } else {
                plugin.getLogger().info("✅ The messages.yml file has been loaded successfully.");
            }

            messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
            plugin.prefix = ChatUtils.formatColor("#5771ff&lx&r&f&lSpawn &cDefault Prefix &8»&r");
        } catch (Exception e) {
            plugin.getLogger().severe("❌ Failed to load messages configuration due to an unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String getPrefix() { return plugin.prefix; }
    public static void setPrefix(String prefix) { plugin.prefix = prefix; }
    public static FileConfiguration getMessageConfig() {
        return messagesConfig;
    }
}
