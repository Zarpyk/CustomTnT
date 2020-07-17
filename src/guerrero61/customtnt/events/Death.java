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
import guerrero61.customtnt.MainUtils.Config;

public class Death implements Listener {

	private final Main main;
	private final int addStormSeconds;
	private final float stormHours;
	private final String world;

	public Death(Main m) {
		main = m;
		addStormSeconds = Config.getInt("Storm.add-seconds");
		stormHours = addStormSeconds / 60f / 60f;
		world = Config.getString("MainWorld");
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		boolean stormEnable = Config.getBool("Storm.enable");
		boolean soundEnable = Config.getBool("Storm.sound-enable");
		boolean titleEnable = Config.getBool("Storm.title-enable");
		String victim = Objects.requireNonNull(e.getEntity().getPlayer()).getName();
		if (stormEnable) {
			// Codigo modificado de PermadeathCore por vo1d & SebazCRC
			boolean weather = Objects.requireNonNull(Bukkit.getWorld(world)).hasStorm();
			String DeathChatMessage = Config.getString("Death.msg", Config.CONFIG.Messages);
			Bukkit.broadcastMessage(Main.FTextNPrefix(DeathChatMessage.replace("%player%", victim)));
			if (Config.getBool("Death.coords-enable")) {
				String CoordsMessage = Config.getString("Death.coords-msg", Config.CONFIG.Messages);
				String Dx = Integer.toString(e.getEntity().getPlayer().getLocation().getBlockX());
				String Dy = Integer.toString(e.getEntity().getPlayer().getLocation().getBlockY());
				String Dz = Integer.toString(e.getEntity().getPlayer().getLocation().getBlockZ());
				Bukkit.broadcastMessage(
						Main.FTextNPrefix(CoordsMessage.replace("%x%", Dx).replace("%y%", Dy).replace("%z%", Dz)));
			}
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
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleSyncDelayedTask(main, () -> {
			if (stormEnable) {
				String DeathTrainMessage = Config.getString("Death.train", Config.CONFIG.Messages);
				Bukkit.broadcastMessage(
						Main.FTextNPrefix(DeathTrainMessage.replace("%time%", Float.toString(stormHours))));
			}
			if (soundEnable) {
				for (Player player : Bukkit.getOnlinePlayers())
					player.playSound(player.getLocation(), Sound.ENTITY_SKELETON_HORSE_DEATH, 10.0F, 1.0F);
			}
		}, 75L);
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (titleEnable) {
				String ServerMessageTitle = Config.getString("Death.title", Config.CONFIG.Messages);
				String ServerMessageSubtitle = Config.getString("Messages.Death.subtitle", Config.CONFIG.Messages);
				player.sendTitle(Main.FTextNPrefix(ServerMessageTitle),
						Main.FTextNPrefix(ServerMessageSubtitle.replace("%player%", victim)), 10, 70, 20);
			}
			if (soundEnable) {
				player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_DEATH, 10.0F, -5.0F);
			}
		}
	}
}
