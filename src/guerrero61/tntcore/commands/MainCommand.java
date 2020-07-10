package guerrero61.tntcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import guerrero61.tntcore.Main;

public class MainCommand implements CommandExecutor {

	private Main main;
	private Boolean isPlayer;
	private Player player;

	public MainCommand(Main main) {
		this.main = main;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			isPlayer = true;
			player = (Player) sender;
		} else isPlayer = false;
		
		if (args.length > 0) {
			switch (args[0].toLowerCase()) {
			case "check":
					new Check(isPlayer, sender);
					return true;
			case "reload":
				main.reloadConfig();
				if (sender instanceof Player) {
					player = (Player) sender;
					player.sendMessage(Main.FText(Main.getString("Messages.Reload")));
				} else {
					Bukkit.getConsoleSender().sendMessage(Main.FText(Main.getString("Messages.Reload")));
				}
				return true;
			default:
				if (sender instanceof Player) {
					player = (Player) sender;
					player.sendMessage(Main.FText(Main.getString("Messages.no-exist")));
					
				} else {
					Bukkit.getConsoleSender().sendMessage(Main.FText(Main.getString("Messages.no-exist")));
				}
				return true;
			}
		} else {
			
			String[] commands = new String[] {
					Main.FTextNPrefix("&a--------&c&lTnTCore&a--------"),
					Main.FTextNPrefix("&6/tnt check &7- Comprobación basica del funcionamiento."),
					Main.FTextNPrefix("&6/tnt reload &7- Sirve para recargar la configuración."),
					Main.FTextNPrefix("&a--------&c&l-------&a--------"),
			};
			
			if (sender instanceof Player) {
				player = (Player) sender;
				player.sendMessage(Main.FText(Main.getString("Messages.no-exist")));
				return true;
			} else {
				Bukkit.getConsoleSender()
						.sendMessage(Main.FText(Main.getString("Messages.no-exist")));
				return false;
			}
		}

	}

}
