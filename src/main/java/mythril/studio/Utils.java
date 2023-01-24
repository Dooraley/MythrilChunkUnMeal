package mythril.studio;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import static mythril.studio.Main.plugin;

public class Utils {

    public static Map<String, List<String>> locations = new HashMap<>();

    public static List<String> getBlockLocationsBetween(Location loc1, Location loc2) {
        List<String> locations = new ArrayList<>();

        int x1 = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int y1 = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int z1 = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        int x2 = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int y2 = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int z2 = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                for (int z = z1; z <= z2; z++) {
                    String world = loc1.getWorld().getName();
                    String locX = String.valueOf(x);
                    String locY = String.valueOf(y);
                    String locZ = String.valueOf(z);

                    String data = world + ":" + locX + ":" + locY + ":" + locZ;

                    locations.add(data);
                }
            }
        }
        return locations;
    }

    public static void saveData() {
        try {
            File locationsFile = new File(plugin.getDataFolder(), "locations.dat");
            if (!locationsFile.exists()) {
                boolean result = locationsFile.createNewFile();
                if (!result) {
                    plugin.getLogger().log(Level.SEVERE, "Ошибка создания файла данных locations.dat");
                }
            }
            ObjectOutputStream barrelsListOOS = new ObjectOutputStream(Files.newOutputStream(locationsFile.toPath()));
            barrelsListOOS.writeObject(locations);
            barrelsListOOS.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadData() {
        try {
            File locationsFile = new File(plugin.getDataFolder(), "locations.dat");
            if (locationsFile.exists()) {
                ObjectInputStream barrelsListOIS = new ObjectInputStream(Files.newInputStream(locationsFile.toPath()));
                locations = (Map<String, List<String>>) barrelsListOIS.readObject();
                barrelsListOIS.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
