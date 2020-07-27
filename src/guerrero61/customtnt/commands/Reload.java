package guerrero61.customtnt.commands;

import java.util.HashMap;

import org.bukkit.command.CommandSender;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mainutils.config.Config;
import guerrero61.customtnt.mainutils.registers.Registers;

public class Reload {

	public Reload(boolean isPlayer, CommandSender sender, Main main) {
		//DisableBot.Disable(main);
		Main.configMap = null;
		Main.config = null;
		main.configPath = null;
		Main.messagesConfig = null;
		main.messagesConfigFile = null;
		Main.discordConfig = null;
		main.discordConfigFile = null;

		Main.configMap = new HashMap<>();
		Registers registers = new Registers();
		registers.registerConfig(main);

		if (main.api == null) {
			registers.registerDiscord(main);
			main.api.cancelRequests();
		}
		if (isPlayer) {
			sender.sendMessage(Formatter.FText(Config.getString(Config.Options.Reload)));
		} else {
			Main.consoleMsg(Formatter.FText(Config.getString(Config.Options.Reload)));
		}
	}
}
