package guerrero61.tntcore.MainUtils;

import java.awt.*;

import org.bukkit.Bukkit;

import guerrero61.tntcore.Main;

public class Scheduler {

	public void startMessageDelayScheduler(Main m) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(m,
				() -> ReloadStatus.startStopToDiscord("https://imgur.com/uIcXfam.png", m.api,
						Main.getString("Discord.start-msg"), new Color(125, 255, 100), "online"),
				20L);
	}

	public void reloadStatusScheduler(Main m) {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(m, () -> ReloadStatus.reloadStatus(m.api, "online"), 0L, 6000L);
	}

}
