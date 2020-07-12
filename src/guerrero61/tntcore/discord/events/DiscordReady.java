package guerrero61.tntcore.discord.events;

import org.bukkit.Bukkit;

import guerrero61.tntcore.Main;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public class DiscordReady implements EventListener {
	@Override
	public void onEvent(GenericEvent event) {
		if (event instanceof ReadyEvent) {
			Bukkit.getConsoleSender().sendMessage(Main.FText("&aSe ha cargado el Java Discord API correctamente"));
		}
		if (event instanceof DisconnectEvent) {
			Bukkit.getConsoleSender().sendMessage(Main.FText("&cEl bot de Discord se ha desconectado"));
		}
	}
}
