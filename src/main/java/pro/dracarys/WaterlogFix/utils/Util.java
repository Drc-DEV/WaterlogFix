package pro.dracarys.WaterlogFix.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Util {

    public static void sendConsole(String str) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', str));
    }

    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}