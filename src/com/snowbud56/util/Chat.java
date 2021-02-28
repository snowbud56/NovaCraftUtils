package com.snowbud56.util;

/*
 * Created by snowbud56 on July 24, 2019
 * Do not change or use this code without permission
 */

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Chat {
    public static String cAqua = "" + ChatColor.AQUA;
    public static String cBlack = "" + ChatColor.BLACK;
    public static String cBlue = "" + ChatColor.BLUE;
    public static String cDAqua = "" + ChatColor.DARK_AQUA;
    public static String cDBlue = "" + ChatColor.DARK_BLUE;
    public static String cDGray = "" + ChatColor.DARK_GRAY;
    public static String cDGreen = "" + ChatColor.DARK_GREEN;
    public static String cDPurple = "" + ChatColor.DARK_PURPLE;
    public static String cDRed = "" + ChatColor.DARK_RED;
    public static String cGold = "" + ChatColor.GOLD;
    public static String cGray = "" + ChatColor.GRAY;
    public static String cGreen = "" + ChatColor.GREEN;
    public static String cPurple = "" + ChatColor.LIGHT_PURPLE;
    public static String cRed = "" + ChatColor.RED;
    public static String cWhite = "" + ChatColor.WHITE;
    public static String cYellow = "" + ChatColor.YELLOW;
    public static String mHead = "" + ChatColor.BLUE;
    public static String mBody = "" + ChatColor.GRAY;
    public static String mChat = "" + ChatColor.WHITE;
    public static String mElem = "" + ChatColor.YELLOW;

    public static void messagePermission(String message, String permission) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission(permission)) {
                player.sendMessage(message);
            }
        }
    }
}
