package guerrero61.customtnt.formatters.chat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mainutils.config.Config;

public class ColorChat implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void playerChat(AsyncPlayerChatEvent event) {
		String message = event.getMessage();
		event.setFormat(Config.getString(Config.Options.ChatFormat));
		event.setMessage(Formatter.FText(message, true));
	}
}
