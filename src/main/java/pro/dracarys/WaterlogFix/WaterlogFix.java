package pro.dracarys.WaterlogFix;

import com.tchristofferson.configupdater.ConfigUpdater;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import pro.dracarys.WaterlogFix.commands.MainCommand;
import pro.dracarys.WaterlogFix.listeners.ExplosionListener;
import pro.dracarys.WaterlogFix.utils.Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

public class WaterlogFix extends JavaPlugin {

    private static WaterlogFix plugin;

    public static WaterlogFix getInstance() {
        return plugin;
    }

    @Override
    public void onEnable() {
        try {
            int pluginId = 7322;
            new Metrics(this, pluginId);
        } catch (Exception ex) {
            getServer().getLogger().log(Level.SEVERE, "Error while trying to register Metrics (bStats)");
        }
        plugin = this;
        checkServerVersion();
        if (getServerVersion() < 13) {
            Util.sendConsole("&cYour Server version (1." + ver + ") is not compatible with this plugin! Only 1.13+ supported! Disabling...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        } else if (getServerVersion() > 17) {
            Util.sendConsole("&e[WaterlogFix] Your Server version (1." + ver + ") should be compatible but it's untested!");
        } else {
            Util.sendConsole("&a[WaterlogFix] Your Server version (1." + ver + ") is compatible");
        }
        saveDefaultConfig();
        File configFile = new File(getDataFolder(), "config.yml");
        try {
            ConfigUpdater.update(plugin, "config.yml", configFile, new ArrayList<>());
        } catch (IOException e) {
            e.printStackTrace();
        }

        PluginCommand cmd = this.getCommand("waterlogfix");
        MainCommand executor = new MainCommand();
        try {
            assert cmd != null;
            cmd.setExecutor(executor);
            cmd.setTabCompleter(executor);
        } catch (NullPointerException ex) {
            getServer().getLogger().log(Level.SEVERE, Util.color("&c[WaterlogFix] Error while registering /waterlogfix command! Disabling..."), ex);
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        try {
            registerListeners(new ExplosionListener());
        } catch (NoSuchMethodError ex) {
            getServer().getLogger().log(Level.SEVERE, Util.color("&c[WaterlogFix] Error while registering ExplosionListener! Disabling..."), ex);
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
        Util.sendConsole("&f<> &f<> &f<> &f<>&f<>&f<>&f<>&f<>&f<>&f<>&f<>&f<>&f<>&f<> &f<> &f<> &f<>");
        Util.sendConsole(" ");
        Util.sendConsole("&f-  &c" + getDescription().getName() + " &7v" + getDescription().getVersion() + "&a Enabled");
        Util.sendConsole("&f-  &f&o" + getDescription().getDescription());
        Util.sendConsole("&f- &eMade with &4♥ &eby &f" + getDescription().getAuthors().get(0));
        if (getDescription().getVersion().contains("-DEV"))
            Util.sendConsole("&f- &cThis is a DEV BUILD, report any unexpected behaviour to the Author!");
        Util.sendConsole(" ");
        Util.sendConsole("&f<> &f<> &f<> &f<>&f<>&f<>&f<>&f<>&f<>&f<>&f<>&f<>&f<>&f<> &f<> &f<> &f<>");
    }

    private static int ver;

    private static void checkServerVersion() {
        ver = Integer.parseInt(Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3].replace("1_", "").substring(1).replaceAll("_R\\d", ""));
    }

    private static int getServerVersion() {
        return ver;
    }

    public static String getMessage(String key) {
        return Util.color(plugin.getConfig().getString("Messages." + key, "Missing Translation for key " + key));
    }

}
