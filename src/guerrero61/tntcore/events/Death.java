package guerrero61.tntcore.events;

import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import guerrero61.tntcore.Main;

public class Death implements Listener {

	private Main main;
	private Integer addStormSeconds;
	private Float stormHours;
	private String world;

	public Death(Main m) {
		main = m;
		addStormSeconds = Main.getInt("Storm.add-seconds");
		stormHours = addStormSeconds / 60f / 60f;
		world = Main.getString("MainWorld");
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		// Codigo modificado de PermadeathCore por vo1d & SebazCRC
		Boolean weather = Bukkit.getWorld(Objects.<String>requireNonNull(world)).hasStorm();
		String victim = e.getEntity().getPlayer().getName();
		Player killer = e.getEntity().getKiller();
		String DeathChatMessage = Main.getString("Messages.Death.msg");
		Bukkit.broadcastMessage(
				Main.FText(DeathChatMessage.replace("%player%", victim)));
		if (Main.getBool("Messages.Death.coords-enable")) {
			String CoordsMessage = Main.getString("Messages.Death.coords-msg");
			String Dx = Integer.toString(e.getEntity().getPlayer().getLocation().getBlockX());
			String Dy = Integer.toString(e.getEntity().getPlayer().getLocation().getBlockY());
			String Dz = Integer.toString(e.getEntity().getPlayer().getLocation().getBlockZ());
			Bukkit.broadcastMessage(Main.FText(CoordsMessage.replace("%x%", Dx).replace("%y%", Dy).replace("%z%", Dz)));
		}
		Integer stormDuration = Bukkit.getWorld(Objects.<String>requireNonNull(world)).getWeatherDuration();
		Integer stormTicksToSeconds = stormDuration / 20;
		Integer stormIncrement = Math.round(stormTicksToSeconds + this.addStormSeconds);
		Integer intsTicks = Math.round(addStormSeconds);
		Integer inc = Math.round(stormIncrement);
		if (weather) {
			Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(), "minecraft:weather thunder");
			Bukkit.getWorld(Objects.<String>requireNonNull(world)).setWeatherDuration(inc * 20);
		} else {
			Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(), "minecraft:weather thunder");
			Bukkit.getWorld(Objects.<String>requireNonNull(world)).setWeatherDuration(intsTicks * 20);
		}
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleSyncDelayedTask((Plugin) main, new Runnable() {
			public void run() {
				String DeathTrainMessage = Main.getString("Messages.Death.train");
				Bukkit.broadcastMessage(
						Main.FText(DeathTrainMessage.replace("%time%", Float.toString(stormHours))));
				for (Player player : Bukkit.getOnlinePlayers())
					player.playSound(player.getLocation(), Sound.ENTITY_SKELETON_HORSE_DEATH, 10.0F, 1.0F);
			}
		}, 75L);
		for (Player player : Bukkit.getOnlinePlayers()) {
			String ServerMessageTitle = Main.getString("Messages.Death.title");
			String ServerMessageSubtitle = Main.getString("Messages.Death.subtitle");
			player.sendTitle(Main.FTextNPrefix(ServerMessageTitle),
					Main.FTextNPrefix(ServerMessageSubtitle.replace("%player%", victim)), 10, 70, 20);
			player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_DEATH, 10.0F, -5.0F);
		}
	}
}
