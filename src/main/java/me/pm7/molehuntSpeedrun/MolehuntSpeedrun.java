package me.pm7.molehuntSpeedrun;

import me.pm7.molehuntSpeedrun.Commands.resetmolehunt;
import me.pm7.molehuntSpeedrun.Commands.startmolehunt;
import me.pm7.molehuntSpeedrun.Listeners.BedBombPrevention;
import me.pm7.molehuntSpeedrun.Listeners.ConnectionListener;
import me.pm7.molehuntSpeedrun.Listeners.DeathListener;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class MolehuntSpeedrun extends JavaPlugin {
    private static MolehuntSpeedrun plugin;

    @Override
    public void onEnable() {
        plugin = this;

        // registry!!
        getCommand("startmolehunt").setExecutor(new startmolehunt());
        getCommand("resetmolehunt").setExecutor(new resetmolehunt());
        getServer().getPluginManager().registerEvents(new BedBombPrevention(), plugin);
        getServer().getPluginManager().registerEvents(new ConnectionListener(), plugin);
        getServer().getPluginManager().registerEvents(new DeathListener(), plugin);

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        trackerManager = new TrackerManager();
    }

    @Override
    public void onDisable() {
        trackerManager.stop();
    }

    private final List<UUID> moles = new ArrayList<>();
    public List<UUID> getMoles() {return moles;}
    public void addMole(UUID uuid) {moles.add(uuid);}

    private World mainWorld;
    public void setMainWorld(World w) {mainWorld = w;}
    public World getMainWorld() {return mainWorld;}

    private TrackerManager trackerManager;
    public TrackerManager getTrackerManager() {return trackerManager;}


    public static MolehuntSpeedrun getPlugin() {return plugin;}
}
