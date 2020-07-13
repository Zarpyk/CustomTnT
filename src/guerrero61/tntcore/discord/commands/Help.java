package guerrero61.tntcore.discord.commands;

import java.awt.*;
import java.time.Instant;

import org.bukkit.Bukkit;

import guerrero61.tntcore.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Help extends ListenerAdapter {

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (Main.checkCommand("help", event.getMessage(), event.getChannel())) {
			return;
		}

		String ip = Bukkit.getServer().getIp() + ":" + Bukkit.getServer().getPort();

		String autorAvatar = event.getAuthor().getAvatarUrl();

		EmbedBuilder embed = new EmbedBuilder().setAuthor("Comandos disponibles", autorAvatar, autorAvatar)
				.addField("/info", "Sirve para ver la información del server de minecraft", false)
				.addField("/report - /suggest", "Sirve para ver donde reportar o sugerir", false)
				.setColor(new Color(255, 61, 61)).setFooter(ip, "https://imgur.com/jrz2u0a.png")
				.setTimestamp(Instant.now());

		event.getChannel().sendMessage(embed.build()).complete();
		event.getMessage().delete().complete();
	}
}
