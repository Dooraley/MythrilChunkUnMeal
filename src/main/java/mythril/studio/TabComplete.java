package mythril.studio;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static mythril.studio.Utils.locations;

public class TabComplete implements TabCompleter {
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command joinmsg, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("mythril.chunks.*")) {
            return null;
        }
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            list.add("pos1");
            list.add("pos2");
            list.add("remove");
            list.add("<название локации>");
            return list;
        }
        if (args[0].equals("remove")) {
            list.addAll(locations.keySet());
            return list;

        }
        return null;
    }
}
