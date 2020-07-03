package guerrero61.tntcore.events;

import java.util.ArrayList;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import guerrero61.tntcore.Main;

public class Sleep implements Listener {

	private Main main;
	private FileConfiguration config;
	private String prefix;
	ArrayList<Player> sleeping = new ArrayList<>();
	ArrayList<Player> globalSleeping = new ArrayList<>();
	private boolean executed = false;
	private String world;

	public Sleep(Main m) {
		main = m;
		config = main.getConfig();
		prefix = config.getString("Prefix");
		world = config.getString("MainWorld");
	}

	@EventHandler
	public void playerSleep(final PlayerBedEnterEvent event) {
		Player player = event.getPlayer();
		Server server = Bukkit.getServer();
		long time = Bukkit.getWorld(Objects.<String>requireNonNull(world)).getTime();
		if (time > 13000L && !executed && !Bukkit.getWorld(Objects.<String>requireNonNull(world)).hasStorm()) {
			executed = true;
			Bukkit.getServer().getScheduler().runTaskLater((Plugin) main, new Runnable() {
				public void run() {
					event.getPlayer().getWorld().setTime(0L);
					executed = false;
					String sleepMsg = config.getString("Sleep.msg");
					Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
							prefix + sleepMsg.replace("%player%", player.getName())));
					event.setCancelled(true);
				}
			}, 100L);
		} else if ((time < 13000L || executed || Bukkit.getWorld(Objects.<String>requireNonNull(world)).hasStorm()) && config.getBoolean("Sleep.explosive")){
			player.setStatistic(Statistic.TIME_SINCE_REST, 0);
			Location playerbed = player.getBedSpawnLocation();
			World world = Bukkit.getWorld(config.getString("MainWorld"));
			world.playEffect(playerbed, Effect.GHAST_SHOOT, 100);
			world.spawnParticle(Particle.EXPLOSION_HUGE, playerbed, 1);
			world.createExplosion(playerbed, 0.0F);
			event.setCancelled(true);
		} else {
			event.setCancelled(true);
		}
	
	}

	@EventHandler
	public void onBedLeave(PlayerBedLeaveEvent e) {
		Player p = e.getPlayer();
		if (sleeping.contains(p))
			sleeping.remove(p);
		if (globalSleeping.contains(p))
			globalSleeping.remove(p);
	}

	@EventHandler
	public void onLeaveForBed(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (sleeping.contains(p))
			sleeping.remove(p);
		if (globalSleeping.contains(p))
			globalSleeping.remove(p);
	}

}
