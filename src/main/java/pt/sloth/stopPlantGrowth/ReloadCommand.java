package pt.sloth.stopPlantGrowth;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("spg.reload")){
            Main.plugin.reloadConfig();
            utils.log("Configuration reloaded by " + sender.getName());
            utils.sendMessageToPlayer((org.bukkit.entity.Player) sender, "Configuration reloaded.");
        }
        return true;
    }
}
