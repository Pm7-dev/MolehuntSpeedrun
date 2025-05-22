package me.pm7.molehuntSpeedrun.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
import java.util.List;

public class BedBombPrevention implements Listener {

    private static final List<Material> beds = Arrays.asList(
            Material.WHITE_BED,
            Material.ORANGE_BED,
            Material.MAGENTA_BED,
            Material.LIGHT_BLUE_BED,
            Material.YELLOW_BED,
            Material.LIME_BED,
            Material.PINK_BED,
            Material.GRAY_BED,
            Material.LIGHT_GRAY_BED,
            Material.CYAN_BED,
            Material.PURPLE_BED,
            Material.BLUE_BED,
            Material.BROWN_BED,
            Material.GREEN_BED,
            Material.RED_BED,
            Material.BLACK_BED
    );

    @EventHandler
    public void onPlayerSleep(PlayerInteractEvent e) {
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block b = e.getClickedBlock();
        if(b == null) return;

        if(beds.contains(b.getType())) {
            World.Environment env = b.getWorld().getEnvironment();
            if(env != World.Environment.NORMAL) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.RED + "No bed bombing >:3");
            }
        }
    }
}
