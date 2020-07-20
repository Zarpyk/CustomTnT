package guerrero61.customtnt.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.MainUtils.Formatter;
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
				new Reload(isPlayer, sender, main);
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
		String[] helpMsg = new String[] { "&a-----------------&c&lTnTCore&a-----------------",
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
