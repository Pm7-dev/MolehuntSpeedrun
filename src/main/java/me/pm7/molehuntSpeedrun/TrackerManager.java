package me.pm7.molehuntSpeedrun;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.*;

public class TrackerManager {
    private static final MolehuntSpeedrun plugin = MolehuntSpeedrun.getPlugin();

    private static final int TRACKER_FOV = 120;
    private static final int BAR_SIZE = 20;

    private Integer taskID;
    private final Map<UUID, BossBar> trackerBars;



    public TrackerManager() {
        taskID = null;
        trackerBars = new HashMap<>();
    }

    public void start() {
        if(taskID != null) return;
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this::loop, 0L, 1L);
    }

    public void stop() {
        if(taskID == null) return;
        Bukkit.getScheduler().cancelTask(taskID);
        for(Map.Entry<UUID, BossBar> entry : trackerBars.entrySet()) {
            entry.getValue().removeAll();
        }
        trackerBars.clear();
        taskID = null;
    }

    private void loop() {
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(!plugin.getMoles().contains(p.getUniqueId())) continue;

            if(!trackerBars.containsKey(p.getUniqueId())) {
                trackerBars.put(p.getUniqueId(), Bukkit.createBossBar("", BarColor.BLUE, BarStyle.SOLID));
            }

            BossBar bar = trackerBars.get(p.getUniqueId());
            if(!bar.getPlayers().contains(p)) {
                bar.addPlayer(p);
            }

            Location pointLoc = p.getLocation();

            TreeMap<Integer, Boolean> locations = new TreeMap<>();
            for(Player point : Bukkit.getOnlinePlayers()) {
                if(point == p) continue;
                if(point.getGameMode() == GameMode.SPECTATOR) continue;
                if(!point.getWorld().getUID().equals(p.getWorld().getUID())) continue;
                Location pLoc = point.getLocation();

                // Show a red dot if the player is a mole, otherwise show a green dot
                boolean mole = plugin.getMoles().contains(point.getUniqueId());

                double xDist = pLoc.getX() - pointLoc.getX();
                double zDist = pLoc.getZ() - pointLoc.getZ();
                double angle = -Math.toDegrees(Math.atan2(xDist, zDist));
                angle = (angle + 360) % 360;

                double yaw = pointLoc.getYaw();
                yaw = (yaw + 360) % 360;

                float angleDiff = (float) ((angle - yaw + 540) % 360 - 180);

                int location;
                if (angleDiff >= (float) TRACKER_FOV/2) location = BAR_SIZE * 9 + 2;
                else if (angleDiff <= (float) -TRACKER_FOV/2) location = 0;
                else location = Math.round((0.5f + (angleDiff/TRACKER_FOV)) * (BAR_SIZE*9 + 2));

                location = Math.max(location, 3);
                location = Math.min(location, (BAR_SIZE * 9));

                locations.put(location, mole);
            }

            bar.setTitle(generateBar(locations).toLegacyText());
        }
    }

    private BaseComponent generateBar(TreeMap<Integer, Boolean> locations) {
        ComponentBuilder barBuilder = new ComponentBuilder();
        if(plugin.getConfig().getBoolean("irisMode")) {
            barBuilder.color(ChatColor.of(new Color(255, 255, 255))); // will look slightly worse
        } else {
            barBuilder.color(ChatColor.of(new Color(78, 92, 36)));
        }

        // Generate the bar
        for(int i=0; i<BAR_SIZE; i++) {
            if(i==0) barBuilder.append("\uE000");
            else if (i==BAR_SIZE-1) barBuilder.append("\uE002");
            else barBuilder.append("\uE001");

            barBuilder.append("\uF801"); // Cancel out the space after the character

            if(i==BAR_SIZE/2) { // add two extra pixels in the middle
                barBuilder.append("\uE00A");
                barBuilder.append("\uF801");
            }
        }

        // Move back to the startmolehunt of the bar
        barBuilder.append("\uF801".repeat((BAR_SIZE * 9) + 2));

        int position = 0;
        for(Map.Entry<Integer, Boolean> point : locations.entrySet()) {
            while(position != point.getKey()) {
                position++;
                barBuilder.append("\uE004\uF801");
            }
            if(position == 3 || position == 4) insertMarker(barBuilder, point.getValue(), "left");
            else if(position == (BAR_SIZE * 9) || position == (BAR_SIZE * 9) - 1) insertMarker(barBuilder, point.getValue(), "right");
            else insertMarker(barBuilder, point.getValue(), "normal");
        }

        for(; position<BAR_SIZE*9 + 2; position++) {
            barBuilder.append("\uE004\uF801");
        }

        return barBuilder.build();
    }

    private void insertMarker(ComponentBuilder input, boolean mole, String style) {
        input.append("\uF801\uF801\uF801\uF801\uF801");
        if(mole) {
            if(style.equals("left")) {
                input.append("\uE006");
            } else if(style.equals("right")) {
                input.append("\uE007");
            } else {
                input.append("\uE005");
            }
        }
        else {
            if(style.equals("left")) {
                input.append("\uE008");
            } else if(style.equals("right")) {
                input.append("\uE009");
            } else {
                input.append("\uE003");
            }
        }
        input.append("\uF801\uF801\uF801\uF801\uF801");
    }

    public void removePlayer(Player p) {
        if(trackerBars.containsKey(p.getUniqueId())) {
            BossBar bar = trackerBars.get(p.getUniqueId());
            bar.removeAll();
            trackerBars.remove(p.getUniqueId());
        }
    }
}
