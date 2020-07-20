package guerrero61.customtnt.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.MainUtils.Config;

public class Check {

	public Check(boolean isPlayer, CommandSender sender) {
		if (isPlayer) {
			Player player = (Player) sender;
			String playerMsg = Config.getString(Config.Options.CheckPlayer);
			player.sendMessage(Main.FText(playerMsg));
		} else {
			String consoleMsg = Config.getString(Config.Options.CheckConsole);
			Bukkit.getConsoleSender().sendMessage(Main.FText(consoleMsg));
		}
	}
}
