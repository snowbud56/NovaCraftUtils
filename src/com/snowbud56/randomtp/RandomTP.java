package com.snowbud56.randomtp;

/*
 * Created by snowbud56 on July 23, 2019
 * Do not change or use this code without permission
 */

import com.snowbud56.MiniPlugin;
import com.snowbud56.NovaUtils;
import com.snowbud56.randomtp.command.WarpCommand;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class RandomTP extends MiniPlugin {

    public static World portalWorld;
    public static World teleportWorld;

    public RandomTP(JavaPlugin plugin) {
        super("RandomTP", plugin);
    }

    @Override
    public void enable() {
        portalWorld = Bukkit.getWorld(Objects.requireNonNull(NovaUtils.getInstance().getConfig().getString("portal_world")));
        teleportWorld = Bukkit.getWorld(Objects.requireNonNull(NovaUtils.getInstance().getConfig().getString("survival_world")));
        if (NovaUtils.getInstance().getConfig().getBoolean("portal_enabled")) com.snowbud56.portals.PortalManager.initiate();
        if (portalWorld == null) System.out.println("Portal World isn't set properly!");
        if (teleportWorld == null) System.out.println("Teleport World isn't set properly!");
    }

    @Override
    public void addCommands() {
        addCommand(new WarpCommand());
    }

    public static World getPortalWorld() {
        return portalWorld;
    }

    public static World getTeleportWorld() {
        return teleportWorld;
    }
}