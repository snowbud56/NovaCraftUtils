package com.snowbud56.command;

/*
* Created by snowbud56 on January 10, 2018
* Do not change or use this code without permission
*/

import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public abstract class CommandBase implements Command {

    private List<String> aliases;
    private String permission;
    protected String aliasUsed;
    private boolean isDisabled = false;

    public CommandBase(String permission, String...aliases) {
        this.permission = permission;
        this.aliases = Arrays.asList(aliases);
    }

    @Override
    public Collection<String> getAliases() {
        return aliases;
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public void setAliasUsed(String alias) {
        this.aliasUsed = alias;
    }

    @Override
    public List<String> onTabComplete(Player p, String[] args) {
        return null;
    }

    @Override
    public boolean isDisabled() {
        return false;
    }
}
