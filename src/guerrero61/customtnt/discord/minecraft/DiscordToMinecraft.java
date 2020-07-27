package guerrero61.customtnt.discord.minecraft;

import org.bukkit.Bukkit;

import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mainutils.config.Config;
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
		if (!event.getChannel().getId().equals(Config.getString(Config.Options.ChannelsSendMsg))
				|| event.getAuthor().getId().equals(api.getSelfUser().getId()))
			return;
		for (String commandList : Config.getStringList(Config.Options.MessagesCommandList)) {
			if (event.getMessage().getContentDisplay().equalsIgnoreCase(commandList)) {
				return;
			}
		}
		Bukkit.broadcastMessage(Formatter.FText(Config.getString(Config.Options.MessagesDiscordToMinecraftChat)
				.replace("%nick%", event.getAuthor().getName())
				.replace("%msg%", Formatter.FText(event.getMessage().getContentDisplay(), true)), true));
	}
}
