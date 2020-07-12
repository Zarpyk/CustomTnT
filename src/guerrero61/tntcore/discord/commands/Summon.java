package guerrero61.tntcore.discord.commands;

import org.bukkit.Bukkit;

import guerrero61.tntcore.Main;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Summon extends ListenerAdapter {

	private final Main main;

	private final String id = "290223330773172225L";

	public Summon(Main m) {
		main = m;
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (!event.getMessage().getContentDisplay().equalsIgnoreCase("/summon")
				|| event.getMessage().getAuthor().getId().equals(id)) {
			return;
		}

		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "boss summonp PhyPsi15 BossPalPepsi");
		event.getMessage().delete().complete();
	}
}
