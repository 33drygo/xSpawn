package dev.drygo.XSpawn.Utils;

import dev.drygo.XSpawn.Managers.ConfigManager;
import dev.drygo.XSpawn.XSpawn;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatUtils {
    private static XSpawn plugin;
    private static final MiniMessage MINI = MiniMessage.miniMessage();

    public static void init(XSpawn plugin) {
        ChatUtils.plugin = plugin;
    }

    public static String formatColor(String message) {
        message = replaceHexColors(message);
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private static String replaceHexColors(String message) {
        Pattern hexPattern = Pattern.compile("#([A-Fa-f0-9]{6})");
        Matcher matcher = hexPattern.matcher(message);
        StringBuilder buffer = new StringBuilder();

        while (matcher.find()) {
            String hexColor = matcher.group(1);
            StringBuilder color = new StringBuilder("&x");
            for (char c : hexColor.toCharArray()) {
                color.append("&").append(c);
            }
            matcher.appendReplacement(buffer, color.toString());
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }
    public static String getMessage(String path, Player player) {

        String message = ConfigManager.getMessageConfig().isList(path)
                ? String.join("\n", ConfigManager.getMessageConfig().getStringList(path))
                : ConfigManager.getMessageConfig().getString(path);

        if (message == null || message.isEmpty()) {
            plugin.getLogger().warning("[WARNING] Message not found: " + path);
            return ChatUtils.formatColor("&r" + ConfigManager.getPrefix() + " #FF0000&l[ERROR] #FF3535Message not found: " + path);
        }

        message = message.replace("%player%", player != null ? player.getName() : "Unknown");

        /*
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            message = PlaceholderAPI.setPlaceholders(player, message);

         */

        message = message.replace("%prefix%", ConfigManager.getPrefix());

        return ChatUtils.formatColor(message);
    }

    public static Component getMiniMessage(String path, Player player, TagResolver... extraResolvers) {

        String message = ConfigManager.getMessageConfig().isList(path)
                ? String.join("\n", ConfigManager.getMessageConfig().getStringList(path))
                : ConfigManager.getMessageConfig().getString(path);

        if (message == null || message.isEmpty()) {
            plugin.getLogger().warning("[WARNING] Message not found: " + path);

            return MINI.deserialize(
                    "<reset><prefix> <red><bold>[ERROR]</bold> <dark_red>Message not found: <gray>" + path,
                    Placeholder.parsed("prefix", ConfigManager.getPrefix())
            );
        }

        /*
        if (player != null && Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            message = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, message);
        }
         */

        TagResolver baseResolvers = TagResolver.builder()
                .resolver(Placeholder.parsed("prefix", ConfigManager.getPrefix()))
                .resolver(Placeholder.parsed("player", player != null ? player.getName() : "Unknown"))
                .build();

        TagResolver resolver = TagResolver.builder()
                .resolver(baseResolvers)
                .resolvers(extraResolvers)
                .build();

        return MINI.deserialize(message, resolver);
    }
}
