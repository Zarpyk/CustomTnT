package guerrero61.tntcore.MainUtils;

import java.util.Objects;

import org.bukkit.Bukkit;

import guerrero61.tntcore.Main;

public class StormActionBar {

	public static String stormTime;

	public void StormAB(Main main) {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> {
			int segundosbrutos = (Objects.requireNonNull(Bukkit.getWorld("world")).getWeatherDuration() / 20);
			int hours = Math.toIntExact(segundosbrutos % 86400L / 3600L);
			int minutes = Math.toIntExact(segundosbrutos % 3600L / 60L);
			int seconds = Math.toIntExact(segundosbrutos % 60L);
			int days = Math.toIntExact(segundosbrutos / 86400L);
			stormTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
			if (days < 1L && Objects.requireNonNull(Bukkit.getWorld("world")).hasStorm()) {
				String Message = Main.getString("Messages.Death.train-actionbar");
				Bukkit.getOnlinePlayers().forEach(player -> {
					assert Message != null;
					player.sendActionBar(Main.FTextNPrefix(Message.replace("%time%", stormTime)));
				});
			}
		}, 0L, 20L);
	}

}
