package guerrero61.tntcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import guerrero61.tntcore.Main;

public class Check {

	public Check(boolean isPlayer, CommandSender sender) {
		String prefix = Main.getString("Prefix");
		if (isPlayer) {
			Player player = (Player) sender;
			String playerMsg = Main.getString("Messages.Check.player");
			player.sendMessage(Main.FText(playerMsg));
		} else {
			String consoleMsg = Main.getString("Messages.Check.console");
			Bukkit.getConsoleSender().sendMessage(Main.FText(consoleMsg));
		}
	}
}
