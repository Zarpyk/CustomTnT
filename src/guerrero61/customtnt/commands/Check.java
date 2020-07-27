package guerrero61.customtnt.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.MainUtils.Formatter;
import guerrero61.customtnt.MainUtils.Config.Config;

public class Check {

	public Check(boolean isPlayer, CommandSender sender) {
		if (isPlayer) {
			Player player = (Player) sender;
			String playerMsg = Config.getString(Config.Options.CheckPlayer);
			player.sendMessage(Formatter.FText(playerMsg));
		} else {
			String consoleMsg = Config.getString(Config.Options.CheckConsole);
			Main.consoleMsg(Formatter.FText(consoleMsg));
		}
	}
}
