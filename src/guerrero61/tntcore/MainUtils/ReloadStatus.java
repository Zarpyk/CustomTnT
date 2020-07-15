package guerrero61.tntcore.MainUtils;

import java.awt.*;
import java.util.Objects;

import guerrero61.tntcore.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;

public class ReloadStatus {

	public static void startStopToDiscord(String url, JDA api, String msg, Color color, String status) {
		EmbedBuilder startEmbed = new EmbedBuilder().setAuthor(msg, url, url).setColor(color);
		TextChannel textChannel = Objects
				.requireNonNull(api.getTextChannelById(Main.getString("Discord.send-msg-channel")));
		textChannel.sendMessage(startEmbed.build()).queue();

		reloadStatus(api, status);
	}

	public static void reloadStatus(JDA api, String status) {
		TextChannel textChannel = Objects
				.requireNonNull(api.getTextChannelById(Main.getString("Discord.send-msg-channel")));

		if (!status.equals("offline")) {
			textChannel.getManager()
					.setTopic(Main.getString("Discord.msg-channel-description")
							.replace("%status%", "Servidor " + status + (" | " + Main.getPlayerCount()))
							.replace("%unique-players%", Main.getInt("Discord.unique-players").toString()))
					.queue();
		} else {
			textChannel.getManager()
					.setTopic(Main.getString("Discord.msg-channel-description")
							.replace("%status%", "Servidor " + status + "")
							.replace("%unique-players%", Main.getInt("Discord.unique-players").toString()))
					.queue();
		}

	}

}
