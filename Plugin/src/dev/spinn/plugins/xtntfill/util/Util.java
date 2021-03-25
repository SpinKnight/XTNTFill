package dev.spinn.plugins.xtntfill.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Util {

    public static void sendMessage(CommandSender sender, String msg) {
        for (String line : msg.split("\\\\n")) {
            sender.sendMessage(colorize(line).replace("\\\\n", "\n"));
        }
    }

    public static String colorize(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}
