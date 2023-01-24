package mythril.studio;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.world.StructureGrowEvent;

import java.util.List;
import java.util.Random;

import static mythril.studio.Main.boneMealing;
import static mythril.studio.Main.mealingBlocks;
import static mythril.studio.Utils.locations;

public class EventListener implements Listener {



    @EventHandler
    public void onStructureGrowEvent (StructureGrowEvent event) {
        Player player = event.getPlayer();
        Block block = event.getLocation().getBlock();

        if (notInLoc(block)) return;

        Material blockMaterial = block.getType();
        double random = new Random().nextDouble();
        Double chanceToGrow;

        if (player == null) {
            chanceToGrow =  mealingBlocks.get(blockMaterial);
        } else chanceToGrow = boneMealing.get(blockMaterial);

        if (chanceToGrow == null) return;
        if (random <= chanceToGrow) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockGrowEvent (BlockGrowEvent event) {
        Block block = event.getBlock();


        if (notInLoc(block)) return;

        Material blockNewMaterial = event.getNewState().getType();
        double random = new Random().nextDouble();
        Double chanceToGrow =  mealingBlocks.get(blockNewMaterial);


        if (chanceToGrow == null) return;
        if (random <= chanceToGrow) return;

        event.setCancelled(true);
    }



    private boolean notInLoc (Block block) {

        Location blockLoc = block.getLocation();

        String world = blockLoc.getWorld().getName();
        String locX = String.valueOf(block.getX());
        String locY = String.valueOf(block.getY());
        String locZ = String.valueOf(block.getZ());

        String data = world + ":" + locX + ":" + locY + ":" + locZ;

        for (String key : locations.keySet()) {
            List<String> blocks = locations.get(key);
            if (blocks.contains(data)) {
                return false;
            }
        }
        return true;
    }

}
