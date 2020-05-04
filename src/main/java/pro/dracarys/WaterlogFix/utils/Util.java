package pro.dracarys.WaterlogFix.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import pro.dracarys.WaterlogFix.WaterlogFix;

public class Util {

    public static void sendConsole(String str) {
        Bukkit.getConsoleSender().sendMessage(color(str));
    }

    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static boolean isEnabledWorld(String worldName) {
        boolean output = WaterlogFix.getInstance().getConfig().getStringList("Settings.enabled-worlds").stream().anyMatch(worldName::equalsIgnoreCase);
        if (WaterlogFix.getInstance().getConfig().getBoolean("Settings.make-enabled-worlds-a-blacklist"))
            return !output;
        return output;
    }

}