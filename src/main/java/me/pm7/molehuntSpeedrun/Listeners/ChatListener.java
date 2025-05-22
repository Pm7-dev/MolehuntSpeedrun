package me.pm7.molehuntSpeedrun.Listeners;

import me.pm7.molehuntSpeedrun.MolehuntSpeedrun;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    private static final MolehuntSpeedrun plugin = MolehuntSpeedrun.getPlugin();
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        Player sender = e.getPlayer();

        if(plugin.getMoles().contains(sender.getUniqueId())) {
            return;
        }

        for(Player p : Bukkit.getOnlinePlayers()) {
            if(plugin.getMoles().contains(p.getUniqueId())) continue;
            p.sendMessage(ChatColor.GREEN + "[Survivor Chat]: " + ChatColor.RESET + "<" + sender.getDisplayName() + "> " + e.getMessage());
        }
    }
}
