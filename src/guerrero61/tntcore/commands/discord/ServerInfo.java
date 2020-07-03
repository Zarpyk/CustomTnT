package guerrero61.tntcore.commands.discord;

import java.awt.Color;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.event.message.MessageEditEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import guerrero61.tntcore.Main;

public class ServerInfo implements MessageCreateListener {

	private DiscordApi api;
	private Main main;

	public ServerInfo(DiscordApi a, Main m) {
		api = a;
		main = m;
	}

	@Override
	public void onMessageCreate(MessageCreateEvent event) {
		// Ignore any message that is not !whoIsOnline
		if (!event.getMessageContent().equalsIgnoreCase("/info")) {
			return;
		}

		// Collect the names of all online players
		String onlinePlayers = Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.joining(","))
				.replace(",", "\n");
		Integer onlinePlayersCount = main.getServer().getOnlinePlayers().size();

		Icon avatar = api.getYourself().getAvatar();
		String ip = Bukkit.getServer().getIp() + ":" + Bukkit.getServer().getPort();

		EmbedBuilder embed = new EmbedBuilder().setAuthor("Información del server | TnTServer").setThumbnail(avatar)
				.addField("Versión",
						Bukkit.getVersion().replace("git-", "") + "\n"
								+ (Bukkit.getOnlineMode() ? "Premium" : "Premium/No Premium"),
						true)
				.addField("Jugadores " + onlinePlayersCount + "/" + Integer.toString(main.getServer().getMaxPlayers()),
						(onlinePlayersCount == 0) ? "No hay nadie jugando" : onlinePlayers, true)
				.setColor(new Color(255, 61, 61)).setFooter(ip, "https://imgur.com/jrz2u0a.png").setTimestampToNow();

		// Check if there are any online players
		if (onlinePlayers.isEmpty()) {
			new MessageBuilder().setEmbed(embed).send(event.getChannel());
			return;
		}

		// Display the names of all online players
		new MessageBuilder().setEmbed(embed).send(event.getChannel());
	}
}
