package guerrero61.customtnt.death;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mainutils.config.Config;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.Objects;

public class Sleep implements Listener {

    ArrayList<Player> sleeping = new ArrayList<>();
    ArrayList<Player> globalSleeping = new ArrayList<>();
    private Boolean executed = false;
    private final String world;
    private final Main main;

    public Sleep(Main m) {
        main = m;
        world = Config.getString(Config.Options.MainWorld);
    }

    @EventHandler
    public void playerSleep(final PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        boolean playerIsInMainWorld = player.getWorld().equals(Bukkit.getWorld(world));
        Server server = Bukkit.getServer();
        long time = Objects.requireNonNull(Bukkit.getWorld(world)).getTime();
        if (playerIsInMainWorld) {
            if (time > 13000L && !executed && !Objects.requireNonNull(Bukkit.getWorld(world)).hasStorm()) {
                executed = true;
                Bukkit.getServer().getScheduler().runTaskLater(main, () -> {
                    event.getPlayer().getWorld().setTime(0L);
                    executed = false;
                    String sleepMsg = Config.getString(Config.Options.SleepMsg);
                    Bukkit.broadcastMessage(Formatter.FText(sleepMsg, player));
                    event.setCancelled(true);
                }, 100L);
            } else if ((time < 13000L || executed || Objects.requireNonNull(Bukkit.getWorld(world)).hasStorm())
                    && Config.getBool(Config.Options.SleepExplosive)) {
                event.setCancelled(true);
                player.setStatistic(Statistic.TIME_SINCE_REST, 0);
                Location playerbed = player.getBedSpawnLocation();
                World world = Bukkit.getWorld(Config.getString(Config.Options.MainWorld));
                assert world != null;
                assert playerbed != null;
                world.playEffect(playerbed, Effect.GHAST_SHOOT, 100);
                world.spawnParticle(Particle.EXPLOSION_HUGE, playerbed, 1);
                world.createExplosion(playerbed, 0.0F);
            } else {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBedLeave(PlayerBedLeaveEvent e) {
        Player p = e.getPlayer();
        sleeping.remove(p);
        globalSleeping.remove(p);
    }

    @EventHandler
    public void onLeaveForBed(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        sleeping.remove(p);
        globalSleeping.remove(p);
    }

}
