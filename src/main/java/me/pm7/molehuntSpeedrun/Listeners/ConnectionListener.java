package me.pm7.molehuntSpeedrun.Listeners;

import me.pm7.molehuntSpeedrun.MolehuntSpeedrun;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {
    private static final MolehuntSpeedrun plugin = MolehuntSpeedrun.getPlugin();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if(plugin.getMoles().contains(p.getUniqueId())) {
            p.setPlayerListName(ChatColor.RED + p.getName());
        } else if(p.getGameMode() == GameMode.SPECTATOR) {
            p.setPlayerListName(ChatColor.GRAY + p.getName());
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        plugin.getTrackerManager().removePlayer(e.getPlayer());
    }
}
