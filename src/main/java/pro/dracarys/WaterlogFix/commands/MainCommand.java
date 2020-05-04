package pro.dracarys.WaterlogFix.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
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
            String perm = WaterlogFix.getInstance().getConfig().getString("Permissions.reload-command");
            if (perm == null) perm = "waterlogfix.reload";
            if (sender.hasPermission(perm)) {
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
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(Util.color("&8=====[ &fWaterlog&4&lFIX&8 ]=====[ v.&7%version%&8 ]=====")
                    .replace("%version%", WaterlogFix.getInstance().getDescription().getVersion()));
            sender.sendMessage(Util.color(" &e/wlogfix &6reload &7» &f" + WaterlogFix.getMessage("cmd-reload-desc")));
            if (sender instanceof Player) {
                Player p = (Player) sender;
                sender.sendMessage(Util.color(" &6Enabled in this World: &7" + Util.isEnabledWorld(p.getWorld().getName())));
            }
            return true;
        }
        // |====================================================»
        // |> Reload Command - Only OP and Console by default
        // |====================================================»
        if (args[0].equalsIgnoreCase(("reload"))) {
            String perm = WaterlogFix.getInstance().getConfig().getString("Permissions.reload-command");
            if (perm == null) perm = "waterlogfix.reload";
            if (!sender.hasPermission(perm)) {
                sender.sendMessage(WaterlogFix.getMessage("no-permissions").replace("%perm%", perm));
                return true;
            }
            WaterlogFix.getInstance().reloadConfig();
            sender.sendMessage(WaterlogFix.getMessage("config-reloaded"));
            return true;
        }
        sender.sendMessage(WaterlogFix.getMessage("cmd-wrong-args").replace("%cmd%", "/wlogfix help"));
        return true;
    }

}
