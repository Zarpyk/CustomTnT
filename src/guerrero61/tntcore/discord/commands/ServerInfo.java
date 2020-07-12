package guerrero61.tntcore.discord.commands;

import java.awt.*;
import java.time.Instant;
import java.util.Objects;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import guerrero61.tntcore.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ServerInfo extends ListenerAdapter {

	private final JDA api;
	private final Main main;

	public ServerInfo(JDA a, Main m) {
		api = a;
		main = m;
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (!event.getMessage().getContentDisplay().equalsIgnoreCase("/info")) {
			return;
		}

		String onlinePlayers = Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.joining(","))
				.replace(",", "\n");
		int onlinePlayersCount = main.getServer().getOnlinePlayers().size();

		String ip = Bukkit.getServer().getIp() + ":" + Bukkit.getServer().getPort();

		EmbedBuilder embed = new EmbedBuilder()
				.setAuthor("Información del server | TnTServer", "", event.getAuthor().getAvatarUrl())
				.setThumbnail(api.getSelfUser().getAvatarUrl())
				.addField("Versión",
						Bukkit.getVersion().replace("git-", "") + "\n"
								+ (Bukkit.getOnlineMode() ? "Premium" : "Premium/No Premium"),
						true)
				.addField("Jugadores " + onlinePlayersCount + "/" + main.getServer().getMaxPlayers(),
						(onlinePlayersCount == 0) ? "No hay nadie jugando" : onlinePlayers, true)
				.addField("Horas de tormenta",
						(Objects.requireNonNull(Bukkit.getWorld("world")).hasStorm() ? main.stormTime
								: "No hay tormenta"),
						false)
				.setColor(new Color(255, 61, 61)).setFooter(ip, "https://imgur.com/jrz2u0a.png")
				.setTimestamp(Instant.now());

		event.getChannel().sendMessage(embed.build()).queue();
		event.getMessage().delete().queue();
	}
}
