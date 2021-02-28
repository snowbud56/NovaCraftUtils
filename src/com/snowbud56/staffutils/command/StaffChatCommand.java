package com.snowbud56.staffutils.command;

/*
 * Created by snowbud56 on July 24, 2019
 * Do not change or use this code without permission
 */

import com.snowbud56.command.CommandBase;
import com.snowbud56.staffutils.StaffUtils;
import com.snowbud56.util.Chat;
import org.bukkit.entity.Player;

public class StaffChatCommand extends CommandBase {

    public StaffChatCommand() {
        super("staffutils.use","sc", "staffutils");
    }

    @Override
    public void execute(Player p, String[] args) {
        if (args.length == 0) {
            p.sendMessage(Chat.cRed + "Usage: /" + aliasUsed + " <msg>");
            return;
        }
        StringBuilder msg = new StringBuilder();
        for (String arg : args)
            msg.append(arg).append(" ");
        Chat.messagePermission(Chat.cDRed + "STAFF> " + Chat.cRed + p.getName() + ": " + Chat.cAqua + msg.toString(), "staffutils.use");
        return;
    }
}
