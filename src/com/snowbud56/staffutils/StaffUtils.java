package com.snowbud56.staffutils;

/*
 * Created by snowbud56 on July 26, 2019
 * Do not change or use this code without permission
 */

import com.snowbud56.MiniPlugin;
import com.snowbud56.staffutils.command.StaffChatCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class StaffUtils extends MiniPlugin {

    public StaffUtils(JavaPlugin plugin) {
        super("StaffUtils", plugin);
    }

    @Override
    public void enable() {
    }

    @Override
    public void disable() {
        super.disable();
    }

    @Override
    public void addCommands() {
        addCommand(new StaffChatCommand());
    }
}
