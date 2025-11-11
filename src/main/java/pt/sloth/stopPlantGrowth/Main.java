package pt.sloth.stopPlantGrowth;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.Objects;

public final class Main extends JavaPlugin {
    public static Plugin plugin;
    @Override
    public void onEnable() {
        plugin = this;
        this.saveDefaultConfig();
        Objects.requireNonNull(this.getCommand("stopplantgrowthreload")).setExecutor(new ReloadCommand());
        getServer().getPluginManager().registerEvents(new EventHandler(), this);
        try {
            DatabaseManager.getInstance();
        } catch (SQLException ignored) {

        }
        try {
            utils.populateMetadata();
        } catch (SQLException e) {
            utils.log("Error populating metadata: " + e.getMessage());
        }
    }

    @Override
    public void onDisable() {
        try {
            DatabaseManager.getInstance().closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
