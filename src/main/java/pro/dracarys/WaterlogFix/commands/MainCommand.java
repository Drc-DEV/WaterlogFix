package pro.dracarys.WaterlogFix.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;
import pro.dracarys.WaterlogFix.WaterlogFix;
import pro.dracarys.WaterlogFix.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class MainCommand implements TabExecutor {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        List<String> lista = new ArrayList<>();
        if (args.length == 0) {
            lista.add("wlogfix");
            lista.add("waterlogfix");
        }
        if (args.length == 1) {
            List<String> tempList = new ArrayList<>();
            tempList.add("help");
            if (sender.hasPermission("waterlogfix.reload")) {
                tempList.add("reload");
            }
            lista.addAll(StringUtil.copyPartialMatches(args[0], tempList, new ArrayList<>()));
        }
        return lista;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        // |====================================================»
        // |> Help Command - Anybody by default
        // |====================================================»
        if (args.length == 0 || (args[0].equalsIgnoreCase("help"))) {
            sender.sendMessage(Util.color("&f&lWaterlogFix &7by Valentina_pro &2v%version%").replace("%version%", WaterlogFix.getInstance().getDescription().getVersion()));
            sender.sendMessage(Util.color(" &e/wlogfix &6reload &7» &fReload Config"));
            sender.sendMessage(Util.color(" &6Enabled Worlds: &7" + String.join("&f,&7", WaterlogFix.enabledWorlds)));
            return true;
        }
        // |====================================================»
        // |> Reload Command - Only OP and Console by default
        // |====================================================»
        if (args[0].equalsIgnoreCase(("reload"))) {
            if (!sender.hasPermission("waterlogfix.reload")) {
                sender.sendMessage(Util.color("&cYou must have the &fwaterlogfix.reload &cpermission!"));
                return true;
            }
            WaterlogFix.getInstance().reloadConfig();
            WaterlogFix.enabledWorlds = WaterlogFix.getInstance().getConfig().getStringList("Settings.enabled-worlds");
            sender.sendMessage(Util.color("&aConfig Reloaded."));
            return true;
        }
        sender.sendMessage(Util.color("&cCould not recognize that sub-command! &eUse &f/wlogfix help &efor a list of available commands."));
        return true;
    }

}
