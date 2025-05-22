package me.pm7.molehuntSpeedrun.Listeners;

import me.pm7.molehuntSpeedrun.MolehuntSpeedrun;
import org.bukkit.*;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

public class DeathListener implements Listener {
    private static final MolehuntSpeedrun plugin = MolehuntSpeedrun.getPlugin();

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if(!(e.getEntity()instanceof Player p)) {
            if(e.getEntity() instanceof EnderDragon enderDragon) {
                if(enderDragon.getHealth() - e.getFinalDamage() > 0.0) return;

                // Dragon has been killed, check for survivor win?
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    Bukkit.broadcastMessage(ChatColor.GREEN + "Survivors Win!");
                    for(Player plr : Bukkit.getOnlinePlayers()) { plr.playSound(plr, Sound.BLOCK_NOTE_BLOCK_HAT, 500, 0.5f); }
                }, 100L);
            }
            return;
        }

        // Player must die from this attack to continue
        if(p.getHealth() - e.getFinalDamage() > 0.0) return;
        e.setCancelled(true);

        // sound.
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_DEATH, 1, 1);

        p.setGameMode(GameMode.SPECTATOR);
        p.sendTitle("§c§lYou died!", "", 10, 70, 20);
        p.setPlayerListName(ChatColor.GRAY + p.getName());

        // Gather the contents of the player's inventory and drop them
        Inventory inv = p.getInventory();
        Location loc = p.getLocation();
        World world = p.getWorld();
        double power = 0.2D;
        for(ItemStack item : inv.getContents()) {
            if(item != null && item.getItemMeta() != null) {
                double xVel = -power + (Math.random() * (power*2));
                double zVel = -power + (Math.random() * (power*2));
                Entity dropped = world.dropItem(loc, item);
                dropped.setVelocity(new Vector(xVel, 0.3, zVel));
            }
        }
        inv.clear();

        for(PotionEffect pe : p.getActivePotionEffects()) { p.removePotionEffect(pe.getType()); }

        plugin.getMoles().remove(p.getUniqueId());

        for(Player plr : Bukkit.getOnlinePlayers()) {
            if(plugin.getMoles().contains(plr.getUniqueId())) {
                plr.sendMessage(ChatColor.RED + "[Mole Chat]: " + ChatColor.RESET + p.getName() + " has died!");
            }
        }

        // Check for player & mole win??
        boolean moleWin = true;
        boolean playerWin = true;
        for(Player plr : Bukkit.getOnlinePlayers()) {
            if(plugin.getMoles().contains(plr.getUniqueId())) playerWin = false;
            else if(plr.getGameMode() != GameMode.SPECTATOR) moleWin = false;
        }

        if(moleWin) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                Bukkit.broadcastMessage(ChatColor.RED + "Moles Win!");
                for(Player plr : Bukkit.getOnlinePlayers()) { plr.playSound(plr, Sound.BLOCK_NOTE_BLOCK_HAT, 500, 0.5f); }
            }, 100L);
        } else if (playerWin) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                Bukkit.broadcastMessage(ChatColor.GREEN + "Survivors Win!");
                for(Player plr : Bukkit.getOnlinePlayers()) { plr.playSound(plr, Sound.BLOCK_NOTE_BLOCK_HAT, 500, 0.5f); }
            }, 100L);
        }
    }
}
