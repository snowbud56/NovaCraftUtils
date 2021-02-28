package com.snowbud56.portals;

/*
 * Created by snowbud56 on July 01, 2019
 * Do not change or use this code without permission
 */

import com.snowbud56.NovaUtils;
import com.snowbud56.randomtp.RandomTP;
import com.snowbud56.randomtp.TPManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PortalManager implements Listener {

    private static Location portal_base;
    private static int radius = 5;

    public static void initiate() {
        FileConfiguration config = NovaUtils.getInstance().getConfig();
        if (!config.getBoolean("portal_enabled")) return;
        String[] coords = NovaUtils.getInstance().getConfig().getString("portal_location").split(",");
        double x, y, z;
        try {
            x = Double.parseDouble(coords[0].split(":")[1]);
            y = Double.parseDouble(coords[1].split(":")[1]);
            z = Double.parseDouble(coords[2].split(":")[1]);
        } catch (IllegalArgumentException e) {
            System.out.println("[RandomTP] The portal location is invalid! The portal won't work.");
            return;
        }
        World world = RandomTP.getPortalWorld();
        if (world == null) {
            System.out.println("[RandomTP] The portal world is invalid! The portal won't work.");
            return;
        }
        portal_base = new Location(world, x, y, z);
    }

    @EventHandler
    public void playerPortalEnter(PlayerMoveEvent e) {
        if (PortalManager.portal_base == null || e.getTo() == null) return;
        Player p = e.getPlayer();
        if (e.getTo().getBlock() == e.getFrom().getBlock()) return;
        if (e.getTo().getBlock().getType() == Material.NETHER_PORTAL || e.getTo().getBlock().getType() == Material.END_PORTAL) {
            if (p.getWorld().equals(PortalManager.portal_base.getWorld()) && p.getLocation().distance(PortalManager.portal_base) <= radius) {
                TPManager.teleportPlayer(p);
            }
        }
    }
}
