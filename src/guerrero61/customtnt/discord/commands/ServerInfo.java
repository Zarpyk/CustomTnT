package guerrero61.customtnt.discord.commands;

import java.awt.*;
import java.time.Instant;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mainutils.StormActionBar;
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
		if (Main.checkCommand("info", event.getMessage(), event.getChannel())) {
			return;
		}
		StringBuilder onlinePlayer = new StringBuilder();
		for (Player player : Bukkit.getOnlinePlayers()) {
			onlinePlayer.append(Formatter.FText(Formatter.FText("%essentials_afk%", true, player).equals("yes")
					? Formatter.FText("[AFK] ", true, player)
					: "", true, player)).append(player.getName()).append("\n");
		}

		String autorAvatar = event.getAuthor().getAvatarUrl();

		double TPS = Main.round(Bukkit.getServer().getTPS()[0], 2);
		String StringTPS = TPS >= 20 ? "20.0" : Double.toString(TPS);

		EmbedBuilder embed = new EmbedBuilder()
				.setAuthor("Información del server | TnTServer", autorAvatar, autorAvatar)
				.setThumbnail(api.getSelfUser().getAvatarUrl())
				.addField("Versión",
						Bukkit.getVersion().replace("git-", "") + "\n"
								+ (Bukkit.getOnlineMode() ? "Premium" : "Premium/No Premium"),
						true)
				.addField(Main.getPlayerCount(),
						(Bukkit.getOnlinePlayers().size() == 0) ? "No hay nadie jugando" : onlinePlayer.toString(),
						true)
				.addBlankField(true)
				.addField("Jugadores unicos", Integer.toString(Bukkit.getOfflinePlayers().length), true)
				.addField("TPS", StringTPS, true).addBlankField(true)
				.addField("Horas de tormenta",
						(Objects.requireNonNull(Bukkit.getWorld("world")).hasStorm() ? StormActionBar.stormTime
								: "No hay tormenta"),
						true)
				.setColor(new Color(255, 61, 61)).setFooter(Main.getIp(), "https://imgur.com/jrz2u0a.png")
				.setTimestamp(Instant.now());

		event.getChannel().sendMessage(embed.build()).queue();
		event.getMessage().delete().queue();
	}
}
