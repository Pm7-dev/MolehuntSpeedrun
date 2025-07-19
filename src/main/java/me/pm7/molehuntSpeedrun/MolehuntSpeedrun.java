package me.pm7.molehuntSpeedrun;

import me.pm7.molehuntSpeedrun.Commands.resetmolehunt;
import me.pm7.molehuntSpeedrun.Commands.startmolehunt;
import me.pm7.molehuntSpeedrun.Commands.starttimer;
import me.pm7.molehuntSpeedrun.Listeners.BedBombPrevention;
import me.pm7.molehuntSpeedrun.Listeners.ConnectionListener;
import me.pm7.molehuntSpeedrun.Listeners.DeathListener;
import me.pm7.molehuntSpeedrun.Listeners.JoinListener;
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
        getCommand("starttimer").setExecutor(new starttimer());
        getServer().getPluginManager().registerEvents(new BedBombPrevention(), plugin);
        getServer().getPluginManager().registerEvents(new ConnectionListener(), plugin);
        getServer().getPluginManager().registerEvents(new DeathListener(), plugin);
        getServer().getPluginManager().registerEvents(new JoinListener(plugin), plugin);

        getConfig().options().copyDefaults();
        saveDefaultConfig();
        saveConfig();

        /*
        minMoles: 2
        maxMoles: 3
        preTime: 90
        borderSize: 750
        spreadDistance: 500
        irisMode: false
         */

        if(!getConfig().contains("minMoles")) {
            getConfig().set("minMoles", 2);
        }
        if(!getConfig().contains("maxMoles")) {
            getConfig().set("maxMoles", 3);
        }
        if(!getConfig().contains("preTime")) {
            getConfig().set("preTime", 90);
        }
        if(!getConfig().contains("borderSize")) {
            getConfig().set("borderSize", 750);
        }
        if(!getConfig().contains("spreadDistance")) {
            getConfig().set("spreadDistance", 500);
        }
        if(!getConfig().contains("irisMode")) {
            getConfig().set("irisMode", false);
        }

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
