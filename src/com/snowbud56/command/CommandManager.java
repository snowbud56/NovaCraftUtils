package com.snowbud56.command;

/*
* Created by snowbud56 on January 10, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.util.Chat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.HashMap;

public class CommandManager implements Listener {
    public static CommandManager instance;

    private static HashMap<String, Command> commands;

    public CommandManager() {
        instance = this;
        commands = new HashMap<>();
    }

    @EventHandler
    public void preProcessCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        String commandName = e.getMessage().substring(1);
        String[] args = null;
        if (commandName.contains(" ")) {
            commandName = commandName.split(" ")[0];
            args = e.getMessage().substring(e.getMessage().indexOf(' ') + 1).split(" ");
        }
//        if (commandName.toLowerCase().startsWith("bukkit:") || commandName.toLowerCase().startsWith("minecraft:") ||
////                commandName.toLowerCase().equals("plugin") || commandName.toLowerCase().equals("plugins") || commandName.toLowerCase().equals("pl") || commandName.toLowerCase().equals("ver") || commandName.toLowerCase().equals("version")) {
////            e.setCancelled(true);
////            e.getPlayer().sendMessage(Chat.cRed + "That command has been disabled.");
////            return;
////        }
        Command command = commands.get(commandName.toLowerCase());
        if (command != null) {
            if (command.isDisabled()) {
                p.sendMessage(Chat.cRed + "That command has been temporarily disabled.");
                return;
            }
            e.setCancelled(true);
            if (args == null) args = new String[] {};
            if (p.hasPermission(command.getPermission()) || command.getPermission() == null) {
                command.setAliasUsed(commandName.toLowerCase());
                command.execute(e.getPlayer(), args);
            } else {
                p.sendMessage(Chat.cRed + "You don't have permission to execute this command!");
            }
        }
    }

    public void addCommand(Command cmd) {
        for (String alias : cmd.getAliases()) {
            commands.put(alias.toLowerCase(), cmd);
        }
    }

    public void removeCommand(Command cmd) {
        for (String alias : cmd.getAliases()) {
            commands.remove(alias);
        }
    }
}
