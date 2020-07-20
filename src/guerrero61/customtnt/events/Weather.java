package guerrero61.customtnt.events;

import java.awt.*;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.MainUtils.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;

public class Weather implements Listener {

	private final JDA api;

	public Weather(JDA a) {
		api = a;
	}

	@EventHandler
	public void onWeatherStorm(WeatherChangeEvent event) {
		boolean weather = event.getWorld().hasStorm();
		if (weather) {
			String StormMessage = Config.getString(Config.Options.StormEndMsg);
			Bukkit.broadcastMessage(Main.FText(StormMessage));

			EmbedBuilder startEmbed = new EmbedBuilder().setAuthor(Main.removeFormatter(StormMessage),
					"https://imgur.com/U7bc9ii.png", "https://imgur.com/U7bc9ii.png")
					.setColor(new Color(125, 255, 100));
			TextChannel textChannel = Objects
					.requireNonNull(api.getTextChannelById(Config.getString(Config.Options.ChannelsSendMsg)));
			textChannel.sendMessage(startEmbed.build()).queue();
		}
	}

}