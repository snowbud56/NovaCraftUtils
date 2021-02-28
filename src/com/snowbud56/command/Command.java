package com.snowbud56.command;

/*
* Created by snowbud56 on January 10, 2018
* Do not change or use this code without permission
*/

import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

public interface Command {

    void execute(Player p, String[] args);

    Collection<String> getAliases();

    String getPermission();

//    List<String> onTabComplete(CommandSender sender, String label, String[] args);

    void setAliasUsed(String s);

    boolean isDisabled();

    List<String> onTabComplete(Player p, String[] args);
}
