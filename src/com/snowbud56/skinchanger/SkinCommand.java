package com.snowbud56.skinchanger;

/*
 * Created by snowbud56 on June 25, 2019
 * Do not change or use this code without permission
 */

import com.mojang.authlib.GameProfile;
import com.snowbud56.command.CommandBase;
import com.snowbud56.util.TimeUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkinCommand extends CommandBase {

    private Map<UUID, Long> commandCoolDowns = new HashMap<>();
    private static long commandCooldown = 3600000L;

    public SkinCommand() {
        super("skinchchanger.use");
    }

    @Override
    public void execute(Player p, String[] args) {
        if (args.length == 0) {
            p.sendMessage(ChatColor.RED + "You need a player name for that! Usage: /skin <player>");
            return;
        }
        System.out.println(commandCoolDowns.getOrDefault(p.getUniqueId(), 0L) - System.currentTimeMillis());
        if ((commandCoolDowns.getOrDefault(p.getUniqueId(), 0L) <= System.currentTimeMillis()) || (p.hasPermission("skinchanger.admin")) || p.isOp()) {
            GameProfile gameProfile = SkinManager.setupGameProfile(p, args[0]);
            SkinManager.setPlayerSkinToGameProfile(p, gameProfile);
            p.sendMessage(ChatColor.GREEN + "You now have the skin of " + ChatColor.RED + args[0] + ChatColor.GREEN + "!");
            commandCoolDowns.put(p.getUniqueId(), System.currentTimeMillis() + commandCooldown);
        } else {
            p.sendMessage(ChatColor.RED + "You cannot change your skin for another " + ChatColor.DARK_RED + TimeUtil.convertmstoTime(commandCoolDowns.get(p.getUniqueId()) - System.currentTimeMillis()));
        }
    }
}
