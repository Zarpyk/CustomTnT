package guerrero61.tntcore.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import guerrero61.tntcore.Main;
import net.dv8tion.jda.api.JDA;

public class MinecraftToDiscord implements Listener {

	private final Main main;
	private final JDA api;

	public MinecraftToDiscord(Main m, JDA a) {
		main = m;
		api = a;
	}

	@EventHandler
	public void playerChat(AsyncPlayerChatEvent event) {
		String message = event.getMessage();
		api.getGuildChannelById(Main.getString(""));
	}

}
