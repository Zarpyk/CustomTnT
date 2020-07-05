package guerrero61.tntcore.commands.discord;

import java.awt.Color;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.event.message.MessageEditEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import guerrero61.tntcore.Main;

public class Summon implements MessageCreateListener {

	private Main main;

	private Long id = 290223330773172225L;
	
	public Summon(Main m) {
		main = m;
	}

	@Override
	public void onMessageCreate(MessageCreateEvent event) {
		if (!event.getMessageContent().equalsIgnoreCase("/summon")
				|| event.getMessage().getAuthor().getId() != id) {
			main.getLogger().warning(Long.toString(event.getMessage().getAuthor().getId()));
			return;
		}
		
		main.getLogger().warning(Long.toString(event.getMessage().getAuthor().getId()));

		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "boss summonp PhyPsi15 BossPalPepsi");
		event.getMessage().delete();
	}
}
