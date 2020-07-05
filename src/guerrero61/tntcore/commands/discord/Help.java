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

public class Help implements MessageCreateListener {

	@Override
	public void onMessageCreate(MessageCreateEvent event) {
		if (!event.getMessageContent().equalsIgnoreCase("/help")) {
			return;
		}

		String ip = Bukkit.getServer().getIp() + ":" + Bukkit.getServer().getPort();

		EmbedBuilder embed = new EmbedBuilder().setAuthor("Comandos disponibles", "", event.getMessageAuthor().getAvatar())
				.addField("/info", "Sirve para ver la información del server de minecraft")
				.addField("/report - /suggest", "Sirve para ver donde reportar o sugerir")
				.setUrl("https://github.com/GuerreroCraft61/TnTCoreReport/issues").setColor(new Color(255, 61, 61))
				.setFooter(ip, "https://imgur.com/jrz2u0a.png").setTimestampToNow();

		new MessageBuilder().setEmbed(embed).send(event.getChannel());
		event.getMessage().delete();
	}
}
