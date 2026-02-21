package dev.drygo.XSpawn;

import dev.drygo.XSpawn.API.XSpawnAPI;
import dev.drygo.XSpawn.Managers.ConfigManager;
import dev.drygo.XSpawn.Managers.SpawnManager;
import dev.drygo.XSpawn.Utils.ChatUtils;
import dev.drygo.XSpawn.Utils.LoadUtils;
import dev.drygo.XSpawn.Utils.LogsUtils;
import org.bukkit.plugin.java.JavaPlugin;

public class XSpawn extends JavaPlugin {
    public String prefix;
    public String version;
    public boolean workingXTeams;

    @Override
    public void onEnable() {
        version = getDescription().getVersion();
        workingXTeams = false;
        LogsUtils.init(this);
        ConfigManager.init(this);
        ChatUtils.init(this);
        SpawnManager.init(this);
        LoadUtils.init(this);
        XSpawnAPI.init();

        LoadUtils.loadFeatures();
        LogsUtils.sendStartupMessage();
    }

    @Override
    public void onDisable() {
        LogsUtils.sendShutdownMessage();
    }

    public boolean isWorkingXTeams() {
        return workingXTeams;
    }
}
