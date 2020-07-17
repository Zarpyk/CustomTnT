package guerrero61.tntcore.commands;

import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import guerrero61.tntcore.Main;
import guerrero61.tntcore.MainUtils.Config;
import guerrero61.tntcore.MainUtils.RegisterEvents;
import net.dv8tion.jda.api.JDA;

public class MainCommand implements CommandExecutor {

	private final Main main;
	private final JDA api;
	boolean isPlayer;
	Player player;

	public MainCommand(Main main, JDA a) {
		this.main = main;
		api = a;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			isPlayer = true;
			player = (Player) sender;
		} else
			isPlayer = false;

		if (args.length > 0) {
			switch (args[0].toLowerCase()) {
			case "check":
				new Check(isPlayer, sender);
				return true;
			case "reload":
				Bukkit.getPluginManager().disablePlugin(main);
				Objects.requireNonNull(Bukkit.getPluginManager().getPlugin(main.name)).reloadConfig();
				Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> {
					Bukkit.getPluginManager().enablePlugin(main);
					new RegisterEvents().registerDiscord(main);
					if (isPlayer) {
						player.sendMessage(Main.FText(Config.getString("Reload", Config.CONFIG.Messages)));
					} else {
						Bukkit.getConsoleSender()
								.sendMessage(Main.FText(Config.getString("Reload", Config.CONFIG.Messages)));
					}
				}, 100L);
				return true;
			default:
				SendHelp();
				return true;
			}
		} else {
			SendHelp();
			return true;
		}
	}

	private void SendHelp() {
		String[] helpMsg = new String[] { Main.FTextNPrefix("&a-----------------&c&lTnTCore&a-----------------"),
				Main.FTextNPrefix("&6/tnt check &7- Comprobación basica del funcionamiento."),
				Main.FTextNPrefix("&6/tnt reload &7- Sirve para recargar la configuración."),
				Main.FTextNPrefix("&a------------------------------------------"), };

		if (isPlayer) {
			for (String s : helpMsg) {
				player.sendMessage(s);
			}
		} else {
			for (String s : helpMsg) {
				Bukkit.getConsoleSender().sendMessage(Main.FText(Main.FText(s)));
			}
		}
	}
}
