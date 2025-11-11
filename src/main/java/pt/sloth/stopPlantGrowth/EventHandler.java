package pt.sloth.stopPlantGrowth;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;

import static pt.sloth.stopPlantGrowth.utils.isPlant;

public class EventHandler implements Listener {

    @org.bukkit.event.EventHandler(priority = EventPriority.LOW)
    public void onPlantGrow(org.bukkit.event.block.BlockGrowEvent event) {
        if (event.getBlock().hasMetadata("stopGrowth") || event.getNewState().hasMetadata("stopGrowth")) {
            event.setCancelled(true);
        }

    }
    @org.bukkit.event.EventHandler(priority = EventPriority.LOW)
    public void onPlantSpread(org.bukkit.event.block.BlockSpreadEvent event) {
        if (event.getBlock().hasMetadata("stopGrowth") || event.getSource().hasMetadata("stopGrowth")) {
            event.setCancelled(true);
        }
    }
    /**
     * I was going to do it this way so it doesn't try to handle broken plants
     * However I thought that doing it at server boot is better.
     * **/
//    @org.bukkit.event.EventHandler(priority = EventPriority.LOW)
//    public void onBlockBreak(org.bukkit.event.block.BlockBreakEvent event) {
//        if (event.getBlock().hasMetadata("stopGrowth")) {
//            event.getBlock().removeMetadata("stopGrowth", Main.plugin);
//            try {
//                DatabaseManager.getInstance().removeBlock(event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ(), event.getBlock().getWorld().getName());
//            } catch (Exception e) {
//                utils.log("Error removing block from database: " + e.getMessage());
//            }
//        }
//    }
    @org.bukkit.event.EventHandler(priority = EventPriority.LOW)
    public void onPlayerInteract(org.bukkit.event.player.PlayerInteractEvent event) {
        if (event.getAction() != org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK || event.getHand() != EquipmentSlot.HAND) return;
        if (event.getClickedBlock() != null && utils.isTool(event.getPlayer().getInventory().getItemInMainHand().getType())) {
            if (isPlant(event.getClickedBlock().getType())){
                if (event.getClickedBlock().hasMetadata("stopGrowth")) {
                    event.getClickedBlock().removeMetadata("stopGrowth", Main.plugin);
                    try {
                        DatabaseManager.getInstance().removeBlock(event.getClickedBlock().getX(), event.getClickedBlock().getY(), event.getClickedBlock().getZ(), event.getClickedBlock().getWorld().getName());
                    } catch (Exception e) {
                        utils.log("Error removing block from database: " + e.getMessage());
                    }
                    utils.sendPreventedMessage(event.getPlayer(), false);
                } else {
                    event.getClickedBlock().setMetadata("stopGrowth", new org.bukkit.metadata.FixedMetadataValue(Main.plugin, true));
                    try {
                        DatabaseManager.getInstance().addBlock(event.getClickedBlock().getX(), event.getClickedBlock().getY(), event.getClickedBlock().getZ(), event.getClickedBlock().getWorld().getName());
                    } catch (Exception e) {
                        utils.log("Error adding block to database: " + e.getMessage());
                    }
                    utils.sendPreventedMessage(event.getPlayer(), true);
                }

            }
        }
        //utils.sendMessageToPlayer(event.getPlayer(), event.getClickedBlock().hasMetadata("stopGrowth")+ "");
    }
}
