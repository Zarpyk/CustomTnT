package guerrero61.tntcore.discord.minecraft;

import org.bukkit.Bukkit;

import guerrero61.tntcore.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DiscordToMinecraft extends ListenerAdapter {

	private final JDA api;

	public DiscordToMinecraft(JDA a) {
		api = a;
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (!event.getChannel().getId().equals(Main.getString("Discord.send-msg-channel"))
				|| event.getAuthor().getId().equals(api.getSelfUser().getId()))
			return;
		for (String commandList : Main.getStringList("Discord.command-list")) {
			if (event.getMessage().getContentDisplay().equals(commandList)) {
				return;
			}
		}
		Bukkit.broadcastMessage(Main.FTextNPrefix(Main.getString("Discord.discord-to-minecraft-chat-msg"))
				.replace("%nick%", event.getAuthor().getName())
				.replace("%msg%", event.getMessage().getContentDisplay()));
	}
}
