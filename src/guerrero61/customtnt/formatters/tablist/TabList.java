package guerrero61.customtnt.formatters.tablist;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.mainutils.Formatter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TabList implements Listener {

    private final Main main;

    public TabList(Main main) {
        this.main = main;
    }

    public void reloadTab() {
        Scoreboard scoreboard = main.getServer().getScoreboardManager().getMainScoreboard();

        for (Player player : main.getServer().getOnlinePlayers()) {
            if(!Formatter.FText("%vault_prefix%", true, player).equals("%vault_prefix%") &&
               !Formatter.FText("%vault_suffix%", true, player).equals("%vault_suffix%")) {
                Team team = scoreboard.getTeam(Formatter.FText(player.getName(), true, player));
                if(team == null) {
                    team = scoreboard.registerNewTeam(Formatter.FText(player.getName(), true, player));
                }
                boolean playerIsInTeam = false;
                for (String playerN : team.getEntries()) {
                    if(playerN.equals(player.getName())) {
                        playerIsInTeam = true;
                    }
                }
                if(!playerIsInTeam) team.addEntry(player.getName());
                team.setPrefix(Formatter.FText("%vault_prefix%", true, player));
                team.setSuffix(Formatter.FText("%vault_suffix%", true, player));
            }
        }
    }

    public void reloadTab(Player player) {
        Scoreboard scoreboard = main.getServer().getScoreboardManager().getMainScoreboard();

        if(!Formatter.FText("%vault_prefix%", true, player).equals("%vault_prefix%") &&
           !Formatter.FText("%vault_suffix%", true, player).equals("%vault_suffix%")) {
            Team team = scoreboard.getTeam(Formatter.FText(player.getName(), true, player));
            if(team == null) {
                team = scoreboard.registerNewTeam(Formatter.FText(player.getName(), true, player));
            }
            boolean playerIsInTeam = false;
            for (String playerN : team.getEntries()) {
                if(playerN.equals(player.getName())) {
                    playerIsInTeam = true;
                }
            }
            if(!playerIsInTeam) team.addEntry(player.getName());
            team.setPrefix(Formatter.FText("%vault_prefix%", true, player));
            team.setSuffix(Formatter.FText("%vault_suffix%", true, player));
        }
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        reloadTab(event.getPlayer());
    }

}
