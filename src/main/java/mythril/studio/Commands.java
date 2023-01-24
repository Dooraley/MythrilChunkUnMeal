package mythril.studio;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Pattern;

import static mythril.studio.Main.plugin;
import static mythril.studio.Utils.getBlockLocationsBetween;
import static mythril.studio.Utils.locations;

public class Commands implements CommandExecutor {

    private static final Map < Player, Location > pos1 = new HashMap < > ();
    private static final Map < Player, Location > pos2 = new HashMap < > ();

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            plugin.getLogger().log(Level.SEVERE, "Использование данной команды возможно только в игре!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("mythril.chunks.*")) {
            player.sendMessage("§4Недостаточно прав!");
        }

        if (args.length == 0) {
            player.sendMessage("§cИспользование: §6/mgc <pos1/pos2>§c, §6/mgc <название>§c, §6/mgc remove <название>§c.");
            return true;
        }

        switch (args[0]) {
            case "pos1":
                handlePos1Command(player);
                break;
            case "pos2":
                handlePos2Command(player);
                break;
            case "remove":
                handleRemoveCommand(player, args);
                break;
            default:
                handleCreateCommand(player, args);
                break;
        }
        return true;
    }

    private void handlePos1Command(Player player) {
        Block block = player.getLocation().getBlock();
        int x = block.getX();
        int y = block.getY() - 1;
        int z = block.getZ();
        Location blockLoc = new Location(player.getWorld(), x, y, z);

        pos1.put(player, blockLoc);
        player.sendMessage("§2Первая координата успешно задана!");
    }

    private void handlePos2Command(Player player) {
        Block block = player.getLocation().getBlock();
        int x = block.getX();
        int y = block.getY() - 1;
        int z = block.getZ();
        Location blockLoc = new Location(player.getWorld(), x, y, z);

        pos2.put(player, blockLoc);
        player.sendMessage("§2Вторая координата успешно задана!");
    }

    private void handleRemoveCommand(Player player, String[] args) {
        if (args.length != 2) {
            player.sendMessage("§cИспользование: §6/mgc remove <название>§c.");
            return;
        }
        if (!locations.containsKey(args[1])) {
            if (!locations.isEmpty()) {
                StringBuilder keys = new StringBuilder();
                for (String key: locations.keySet()) {
                    keys.append(key).append(", ");
                }
                keys.delete(keys.length() - 2, keys.length());
                player.sendMessage("§cЛокации с данным названием не найдено!\n§6Доступные названия: §2" + keys +"§6.");
            } else {
                player.sendMessage("§cДоступных для удаления локаций не найдено!");
            }
        } else {
            locations.remove(args[1]);
            player.sendMessage("§2Локация успешно удалена!");
        }
    }

    private void handleCreateCommand(Player player, String[] args) {
        if (!pos1.containsKey(player) || !pos2.containsKey(player)) {
            player.sendMessage("§cСначала задайте обе координаты с помощью команд §6/mgc pos1 §cи §6/mgc pos2§c.");
            return;
        }

        String locName = args[0];
        if (locations.containsKey(locName)) {
            player.sendMessage("§cЛокация с таким названием уже существует!");
            return;
        }

        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_]{3,16}$");
        if (!pattern.matcher(locName).matches()) {
            player.sendMessage("§cНазвание локации содержит недопустимые символы!");
            return;
        }

        Location loc1 = pos1.get(player);
        Location loc2 = pos2.get(player);
        pos1.remove(player);
        pos2.remove(player);
        locations.put(locName, getBlockLocationsBetween(loc1, loc2));

        player.sendMessage("§2Локация успешно создана!");
    }
}