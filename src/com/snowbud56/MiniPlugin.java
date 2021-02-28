package com.snowbud56;

/*
 * Created by snowbud56 on July 26, 2019
 * Do not change or use this code without permission
 */

import com.snowbud56.command.Command;
import com.snowbud56.command.CommandManager;
import com.snowbud56.util.TimeUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class MiniPlugin implements Listener {
    protected String pluginName;

    protected JavaPlugin plugin;

    protected Map<String, Command> _commands;
    protected YamlConfiguration config;
    protected String dataFolder;

    public MiniPlugin(String pluginName, JavaPlugin plugin) {
        this.pluginName = pluginName;
        this.plugin = plugin;
        this._commands = new HashMap<>();
        dataFolder = plugin.getDataFolder() + "/" + pluginName;
        onEnable();
        registerEvents(this);
        NovaUtils.addMiniPlugin(this);
    }

    public YamlConfiguration getConfig() {
        if (config == null) {
            reloadConfig();
        }
        return config;
    }

    public String getDataFolder() {
        return dataFolder;
    }

    public void reloadConfig() {
        try {
            File file = new File(dataFolder, "config.yml");
            if (!file.exists()) file.createNewFile();
            config = YamlConfiguration.loadConfiguration(file);
        } catch (IOException e) {
            Log("Unable to load the config file for " + pluginName + "!");
        }
    }

    public boolean saveConfig() {
        try {
            config.save(new File(dataFolder, "config.yml"));
            return true;
        } catch (IOException e) {
            Log("Unable to save the config file for " + pluginName + "!");
            return false;
        }
    }

    public PluginManager getPluginManager() {
        return this.plugin.getServer().getPluginManager();
    }

    public BukkitScheduler getScheduler() {
        return this.plugin.getServer().getScheduler();
    }

    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    public void registerEvents(Listener listener) {
        this.plugin.getServer().getPluginManager().registerEvents(listener, this.plugin);
    }

    public final void onEnable() {
        long startTime = System.currentTimeMillis();
        Log("Initializing...");
        enable();
        addCommands();
        Log("Enabled in " + TimeUtil.convertmstoTime(System.currentTimeMillis() - startTime) + ".");
    }

    public final void onDisable() {
        disable();
        Log("Disabled.");
    }

    public void enable() {}

    public void disable() {}

    public void addCommands() {}

    public final String getName()
    {
        return this.pluginName;
    }

    public final void addCommand(Command command) {
        CommandManager.instance.addCommand(command);
    }

    public final void removeCommand(Command command) {
        CommandManager.instance.removeCommand(command);
    }

    protected void Log(String message) {
        System.out.println("[" + pluginName + "] " + message);
    }
}
