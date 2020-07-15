package guerrero61.tntcore.events;

import java.util.ArrayList;
import java.util.Objects;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import guerrero61.tntcore.Main;

public class Sleep implements Listener {

	ArrayList<Player> sleeping = new ArrayList<>();
	ArrayList<Player> globalSleeping = new ArrayList<>();
	private Boolean executed = false;
	private final String world;
	private final Main main;

	public Sleep(Main m) {
		main = m;
		world = Main.getString("MainWorld");
	}

	@EventHandler
	public void playerSleep(final PlayerBedEnterEvent event) {
		Player player = event.getPlayer();
		Server server = Bukkit.getServer();
		long time = Objects.requireNonNull(Bukkit.getWorld(world)).getTime();
		if (time > 13000L && !executed && !Objects.requireNonNull(Bukkit.getWorld(world)).hasStorm()) {
			executed = true;
			Bukkit.getServer().getScheduler().runTaskLater(main, () -> {
				event.getPlayer().getWorld().setTime(0L);
				executed = false;
				String sleepMsg = Main.getString("Sleep.msg");
				Bukkit.broadcastMessage(Main.FText(sleepMsg.replace("%player%", player.getName())));
				event.setCancelled(true);
			}, 100L);
		} else if ((time < 13000L || executed || Objects.requireNonNull(Bukkit.getWorld(world)).hasStorm())
				&& Main.getBool("Sleep.explosive")) {
			event.setCancelled(true);
			player.setStatistic(Statistic.TIME_SINCE_REST, 0);
			Location playerbed = player.getBedSpawnLocation();
			World world = Bukkit.getWorld(Main.getString("MainWorld"));
			assert world != null;
			assert playerbed != null;
			world.playEffect(playerbed, Effect.GHAST_SHOOT, 100);
			world.spawnParticle(Particle.EXPLOSION_HUGE, playerbed, 1);
			world.createExplosion(playerbed, 0.0F);
		} else {
			event.setCancelled(true);
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
