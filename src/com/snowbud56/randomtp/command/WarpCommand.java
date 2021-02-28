package com.snowbud56.randomtp.command;

/*
 * Created by snowbud56 on July 01, 2019
 * Do not change or use this code without permission
 */

import com.snowbud56.command.CommandBase;
import com.snowbud56.randomtp.TPManager;
import org.bukkit.entity.Player;

public class WarpCommand extends CommandBase {

    public WarpCommand() {
        super(null, "rtp");
    }

    @Override
    public void execute(Player p, String[] args) {
        TPManager.teleportPlayer(p);
    }
}