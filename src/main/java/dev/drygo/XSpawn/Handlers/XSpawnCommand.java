package dev.drygo.XSpawn.Handlers;

import dev.drygo.XSpawn.Managers.ConfigManager;
import dev.drygo.XSpawn.Managers.SpawnManager;
import dev.drygo.XSpawn.Utils.ChatUtils;
import dev.drygo.XSpawn.Utils.LoadUtils;
import dev.drygo.XSpawn.XSpawn;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev.drygo.XTeams.API.XTeamsAPI;
import dev.drygo.XTeams.Models.Team;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class  XSpawnCommand implements CommandExecutor {
    private final XSpawn plugin;

    public XSpawnCommand(XSpawn plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (args.length < 1) {
            sender.sendMessage(ChatUtils.getMessage("error.unknown_command", null));
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "set" -> handleSet(sender, args, label);
            case "tp" -> handleTP(sender, args, label);
            case "del" -> handleDel(sender, args, label);
            case "reload" -> handleReload(sender, label, args);
            case "info" -> {
                if (!sender.hasPermission("xspawn.command.info") && !sender.hasPermission("xspawn.admin") && !sender.isOp()) {
                    sender.sendMessage(ChatUtils.getMessage("error.no_permission", null)
                            .replace("%command%", label + " " + String.join(" ", args)));
                    return true;
                }
                handleInfo(sender);
            }
            case "help" -> {
                if (!sender.hasPermission("xspawn.command.help") && !sender.hasPermission("xspawn.admin") && !sender.isOp()) {
                    sender.sendMessage(ChatUtils.getMessage("error.no_permission", null)
                            .replace("%command%", label + " " + String.join(" ", args)));
                    return true;
                }
                List<String> helpMessage = ConfigManager.getMessageConfig().getStringList("command.help");
                for (String line : helpMessage) {
                    sender.sendMessage(ChatUtils.formatColor(line));
                }
            }
            default -> sender.sendMessage(ChatUtils.getMessage("error.unknown_command", null));
        }
        return false;
    }

    private void handleSet(CommandSender sender, String[] args, String label) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatUtils.getMessage("error.only_player", null));
            return;
        }
        if (!sender.hasPermission("xspawn.command.set") && !sender.hasPermission("xspawn.admin") && !sender.isOp()) {
            sender.sendMessage(ChatUtils.getMessage("error.no_permission", null)
                    .replace("%command%", label + (args.length > 0 ? " " + String.join(" ", args) : "")));
            return;
        }
        if (args.length < 2) {
            sender.sendMessage(ChatUtils.getMessage("error.invalid_type", null));
            return;
        }

        String type = args[1];
        Location l = player.getLocation();

        switch (type) {
            case "first" -> {
                SpawnManager.setFirstSpawn(l);
                sender.sendMessage(ChatUtils.getMessage("command.set.first.success", null)
                        .replace("%location%", SpawnManager.locToString(l, true)));
            }
            case "team" -> {
                if (!plugin.isWorkingXTeams()) {
                    sender.sendMessage(ChatUtils.getMessage("error.xteams_not_loaded", null));
                    return;
                }

                if (args.length < 3) {
                    sender.sendMessage(ChatUtils.getMessage("error.team_not_specified", null));
                    return;
                }
                Team team = XTeamsAPI.getTeam(args[2]);
                if (team == null) {
                    sender.sendMessage(ChatUtils.getMessage("error.invalid_team", null)
                            .replace("%team%", args[2]));
                    return;
                }
                SpawnManager.setTeamSpawn(team, l);
                sender.sendMessage(ChatUtils.getMessage("command.set.team.success", null)
                        .replace("%team%", team.getName())
                        .replace("%location%", SpawnManager.locToString(l, true)));
            }
            case "player" -> {
                if (args.length < 3) {
                    sender.sendMessage(ChatUtils.getMessage("error.player_not_specified", null));
                    return;
                }

                String target = args[2];

                if (target.equals("*")) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        SpawnManager.setPlayerSpawn(p.getName(), l);
                    }
                    sender.sendMessage(ChatUtils.getMessage("command.set.player.all", null)
                            .replace("%location%", SpawnManager.locToString(l, true)));
                } else {
                    SpawnManager.setPlayerSpawn(target, l);
                    sender.sendMessage(ChatUtils.getMessage("command.set.player.success", null)
                            .replace("%location%", SpawnManager.locToString(l, true))
                            .replace("%target%", target));
                }
            }
            default -> sender.sendMessage(ChatUtils.getMessage("error.invalid_type", null));
        }
    }

    private void handleTP(CommandSender sender, String[] args, String label) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatUtils.getMessage("error.only_player", null));
            return;
        }
        if (!sender.hasPermission("xspawn.command.tp") && !sender.hasPermission("xspawn.admin") && !sender.isOp()) {
            sender.sendMessage(ChatUtils.getMessage("error.no_permission", null)
                    .replace("%command%", label + (args.length > 0 ? " " + String.join(" ", args) : "")));
            return;
        }
        if (args.length < 2) {
            sender.sendMessage(ChatUtils.getMessage("error.invalid_type", null));
            return;
        }
        String type = args[1];

        switch (type) {
            case "first" -> {
                Location l = SpawnManager.getFirstSpawn();
                if (l == null) {
                    l = player.getWorld().getSpawnLocation();
                    sender.sendMessage(ChatUtils.getMessage("command.tp.first.using_world_spawn", null));
                    teleport(player, l);
                } else {
                    sender.sendMessage(ChatUtils.getMessage("command.tp.first.success", null));
                    teleport(player, l);
                }
            }
            case "team" -> {
                if (!plugin.isWorkingXTeams()) {
                    sender.sendMessage(ChatUtils.getMessage("error.xteams_not_loaded", null));
                    return;
                }

                if (args.length < 3) {
                    sender.sendMessage(ChatUtils.getMessage("error.team_not_specified", null));
                    return;
                }
                Team team = XTeamsAPI.getTeam(args[2]);
                if (team == null) {
                    sender.sendMessage(ChatUtils.getMessage("error.invalid_team", null)
                            .replace("%team%", args[2]));
                    return;
                }
                Location l = SpawnManager.getTeamSpawn(XTeamsAPI.getTeam(args[2]));
                if (l != null) {
                    sender.sendMessage(ChatUtils.getMessage("command.tp.team.success", null)
                            .replace("%team%", team.getName()));
                    teleport(player, l);
                } else {
                    sender.sendMessage(ChatUtils.getMessage("error.team_not_defined", null)
                            .replace("%team%", team.getName()));
                }
            }
            case "player" -> {
                if (args.length < 3) {
                    sender.sendMessage(ChatUtils.getMessage("error.player_not_specified", null));
                    return;
                }
                String target = args[2];
                Location l = SpawnManager.getPlayerSpawn(target);
                if (l != null) {
                    sender.sendMessage(ChatUtils.getMessage("command.tp.player.success", null)
                            .replace("%target%", target));
                    teleport(player, l);
                } else {
                    sender.sendMessage(ChatUtils.getMessage("error.player_not_defined", null)
                            .replace("%target%", target));
                }
            }
            default -> sender.sendMessage(ChatUtils.getMessage("error.invalid_type", null));
        }
    }

    private void handleDel(CommandSender sender, String[] args, String label) {
        if (!sender.hasPermission("xspawn.command.del") && !sender.hasPermission("xspawn.admin") && !sender.isOp()) {
            sender.sendMessage(ChatUtils.getMessage("error.no_permission", null)
                    .replace("%command%", label + (args.length > 0 ? " " + String.join(" ", args) : "")));
            return;
        }
        if (args.length < 2) {
            sender.sendMessage(ChatUtils.getMessage("error.invalid_type", null));
            return;
        }
        String type = args[1];

        switch (type) {
            case "first" -> {
                SpawnManager.removeFirstSpawn();
                sender.sendMessage(ChatUtils.getMessage("command.del.first.success", null));
            }
            case "team" -> {
                if (!plugin.isWorkingXTeams()) {
                    sender.sendMessage(ChatUtils.getMessage("error.xteams_not_loaded", null));
                    return;
                }
                if (args.length < 3) {
                    sender.sendMessage(ChatUtils.getMessage("error.team_not_specified", null));
                    return;
                }
                Team team = XTeamsAPI.getTeam(args[2]);
                if (team == null) {
                    sender.sendMessage(ChatUtils.getMessage("error.invalid_team", null)
                            .replace("%team%", args[2]));
                    return;
                }

                Location l = SpawnManager.getTeamSpawn(team);
                if (l == null) {
                    sender.sendMessage(ChatUtils.getMessage("error.team_not_defined", null)
                            .replace("%team%", args[2]));
                    return;
                }

                SpawnManager.removeTeamSpawn(team);
                sender.sendMessage(ChatUtils.getMessage("command.del.team.success", null)
                        .replace("%team%", team.getName()));
            }
            case "player" -> {
                if (args.length < 3) {
                    sender.sendMessage(ChatUtils.getMessage("error.player_not_specified", null));
                    return;
                }

                String target = args[2];

                if (target.equals("*")) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        SpawnManager.removePlayerSpawn(p.getName());
                    }
                    sender.sendMessage(ChatUtils.getMessage("command.del.player.all", null));
                } else {

                    Location l = SpawnManager.getPlayerSpawn(target);
                    if (l == null) {
                        sender.sendMessage(ChatUtils.getMessage("error.player_not_defined", null)
                                .replace("%target%", target));
                        return;
                    }

                    SpawnManager.removePlayerSpawn(target);
                    sender.sendMessage(ChatUtils.getMessage("command.del.player.success", null)
                            .replace("%target%", target));
                }
            }
            default -> sender.sendMessage(ChatUtils.getMessage("error.invalid_type", null));
        }
    }
    private void handleReload(CommandSender sender, String label, String[] args) {
        if (!sender.hasPermission("xspawn.command.reload") && !sender.hasPermission("xspawn.admin") && !sender.isOp()) {
            sender.sendMessage(ChatUtils.getMessage("error.no_permission", null)
                    .replace("%command%", label + (args.length > 0 ? " " + String.join(" ", args) : "")));
            return;
        }

        Player target = (sender instanceof Player) ? (Player) sender : null;
        try {
            LoadUtils.loadFiles();
        } catch (Exception e) {
            sender.sendMessage(ChatUtils.getMessage("command.reload.error", target));
            return;
        }
        sender.sendMessage(ChatUtils.getMessage("command.reload.success", target));
    }

    private void teleport(Player p, Location l) {
        if (plugin.getConfig().getBoolean("settings.tp_to_spectator", false)) {
            p.setGameMode(GameMode.SPECTATOR);
        }
        p.teleport(l);
    }
    private boolean handleInfo(CommandSender sender) {
        sender.sendMessage(dev.drygo.XTeams.Utils.ChatUtils.formatColor("&7"));
        sender.sendMessage(dev.drygo.XTeams.Utils.ChatUtils.formatColor("&7"));
        sender.sendMessage(dev.drygo.XTeams.Utils.ChatUtils.formatColor("&8                           #5771ff&lx&r&f&lSpawn &8» &r&fInfo"));
        sender.sendMessage(dev.drygo.XTeams.Utils.ChatUtils.formatColor("&7"));
        sender.sendMessage(dev.drygo.XTeams.Utils.ChatUtils.formatColor("#fff18d&l                           ᴍᴀᴅᴇ ʙʏ"));
        sender.sendMessage(dev.drygo.XTeams.Utils.ChatUtils.formatColor("&f                           xDrygo #707070» &7&o(@eldrygo)"));
        sender.sendMessage(dev.drygo.XTeams.Utils.ChatUtils.formatColor("&7"));
        sender.sendMessage(dev.drygo.XTeams.Utils.ChatUtils.formatColor("#fff18d&l                  ʀᴜɴɴɪɴɢ ᴘʟᴜɢɪɴ ᴠᴇʀꜱɪᴏɴ"));
        sender.sendMessage(dev.drygo.XTeams.Utils.ChatUtils.formatColor("&f                                    " + plugin.version));
        sender.sendMessage(dev.drygo.XTeams.Utils.ChatUtils.formatColor("&7"));
        sender.sendMessage(dev.drygo.XTeams.Utils.ChatUtils.formatColor("#fff18d&l                      ᴠᴇʀꜱɪᴏɴ ᴄʜᴀɴɢᴇꜱ"));
        sender.sendMessage(dev.drygo.XTeams.Utils.ChatUtils.formatColor("&f            #7070703. #FFFAABFirst public version. Now in Modrinth."));
        sender.sendMessage(dev.drygo.XTeams.Utils.ChatUtils.formatColor("&f            #7070701. #FFFAABFirst, per player, per team spawn manager."));
        sender.sendMessage(dev.drygo.XTeams.Utils.ChatUtils.formatColor("&f            #7070702. #FFFAABUpdated xTeams version to 1.3.1"));
        sender.sendMessage(dev.drygo.XTeams.Utils.ChatUtils.formatColor("&7"));
        sender.sendMessage(dev.drygo.XTeams.Utils.ChatUtils.formatColor("#fff18d&l               ᴅʀʏɢᴏ'ꜱ ɴᴏᴛᴇ ᴏꜰ ᴛʜᴇ ᴠᴇʀꜱɪᴏɴ"));
        sender.sendMessage(dev.drygo.XTeams.Utils.ChatUtils.formatColor("&f  #FFFAAB             Welcome to xSpawn, this plugin is 99% made"));
        sender.sendMessage(dev.drygo.XTeams.Utils.ChatUtils.formatColor("&f  #FFFAAB             by the api, but the first spawn feature is cool."));
        sender.sendMessage(dev.drygo.XTeams.Utils.ChatUtils.formatColor("&7"));
        sender.sendMessage(dev.drygo.XTeams.Utils.ChatUtils.formatColor("&7"));
        return false;
    }
}
