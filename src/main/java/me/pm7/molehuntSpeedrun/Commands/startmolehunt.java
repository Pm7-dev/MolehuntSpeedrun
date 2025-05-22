package me.pm7.molehuntSpeedrun.Commands;

import me.pm7.molehuntSpeedrun.MolehuntSpeedrun;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class startmolehunt implements CommandExecutor {
    private static final MolehuntSpeedrun plugin = MolehuntSpeedrun.getPlugin();
    private final Random random;

    public startmolehunt() {
        random = new Random();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!sender.isOp() || !(sender instanceof Player p)) {
            sender.sendMessage(ChatColor.RED + "Insufficient permissions. Player must be operator.");
            return true;
        }

        World world = p.getWorld();
        plugin.setMainWorld(world);

        Location borderCenter = new Location(world, 0, 0, 0);

        for(World w : Bukkit.getWorlds()) {
            w.setDifficulty(Difficulty.HARD);
            w.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            w.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);
            w.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            w.setGameRule(GameRule.DO_INSOMNIA, false);
            w.setGameRule(GameRule.KEEP_INVENTORY, true);

            w.getWorldBorder().setCenter(borderCenter);
            w.getWorldBorder().setSize(plugin.getConfig().getDouble("borderSize"));
        }

        for(Player plr : Bukkit.getOnlinePlayers()) {
            plr.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 64));
            plr.setGameMode(GameMode.SURVIVAL);
        }

        Bukkit.broadcastMessage(ChatColor.RED + "Starting Molehunt Speedrun in 15 seconds.");

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            // spread players
            for(Player plr : Bukkit.getOnlinePlayers()) if(plr.getGameMode() != GameMode.SPECTATOR) spread(plr);

            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                for(int i=0; i<plugin.getConfig().getInt("moles"); i++) selectMole();
                for(Player plr : Bukkit.getOnlinePlayers()) plr.sendTitle("§e§lYou are...", "", 10, 70, 20);

                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {

                    // announce the funky
                    for(Player plr : Bukkit.getOnlinePlayers()) {
                        if(plugin.getMoles().contains(plr.getUniqueId())) plr.sendTitle("§c§lThe Mole.", "", 10, 70, 20);
                        else plr.sendTitle("§a§lNOT The Mole.", "", 10, 70, 20);
                    }

                    // Don't count your moles before they hatch
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        StringBuilder moles = new StringBuilder();
                        for(int i=0; i<plugin.getMoles().size(); i++) {
                            Player mole = Bukkit.getPlayer(plugin.getMoles().get(i));
                            if(mole == null) continue;

                            if(i < plugin.getMoles().size() - 1) moles.append(" ").append(mole.getName()).append(",");
                            else moles.append(" and ").append(mole.getName()).append(".");
                        }
                        for(Player plr : Bukkit.getOnlinePlayers()) {
                            if(plugin.getMoles().contains(plr.getUniqueId())) {
                                plr.sendMessage(ChatColor.RED + "Moles:" + moles);
                            }
                        }

                        plugin.getTrackerManager().start(); // Start the tracker (highly important)
                    }, 80L);
                }, 100L);
            }, 240L);
        }, 300L);
        return true;
    }

    private void spread(Player p) {
        if(p == null) { return; }

        World main = plugin.getMainWorld();
        WorldBorder border = main.getWorldBorder();
        Location gameLoc = border.getCenter().clone();
        double spreadDistance = plugin.getConfig().getDouble("spreadDistance");
        double x = (gameLoc.getX() + (Math.random() * (spreadDistance - 4)) - (spreadDistance/2));
        double z = (gameLoc.getZ() + (Math.random() * (spreadDistance - 4)) - (spreadDistance/2));

        Block ground = gameLoc.getWorld().getHighestBlockAt((int) x, (int) z);
        while(!ground.getType().isSolid() && ground.getType() != Material.WATER) {
            x = (gameLoc.getX() + (Math.random() * (spreadDistance - 4)) - (spreadDistance/2));
            z = (gameLoc.getZ() + (Math.random() * (spreadDistance - 4)) - (spreadDistance/2));
            ground = gameLoc.getWorld().getHighestBlockAt((int) x, (int) z);
        }

        Location oldLoc = p.getLocation().clone();

        Location tpLoc = ground.getLocation().add(0.5, 1, 0.5);
        tpLoc.setYaw(((float) Math.random()*360) - 180);
        p.teleport(tpLoc);

        // Sounds and effects
        main.playSound(oldLoc, Sound.ENTITY_BREEZE_LAND, 500, 0.9f);
        main.playSound(tpLoc, Sound.ENTITY_BREEZE_LAND, 500, 0.9f);
        main.spawnParticle(Particle.GUST_EMITTER_SMALL, oldLoc, 1);
        main.spawnParticle(Particle.GUST_EMITTER_SMALL, tpLoc, 1);
    }

    private void selectMole() {
        List<Player> plrs = new ArrayList<>(Bukkit.getOnlinePlayers());
        Player randomPlayer = plrs.get((int) (random.nextDouble() * plrs.size()));
        if(plugin.getMoles().contains(randomPlayer.getUniqueId())) {
            selectMole();
            return;
        }
        plugin.addMole(randomPlayer.getUniqueId());
        randomPlayer.setPlayerListName(ChatColor.RED + randomPlayer.getName());
    }
}
