package com.snowbud56.skinchanger;

/*
 * Created by snowbud56 on June 25, 2019
 * Do not change or use this code without permission
 */

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.util.UUIDTypeAdapter;
import com.snowbud56.NovaUtils;
import com.snowbud56.util.managers.NMSManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkinManager implements Listener {

    public static Map<Player, GameProfile> playerGameProfiles = new HashMap<>();

    public static GameProfile setupGameProfile(Player player, String skinName) {
        String value, signature;
        GameProfile gp = new GameProfile(player.getUniqueId(), player.getName());
        UUID uuid = Bukkit.getOfflinePlayer(skinName).getUniqueId();
        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL(String.format("https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false", UUIDTypeAdapter.fromUUID(uuid))).openConnection();
            if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                String reply = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
                value = reply.split("\"value\":\"")[1].split("\"")[0];
                signature = reply.split("\"signature\":\"")[1].split("\"")[0];
                gp.getProperties().clear();
                gp.getProperties().put(player.getName(), new Property("textures", value, signature));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        playerGameProfiles.put(player, gp);
        return gp;
    }

    public static void setPlayerSkinToGameProfile(Player p, GameProfile gp) {
        try {
            Class<?> craftWorldClass = NMSManager.getCraftBukkitClass("CraftWorld");
            Object converted = craftWorldClass.cast(p.getWorld());
            Method handle = converted.getClass().getMethod("getHandle");
            Object worldServObj = handle.invoke(converted);
            Object convertedToWorldServer = NMSManager.getNMSClass("WorldServer").cast(worldServObj);
            Class<?> entityPlayerClass = NMSManager.getNMSClass("EntityPlayer");
            Constructor<?> entityPlayerCon = entityPlayerClass.getConstructor(NMSManager.getNMSClass("MinecraftServer"), NMSManager.getNMSClass("WorldServer"), GameProfile.class, NMSManager.getNMSClass("PlayerInteractManager"));

            Class<?> craftServerClass = NMSManager.getCraftBukkitClass("CraftServer");

            Object craftServerConverted = craftServerClass.cast(Bukkit.getServer());
            Method getServerMethod = NMSManager.getCraftBukkitClass("CraftServer").getMethod("getServer");
            Object minecraftServer = getServerMethod.invoke(craftServerConverted);

            Class<?> PIMClass = NMSManager.getNMSClass("PlayerInteractManager");
            Constructor<?> PIMConstructor = PIMClass.getConstructor(NMSManager.getNMSClass("WorldServer"));
            Object PIMObject = PIMConstructor.newInstance(convertedToWorldServer);

            Object entityPlayer = entityPlayerCon.newInstance(minecraftServer, convertedToWorldServer, gp, PIMObject);

            Class<?> craftPlayerClass = NMSManager.getCraftBukkitClass("entity.CraftPlayer");
            Object cPconverted = craftPlayerClass.cast(p);
            Method playerHandleMethod = cPconverted.getClass().getMethod("getHandle");
            Object playerHandle = playerHandleMethod.invoke(cPconverted);

            for (Player target : Bukkit.getOnlinePlayers()) {
                Object targetConnection = NMSManager.getConnection(target);
                Class<?> packetPlayerInfoClass = NMSManager.getNMSClass("PacketPlayOutPlayerInfo");
                Class<?> playerInfoActionEnum = NMSManager.getNMSClass("PacketPlayOutPlayerInfo$EnumPlayerInfoAction");
                Constructor<?> packetPlayerInfoConstructor = packetPlayerInfoClass.getConstructor(playerInfoActionEnum, Iterable.class);
                Object packetRemovePlayer = packetPlayerInfoConstructor.newInstance(playerInfoActionEnum.getEnumConstants()[4], Collections.singletonList(playerHandle));
                Object packetAddPlayer = packetPlayerInfoConstructor.newInstance(playerInfoActionEnum.getEnumConstants()[0], Collections.singletonList(entityPlayer));

                Method sendPacket = targetConnection.getClass().getMethod("sendPacket", NMSManager.getNMSClass("Packet"));
                sendPacket.invoke(targetConnection, packetRemovePlayer);
                sendPacket.invoke(targetConnection, packetAddPlayer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Location loc = p.getLocation();
        double playerHealth = p.getHealth();
        int playerHunger = p.getFoodLevel();
        p.teleport(new Location(Bukkit.getWorld((p.getWorld() == Bukkit.getWorld("world_nether") ? "world" : "world_nether")), 0, -5, 0));
        new BukkitRunnable() {
            @Override
            public void run() {
                p.teleport(loc);
                p.setHealth(playerHealth);
                p.setFoodLevel(playerHunger);
            }
        }.runTaskLater(NovaUtils.getInstance(), 4L);
    }

    private static boolean shouldUpdatePlayer(Player p) {
        for (Map.Entry<Player, GameProfile> disguise : playerGameProfiles.entrySet()) {
            if (disguise.getKey().getWorld() == p.getWorld()) {
                if (disguise.getKey().getLocation().distance(p.getLocation()) < 250) {
                    return true;
                }
            }
        }
        return false;
    }

    @EventHandler
    public void skinUpdateOnJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        playerGameProfiles.remove(p);
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<Player, GameProfile> disguise : playerGameProfiles.entrySet()) {
                    try {
                        //Craft World and Server
                        Class<?> craftWorldClass = NMSManager.getCraftBukkitClass("CraftWorld");
                        Object converted = craftWorldClass.cast(p.getWorld());
                        Method handle = converted.getClass().getMethod("getHandle");
                        Object worldServObj = handle.invoke(converted);
                        Object convertedToWorldServer = NMSManager.getNMSClass("WorldServer").cast(worldServObj);

                        //EntityPlayer
                        Class<?> entityPlayerClass = NMSManager.getNMSClass("EntityPlayer");
                        Constructor<?> entityPlayerCon = entityPlayerClass.getConstructor(NMSManager.getNMSClass("MinecraftServer"), NMSManager.getNMSClass("WorldServer"), GameProfile.class, NMSManager.getNMSClass("PlayerInteractManager"));

                        Class<?> craftServerClass = NMSManager.getCraftBukkitClass("CraftServer");

                        Object craftServerConverted = craftServerClass.cast(Bukkit.getServer());
                        Method getServerMethod = NMSManager.getCraftBukkitClass("CraftServer").getMethod("getServer");
                        Object minecraftServer = getServerMethod.invoke(craftServerConverted);

                        Class<?> PIMClass = NMSManager.getNMSClass("PlayerInteractManager");
                        Constructor<?> PIMConstructor = PIMClass.getConstructor(NMSManager.getNMSClass("WorldServer"));
                        Object PIMObject = PIMConstructor.newInstance(convertedToWorldServer);

                        Object entityPlayer = entityPlayerCon.newInstance(minecraftServer, convertedToWorldServer, disguise.getValue(), PIMObject);

                        //Setting up craft player
                        Class<?> craftPlayerClass = NMSManager.getCraftBukkitClass("entity.CraftPlayer");
                        Object cPconverted = craftPlayerClass.cast(disguise.getKey());
                        Method targetHandleMethod = cPconverted.getClass().getMethod("getHandle");
                        Object targetHandle = targetHandleMethod.invoke(cPconverted);

                        //Setting up the packets
                        Object playerConnection = NMSManager.getConnection(p);
                        Class<?> packetPlayerInfoClass = NMSManager.getNMSClass("PacketPlayOutPlayerInfo");
                        Class<?> playerInfoActionEnum = NMSManager.getNMSClass("PacketPlayOutPlayerInfo$EnumPlayerInfoAction");
                        Constructor<?> packetPlayerInfoConstructor = packetPlayerInfoClass.getConstructor(playerInfoActionEnum, Iterable.class);
                        Object packetRemovePlayer = packetPlayerInfoConstructor.newInstance(playerInfoActionEnum.getEnumConstants()[4], Collections.singletonList(targetHandle));
                        Object packetAddPlayer = packetPlayerInfoConstructor.newInstance(playerInfoActionEnum.getEnumConstants()[0], Collections.singletonList(entityPlayer));

                        //Sending the packets
                        Method sendPacket = playerConnection.getClass().getMethod("sendPacket", NMSManager.getNMSClass("Packet"));
                        sendPacket.invoke(playerConnection, packetRemovePlayer);
                        sendPacket.invoke(playerConnection, packetAddPlayer);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println(disguise.getKey().getName() + "'s disguise has been updated for " + p.getName());
                }
            }
        }.runTaskLater(NovaUtils.getInstance(), 3L);
        if (SkinManager.shouldUpdatePlayer(p)) {
            Location loc = p.getLocation();
            p.teleport(new Location(Bukkit.getWorld((p.getWorld() == Bukkit.getWorld("world_nether") ? "world" : "world_nether")), 0, 1, 0));
            new BukkitRunnable() {
                @Override
                public void run() {
                    p.teleport(loc);
                    p.setHealth(p.getMaxHealth());
                }
            }.runTaskLater(NovaUtils.getInstance(), 5L);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (playerGameProfiles.containsKey(e.getPlayer())) {
            for (Player target : Bukkit.getOnlinePlayers()) {
                try {
                    Class<?> craftWorldClass = NMSManager.getCraftBukkitClass("CraftWorld");
                    Object converted = craftWorldClass.cast(e.getPlayer().getWorld());
                    Method handle = converted.getClass().getMethod("getHandle");
                    Object worldServObj = handle.invoke(converted);
                    Object convertedToWorldServer = NMSManager.getNMSClass("WorldServer").cast(worldServObj);

                    //EntityPlayer
                    Class<?> entityPlayerClass = NMSManager.getNMSClass("EntityPlayer");
                    Constructor<?> entityPlayerCon = entityPlayerClass.getConstructor(NMSManager.getNMSClass("MinecraftServer"), NMSManager.getNMSClass("WorldServer"), GameProfile.class, NMSManager.getNMSClass("PlayerInteractManager"));

                    Class<?> craftServerClass = NMSManager.getCraftBukkitClass("CraftServer");

                    Object craftServerConverted = craftServerClass.cast(Bukkit.getServer());
                    Method getServerMethod = NMSManager.getCraftBukkitClass("CraftServer").getMethod("getServer");
                    Object minecraftServer = getServerMethod.invoke(craftServerConverted);

                    Class<?> PIMClass = NMSManager.getNMSClass("PlayerInteractManager");
                    Constructor<?> PIMConstructor = PIMClass.getConstructor(NMSManager.getNMSClass("WorldServer"));
                    Object PIMObject = PIMConstructor.newInstance(convertedToWorldServer);

                    Object entityPlayer = entityPlayerCon.newInstance(minecraftServer, convertedToWorldServer, playerGameProfiles.get(e.getPlayer()), PIMObject);

                    //Setting up craft player
                    Class<?> craftPlayerClass = NMSManager.getCraftBukkitClass("entity.CraftPlayer");
                    Object cPconverted = craftPlayerClass.cast(e.getPlayer());
                    Method targetHandleMethod = cPconverted.getClass().getMethod("getHandle");
                    Object targetHandle = targetHandleMethod.invoke(cPconverted);

                    //Setting up the packets
                    Object playerConnection = NMSManager.getConnection(target);
                    Class<?> packetPlayerInfoClass = NMSManager.getNMSClass("PacketPlayOutPlayerInfo");
                    Class<?> playerInfoActionEnum = NMSManager.getNMSClass("PacketPlayOutPlayerInfo$EnumPlayerInfoAction");
                    Constructor<?> packetPlayerInfoConstructor = packetPlayerInfoClass.getConstructor(playerInfoActionEnum, Iterable.class);
                    Object packetRemovePlayer = packetPlayerInfoConstructor.newInstance(playerInfoActionEnum.getEnumConstants()[4], Collections.singletonList(entityPlayer));

                    //Sending the packets
                    Method sendPacket = playerConnection.getClass().getMethod("sendPacket", NMSManager.getNMSClass("Packet"));
                    sendPacket.invoke(playerConnection, packetRemovePlayer);
                } catch (Exception er) {
                    er.printStackTrace();
                }
            }
        }
        playerGameProfiles.remove(e.getPlayer());
    }
}
