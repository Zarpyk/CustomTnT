package guerrero61.customtnt.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.MainUtils.Formatter;
import guerrero61.customtnt.MainUtils.Config.Config;
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

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			isPlayer = true;
			player = (Player) sender;
			if (!player.hasPermission("ctnt.*")) {
				player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsNoPerm), player));
				return true;
			}
		} else
			isPlayer = false;

		if (args.length > 0) {
			Main.debug(args[0]);
			switch (args[0].toLowerCase()) {
			case "check":
				new Check(isPlayer, sender);
				return true;
			case "reload":
				new Reload(isPlayer, sender, main);
				return true;
			case "debug":
				if (Config.getBool(Config.Options.DebugMode)) {
					Config.set(Config.Options.DebugMode, false);
					player.sendMessage(Formatter.FText("&c&lDebug Mode Off", true, player));
				} else {
					Config.set(Config.Options.DebugMode, true);
					player.sendMessage(Formatter.FText("&a&lDebug Mode On", true, player));
				}
				return true;
			case "skills":
				return new DragonSkills(main).onCommand(sender, command, label, args);
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
		String[] helpMsg = new String[] { "&a-----------------&c&lCustomTnT&a-----------------",
				"&6/tnt check &7- Comprobación basica del funcionamiento.",
				"&6/tnt reload &7- Sirve para recargar la configuración.",
				"&a------------------------------------------" };

		if (isPlayer) {
			for (String s : helpMsg) {
				player.sendMessage(Formatter.FText(s, true, player));
			}
		} else {
			for (String s : helpMsg) {
				Main.consoleMsg(Formatter.FText(s, true));
			}
		}
	}
}
