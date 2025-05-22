package me.pm7.molehuntSpeedrun.Commands;

import me.pm7.molehuntSpeedrun.MolehuntSpeedrun;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class resetmolehunt implements CommandExecutor {
    private static final MolehuntSpeedrun plugin = MolehuntSpeedrun.getPlugin();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        // Permission check
        if(!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "Insufficient permissions. Player must be operator.");
            return true;
        }

        // Reset name color
        for(Player p : Bukkit.getOnlinePlayers()) {
            p.setPlayerListName(p.getName());
        }

        // Reset mole list
        plugin.getMoles().clear();

        // stop the tracker
        plugin.getTrackerManager().stop();

        sender.sendMessage(ChatColor.GREEN + "Successfully reset Molehunt Speedrun!");
        return true;
    }
}
