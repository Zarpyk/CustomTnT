package guerrero61.tntcore.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import guerrero61.tntcore.Main;

public class Weather implements Listener {

	private Main main;
	private FileConfiguration config;
	private String prefix;

	public Weather(Main m) {
		main = m;
		config = main.getConfig();
		prefix = config.getString("Prefix");
	}

	@EventHandler
	public void onWeatherStorm(WeatherChangeEvent event) {
		boolean weather = event.getWorld().hasStorm();
		if (weather) {
			String StormMessage = config.getString("Storm.end-msg");
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', prefix + StormMessage));
		}
	}

}
