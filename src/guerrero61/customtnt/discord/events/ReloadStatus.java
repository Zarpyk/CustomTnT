package guerrero61.customtnt.discord.events;

import java.awt.*;
import java.util.Objects;

import org.bukkit.Bukkit;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.MainUtils.Formatter;
import guerrero61.customtnt.MainUtils.Config.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;

public class ReloadStatus {

	public static void startStopToDiscord(String url, JDA api, String msg, Color color, String status) {
		EmbedBuilder startEmbed = new EmbedBuilder().setAuthor(msg, url, url).setColor(color);
		TextChannel textChannel = Objects
				.requireNonNull(api.getTextChannelById(Config.getString(Config.Options.ChannelsSendMsg)));
		textChannel.sendMessage(startEmbed.build()).complete();

		reloadStatus(api, status);
	}

	public static void reloadStatus(JDA api, String status) {
		TextChannel textChannel = Objects
				.requireNonNull(api.getTextChannelById(Config.getString(Config.Options.ChannelsSendMsg)));

		textChannel.getManager()
				.setTopic(Formatter.FText(
						Config.getString(Config.Options.ChannelDescription)
								.replace("%status%",
										"Servidor " + status
												+ (status.equals("online") ? (" | " + Main.getPlayerCount()) : ""))
								.replace("%unique-players%", Integer.toString(Bukkit.getOfflinePlayers().length)),
						true))
				.queue();

	}

}
