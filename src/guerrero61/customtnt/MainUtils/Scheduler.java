package guerrero61.customtnt.MainUtils;

import java.awt.*;

import org.bukkit.Bukkit;

import guerrero61.customtnt.Main;

public class Scheduler {

	public void startMessageDelayScheduler(Main m) {
		Bukkit.getScheduler()
				.scheduleSyncDelayedTask(m,
						() -> ReloadStatus.startStopToDiscord("https://imgur.com/uIcXfam.png", m.api,
								Config.getString(Config.Options.MessagesStart), new Color(125, 255, 100), "online"),
						25L);
	}

	public void reloadStatusScheduler(Main m) {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(m, () -> ReloadStatus.reloadStatus(m.api, "online"), 12000L,
				12000L);
	}

}