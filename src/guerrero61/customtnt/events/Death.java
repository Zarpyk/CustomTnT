package guerrero61.customtnt.events;

import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitScheduler;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mainutils.config.Config;

public class Death implements Listener {

	private final Main main;
	private final int addStormSeconds;
	public static float stormHours;
	private final String world;

	public Death(Main m) {
		main = m;
		addStormSeconds = Config.getInt(Config.Options.StormAddSeconds);
		stormHours = addStormSeconds / 60f / 60f;
		world = Config.getString(Config.Options.MainWorld);
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		boolean stormEnable = Config.getBool(Config.Options.StormEnable);
		boolean soundEnable = Config.getBool(Config.Options.StormSoundEnable);
		boolean titleEnable = Config.getBool(Config.Options.StormTitleEnable);
		boolean deathMsgEnable = Config.getBool(Config.Options.DeathMsgEnable);
		boolean deathCoordEnable = Config.getBool(Config.Options.DeathCoordsEnable);
		Player player = e.getEntity().getPlayer();
		assert player != null;
		String victim = player.getName();
		if (stormEnable) {
			// Codigo modificado de PermadeathCore por vo1d & SebazCRC
			boolean weather = Objects.requireNonNull(Bukkit.getWorld(world)).hasStorm();
			int stormDuration = Objects.requireNonNull(Bukkit.getWorld(world)).getWeatherDuration();
			int stormTicksToSeconds = stormDuration / 20;
			int stormIncrement = Math.round(stormTicksToSeconds + this.addStormSeconds);
			int inc = Math.round(stormIncrement);
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:weather thunder");
			if (weather) {
				Objects.requireNonNull(Bukkit.getWorld(world)).setWeatherDuration(inc * 20);
			} else {
				Objects.requireNonNull(Bukkit.getWorld(world)).setWeatherDuration(addStormSeconds * 20);
			}
		}
		if (deathMsgEnable) {
			String DeathChatMessage = Config.getString(Config.Options.DeathMsg);
			Bukkit.broadcastMessage(Formatter.FText(DeathChatMessage, true, player));
		}
		if (deathCoordEnable) {
			String CoordsMessage = Config.getString(Config.Options.DeathCoordsMsg);
			String Dx = Integer.toString(player.getLocation().getBlockX());
			String Dy = Integer.toString(player.getLocation().getBlockY());
			String Dz = Integer.toString(player.getLocation().getBlockZ());
			Bukkit.broadcastMessage(Formatter
					.FText(CoordsMessage.replace("%x%", Dx).replace("%y%", Dy).replace("%z%", Dz), true, player));
		}
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleSyncDelayedTask(main, () -> {
			if (stormEnable) {
				String DeathTrainMessage = Config.getString(Config.Options.DeathTrainMsg);
				Bukkit.broadcastMessage(
						Formatter.FText(DeathTrainMessage.replace("%time%", Float.toString(stormHours)), true, player));
			}
			if (soundEnable) {
				for (Player p : Bukkit.getOnlinePlayers())
					p.playSound(p.getLocation(), Sound.ENTITY_SKELETON_HORSE_DEATH, 10.0F, 1.0F);
			}
		}, 75L);
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (titleEnable) {
				String ServerMessageTitle = Config.getString(Config.Options.DeathTitle);
				String ServerMessageSubtitle = Config.getString(Config.Options.DeathSubtitle);
				p.sendTitle(Formatter.FText(ServerMessageTitle, true, player),
						Formatter.FText(ServerMessageSubtitle, true, player), 10, 70, 20);
			}
			if (soundEnable) {
				p.playSound(p.getLocation(), Sound.ENTITY_BLAZE_DEATH, 10.0F, -5.0F);
			}
		}
	}
}
