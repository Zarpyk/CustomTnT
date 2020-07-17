package guerrero61.tntcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import guerrero61.tntcore.Main;
import guerrero61.tntcore.MainUtils.Config;

public class Check {

	public Check(boolean isPlayer, CommandSender sender) {
		if (isPlayer) {
			Player player = (Player) sender;
			String playerMsg = Config.getString("Check.player", Config.CONFIG.Messages);
			player.sendMessage(Main.FText(playerMsg));
		} else {
			String consoleMsg = Config.getString("Check.console", Config.CONFIG.Messages);
			Bukkit.getConsoleSender().sendMessage(Main.FText(consoleMsg));
		}
	}
}
