package guerrero61.customtnt.discord.events;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.mainutils.Formatter;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public class DiscordReady implements EventListener {
	@Override
	public void onEvent(GenericEvent event) {
		if (event instanceof ReadyEvent) {
			Main.consoleMsg(Formatter.FText("&aSe ha cargado el Java Discord API correctamente"));
		}
		if (event instanceof ShutdownEvent) {
			Main.consoleMsg(Formatter.FText("&cEl bot de Discord se ha desconectado"));
		}
	}
}
