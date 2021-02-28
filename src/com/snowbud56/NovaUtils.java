package com.snowbud56;

/*
 * Created by snowbud56 on July 23, 2019
 * Do not change or use this code without permission
 */

import com.snowbud56.randomtp.RandomTP;
import com.snowbud56.staffutils.StaffUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class NovaUtils extends JavaPlugin {

    private static NovaUtils instance;
    private static ArrayList<MiniPlugin> miniPlugins;

    @Override
    public void onEnable() {
        instance = this;
        NovaUtils.getInstance().getConfig().options().copyDefaults(true);
        NovaUtils.getInstance().saveDefaultConfig();
        new RandomTP(this);
        new StaffUtils(this);
//        Objects.requireNonNull(NovaUtils.getInstance().getCommand("rtp")).setExecutor(new WarpCommand());
//        Objects.requireNonNull(getServer().getPluginCommand("skin")).setExecutor(new SkinCommand());
//        Objects.requireNonNull(getServer().getPluginCommand("sc")).setExecutor(new StaffChatCommand());
//        getServer().getPluginManager().registerEvents(new SkinManager(), this);
//        getServer().getPluginManager().registerEvents(new WarpCommand(), this);
//        getServer().getPluginManager().registerEvents(new PortalManager(), this);
    }

    public static void addMiniPlugin(MiniPlugin plugin) {
        miniPlugins.add(plugin);
    }

    public static NovaUtils getInstance() {
        return instance;
    }
}
