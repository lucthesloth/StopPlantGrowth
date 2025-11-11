package pt.sloth.stopPlantGrowth;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.Material;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class utils {
    static MiniMessage miniMessage = MiniMessage.miniMessage();
    public static void log(String message) {
        Main.plugin.getComponentLogger().info(miniMessage.deserialize("[<gold>StopPlantGrowth<gold/>]<yellow> " + message + "</yellow>"));
    }
    public static void sendMessageToPlayer(org.bukkit.entity.Player player, String message) {
        player.sendMessage(miniMessage.deserialize("[<gold>StopPlantGrowth</gold>] <dark_aqua>" + message + "</dark_aqua>"));
    }
    public static void sendPreventedMessage(org.bukkit.entity.Player player, boolean a) {
        sendMessageToPlayer(player, String.format(Main.plugin.getConfig().getString("prevention-message","This plant's growth has been <aqua>%s</aqua>"), a ? "Prevented" : "Allowed"));
    }
    public static boolean isPlant(Material material) {
        if (material == Material.AIR) return false;
        return Main.plugin.getConfig().getStringList("prevented-blocks").contains(material.name());
    }

    public static boolean isTool(Material material) {
        if (material == Material.AIR) return false;
        return Main.plugin.getConfig().getStringList("preventing-items").contains(material.name());
    }

    public static void populateMetadata() throws SQLException {
        AtomicInteger notPlants = new AtomicInteger(0);
        List< Location > blockData = DatabaseManager.getInstance().loadBlockData();
        blockData.forEach(location -> {
            if (utils.isPlant(location.getBlock().getType())){
                location.getBlock().setMetadata("stopGrowth", new org.bukkit.metadata.FixedMetadataValue(Main.plugin, true));
            } else {
                notPlants.getAndIncrement();
                try {
                    DatabaseManager.getInstance().removeBlock(location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getWorld().getName());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        });
        utils.log("Loaded " + blockData.size() + " blocks from database. Removed " + notPlants + " non-plant blocks.");
    }
}
