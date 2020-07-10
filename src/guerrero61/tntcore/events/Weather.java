package guerrero61.tntcore.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import guerrero61.tntcore.Main;

public class Weather implements Listener {

	@EventHandler
	public void onWeatherStorm(WeatherChangeEvent event) {
		Boolean weather = event.getWorld().hasStorm();
		if (weather) {
			String StormMessage = Main.getString("Storm.end-msg");
			Bukkit.broadcastMessage(Main.FText(StormMessage));
		}
	}

}
