package pro.dracarys.WaterlogFix;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import pro.dracarys.WaterlogFix.commands.MainCommand;
import pro.dracarys.WaterlogFix.listeners.ExplosionListener;
import pro.dracarys.WaterlogFix.utils.Util;

public class WaterlogFix extends JavaPlugin {

    private static WaterlogFix plugin;

    public static WaterlogFix getInstance() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        checkServerVersion();
        if (getServerVersion() < 13) {
            Util.sendConsole("&cYour Server version (1." + ver + ") is not compatible with this plugin! Only 1.13+ supported! Disabling...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        } else if (getServerVersion() > 14) {
            Util.sendConsole("&e[WaterlogFix] Your Server version (1." + ver + ") should be compatible but it's untested!");
        } else {
            Util.sendConsole("&a[WaterlogFix] Your Server version (1." + ver + ") is compatible &4❤");
        }
        saveDefaultConfig();
        PluginCommand cmd = this.getCommand("waterlogfix");
        MainCommand executor = new MainCommand();
        try {
            assert cmd != null;
            cmd.setExecutor(executor);
            cmd.setTabCompleter(executor);
        } catch (NullPointerException npe) {
            Util.sendConsole("&c[WaterlogFix] Error while registering /waterlogfix command! Disabling...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        try {
            registerListeners(new ExplosionListener());
        } catch (Exception ex) {
            Util.sendConsole("&c[WaterlogFix] Error while registering ExplosionListener! Disabling...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        printPluginInfo();
    }

    @Override
    public void onDisable() {
        Bukkit.getServer().getScheduler().cancelTasks(this);
        plugin = null;
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }

    private void printPluginInfo() {
        Util.sendConsole("&f▆ &f▆ &f▆ &f▆&f▆&f▆&f▆&f▆&f▆&f▆&f▆&f▆&f▆&f▆ &f▆ &f▆ &f▆");
        Util.sendConsole(" ");
        Util.sendConsole("&f➤  &c" + getDescription().getName() + " &7v" + getDescription().getVersion() + "&a Enabled ✔");
        Util.sendConsole("&f➤  &f&o" + getDescription().getDescription());
        Util.sendConsole("&f➤ &eMade with &4❤ &eby &f" + getDescription().getAuthors().get(0));
        if (getDescription().getVersion().contains("-DEV"))
            Util.sendConsole("&f➤ &cThis is a BETA, report any unexpected behaviour to the Author!");
        Util.sendConsole(" ");
        Util.sendConsole("&f▆ &f▆ &f▆ &f▆&f▆&f▆&f▆&f▆&f▆&f▆&f▆&f▆&f▆&f▆ &f▆ &f▆ &f▆");
    }

    private static int ver;

    private static void checkServerVersion() {
        ver = Integer.parseInt(Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3].replace("1_", "").substring(1).replaceAll("_R\\d", ""));
    }

    private static int getServerVersion() {
        return ver;
    }
}
