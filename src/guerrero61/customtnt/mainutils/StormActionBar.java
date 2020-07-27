package guerrero61.customtnt.mainutils;

import java.util.Objects;

import org.bukkit.Bukkit;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.mainutils.config.Config;

public class StormActionBar {

	public static String stormTime;

	public void StormAB(Main main) {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> {
			int segundosbrutos = (Objects.requireNonNull(Bukkit.getWorld("world")).getWeatherDuration() / 20);
			int days = Math.toIntExact(segundosbrutos / 86400L);
			if (days < 1L && Objects.requireNonNull(Bukkit.getWorld("world")).hasStorm()) {
				String Message = Config.getString(Config.Options.DeathTrainActionBar);
				Bukkit.getOnlinePlayers().forEach(player -> {
					assert Message != null;
					player.sendActionBar(Formatter.FText(stormTime, true, player));
				});
			}
		}, 0L, (Config.getInt(Config.Options.StormActionBarDelay) * 20));
		Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> {
			int segundosbrutos = (Objects.requireNonNull(Bukkit.getWorld("world")).getWeatherDuration() / 20);
			int hours = Math.toIntExact(segundosbrutos % 86400L / 3600L);
			int minutes = Math.toIntExact(segundosbrutos % 3600L / 60L);
			int seconds = Math.toIntExact(segundosbrutos % 60L);
			stormTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
		}, 0L, 20L);
	}

}
