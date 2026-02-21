package dev.drygo.XSpawn.Utils;

import dev.drygo.XSpawn.UpdateChecker.ModrinthUpdateChecker;
import dev.drygo.XSpawn.XSpawn;
import org.bukkit.Bukkit;

public class LogsUtils {
    private static XSpawn plugin;

    public static void init(XSpawn plugin) {
        LogsUtils.plugin = plugin;
    }

    public static void sendStartupMessage() {
        Bukkit.getConsoleSender().sendMessage(ChatUtils.formatColor(" "));
        Bukkit.getConsoleSender().sendMessage(ChatUtils.formatColor("&9&lx&r&f&lSpawn #a0ff72plugin enabled!"));
        Bukkit.getConsoleSender().sendMessage(ChatUtils.formatColor("#fff18dVersion: #ffffff" + plugin.version));
        Bukkit.getConsoleSender().sendMessage(ChatUtils.formatColor("#fff18dDeveloped by: #ffffff" + String.join(", ", plugin.getDescription().getAuthors())));
        Bukkit.getConsoleSender().sendMessage(ChatUtils.formatColor(" "));
    }
    public static void sendShutdownMessage() {
        Bukkit.getConsoleSender().sendMessage(ChatUtils.formatColor(" "));
        Bukkit.getConsoleSender().sendMessage(ChatUtils.formatColor("&9&lx&r&f&lSpawn #ff7272plugin disabled!"));
        Bukkit.getConsoleSender().sendMessage(ChatUtils.formatColor("#fff18dVersion: #ffffff" + plugin.version));
        Bukkit.getConsoleSender().sendMessage(ChatUtils.formatColor("#fff18dDeveloped by: #ffffff" + String.join(", ", plugin.getDescription().getAuthors())));
        Bukkit.getConsoleSender().sendMessage(ChatUtils.formatColor(" "));
    }
    public static void sendUpdateMessage() {
        String latestVersion = ModrinthUpdateChecker.isUpdateAvailable( plugin.version);

        if (!latestVersion.equals("false")) {
            Bukkit.getConsoleSender().sendMessage(ChatUtils.formatColor(" "));
            Bukkit.getConsoleSender().sendMessage(ChatUtils.formatColor("&9&lx&r&f&lSpawn &eNew Update Available!"));
            Bukkit.getConsoleSender().sendMessage(ChatUtils.formatColor("&cCurrent Version: &f" + plugin.version));
            Bukkit.getConsoleSender().sendMessage(ChatUtils.formatColor("&aLatest Version: &f" + latestVersion));
            Bukkit.getConsoleSender().sendMessage(ChatUtils.formatColor(" "));
            Bukkit.getConsoleSender().sendMessage(ChatUtils.formatColor("&e&lYou can download it at: &fhttps://modrinth.com/plugin/xspawn"));
            Bukkit.getConsoleSender().sendMessage(ChatUtils.formatColor(" "));
        }
    }
}
