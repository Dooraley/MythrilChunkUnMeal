package mythril.studio;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Level;

import static mythril.studio.Utils.loadData;
import static mythril.studio.Utils.saveData;

public final class Main extends JavaPlugin implements Listener {
    public static Main plugin;
    public static FileConfiguration config;
    public static Map<Material, Double> mealingBlocks;
    public static Map<Material, Double> boneMealing;
    @Override
    public void onEnable() {

        plugin = this;
        this.saveDefaultConfig();
        config = this.getConfig();

        getMealingBlocks();
        loadData();
        PluginCommand mgc = this.getCommand("mgc");

        if (mgc != null) {
            mgc.setExecutor(new Commands());
            mgc.setTabCompleter(new TabComplete());
        }

        Bukkit.getServer().getPluginManager().registerEvents(new EventListener(), this);
        getLogger().info("\n================================================\n             MythrilChunkUnMeal включён!\n  Авторы плагина: https://vk.com/mythrilstudio\n================================================");
    }

    @Override
    public void onDisable() {
        saveData();
        getLogger().info("\n================================================\n             MythrilChunkUnMeal выключен!\n  Авторы плагина: https://vk.com/mythrilstudio\n================================================");

    }

    private void getMealingBlocks() {
        mealingBlocks = new HashMap<>();
        boneMealing = new HashMap<>();    ConfigurationSection mealingBlocksConfig = config.getConfigurationSection("mealing_blocks");
        ConfigurationSection boneMealingConfig = config.getConfigurationSection("bone_mealing");

        if (mealingBlocksConfig == null || boneMealingConfig == null) {
            plugin.getLogger().log(Level.SEVERE, "Конфиг-файл не содержит нужный блок!");
            return;
        }

        Set<String> mealingBlocksKeys = mealingBlocksConfig.getKeys(false);
        Set<String> boneMealingKeys = boneMealingConfig.getKeys(false);

        addBlocksToMap(mealingBlocks, mealingBlocksKeys, mealingBlocksConfig, "mealing_blocks");
        addBlocksToMap(boneMealing, boneMealingKeys, boneMealingConfig, "bone_mealing");
    }

    private void addBlocksToMap(Map<Material, Double> map, Set<String> keys, ConfigurationSection config, String configName) {
        for (String block : keys) {
            Material blockMaterial;
            try {
                blockMaterial = Material.valueOf(block);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().log(Level.SEVERE, "Неверно указан Material блока в разделе " + configName + ": " + block);
                continue;
            }
            double mealingChance = config.getDouble(block) / 100;
            map.put(blockMaterial, mealingChance);
        }
    }


}
