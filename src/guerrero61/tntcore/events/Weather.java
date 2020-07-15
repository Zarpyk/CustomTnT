package guerrero61.tntcore.events;

import java.awt.*;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import guerrero61.tntcore.Main;
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
			String StormMessage = Main.getString("Storm.end-msg");
			Bukkit.broadcastMessage(Main.FText(StormMessage));

			EmbedBuilder startEmbed = new EmbedBuilder().setAuthor(Main.removeFormatter(StormMessage),
					"https://imgur.com/U7bc9ii.png", "https://imgur.com/U7bc9ii.png")
					.setColor(new Color(125, 255, 100));
			TextChannel textChannel = Objects
					.requireNonNull(api.getTextChannelById(Main.getString("Discord.send-msg-channel")));
			textChannel.sendMessage(startEmbed.build()).queue();
		}
	}

}
