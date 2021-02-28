package com.snowbud56.randomtp;

/*
 * Created by snowbud56 on July 01, 2019
 * Do not change or use this code without permission
 */

import com.snowbud56.NovaUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.Random;

public class TPManager {

    public static void teleportPlayer(Player p) {
        FileConfiguration config = NovaUtils.getInstance().getConfig();
        int minX = config.getInt("x_min");
        int maxX = config.getInt("x_max");
        int minZ = config.getInt("z_min");
        int maxZ = config.getInt("z_max");
        Random r = new Random();
        int x = r.nextInt(maxX - minX) + minX;
        int z = r.nextInt(maxZ - minZ) + minZ;
        int y = 255;
        boolean foundY = false;
        Location location = new Location(RandomTP.getTeleportWorld(), x, y, z);
        Objects.requireNonNull(location.getWorld()).loadChunk(location.getChunk());
        while (!foundY) {
            if (location.getBlock().getType() != Material.AIR) {
                foundY = true;
                p.teleport(location.add(0.5, 2,0.5));
            } else {
                location.add(0, -1, 0);
            }
            if (location.getY() <= 0) {
                teleportPlayer(p);
                return;
            }
        }
    }
}
