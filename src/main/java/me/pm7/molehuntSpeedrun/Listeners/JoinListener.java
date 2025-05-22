package me.pm7.molehuntSpeedrun.Listeners;

import me.pm7.molehuntSpeedrun.MolehuntSpeedrun;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class JoinListener implements Listener {
    MolehuntSpeedrun plugin;

    private static Scoreboard board;
    private static Team hiddenName;

    public JoinListener(MolehuntSpeedrun plugin) {
        this.plugin = plugin;
        if(board == null) { setupScoreboard(); }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.getPlayer().setScoreboard(board);
        if(hiddenName == null) {
            hiddenName = board.registerNewTeam("HideNameTag");
            hiddenName.setAllowFriendlyFire(true);
            hiddenName.setCanSeeFriendlyInvisibles(false);
            hiddenName.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        }
        hiddenName.addEntry(e.getPlayer().getName());

        if(plugin.getMoles().contains(e.getPlayer().getUniqueId())) {
            e.getPlayer().setPlayerListName(ChatColor.RED + e.getPlayer().getName());
        }

        e.getPlayer().setInvulnerable(false);
    }

    void setupScoreboard() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        board = manager.getNewScoreboard();

        hiddenName = board.registerNewTeam("HideNameTag");
        hiddenName.setAllowFriendlyFire(true);
        hiddenName.setCanSeeFriendlyInvisibles(false);
        hiddenName.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
    }
}
