package guerrero61.customtnt.commands.tabcompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class MainCommandCompleter implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

		if (sender instanceof Player) {
			if (!sender.hasPermission("ctnt.*")) {
				return null;
			}
		}

		if (args.length == 1) {
			List<String> subcommands = new ArrayList<>();
			subcommands.add("check");
			subcommands.add("reload");
			subcommands.add("debug");
			subcommands.add("skills");
			return subcommands;
		}

		if (args.length > 1) {
			if (sender instanceof Player) {
				if (args[0].equals("skills")) {
					if (args.length == 2) {
						List<String> count = new ArrayList<>();
						for (int i = 1; i < 10; i++) {
							count.add(Integer.toString(i));
						}
						return count;
					}
					if (args[1].equals("1")) {
						Player player = (Player) sender;
						Block targ = player.getTargetBlock(null, 5);
						switch (args.length) {
						case 3:
						case 6:
							return Collections.singletonList(targ.getX() + "");
						case 4:
						case 7:
							return Collections.singletonList(targ.getY() + "");
						case 5:
						case 8:
							return Collections.singletonList(targ.getZ() + "");
						}
					} else if (args[1].equals("2")) {
						Player player = (Player) sender;
						Block targ = player.getTargetBlock(null, 5);
						switch (args.length) {
						case 3:
							return Collections.singletonList(targ.getX() + "");
						case 4:
							return Collections.singletonList(targ.getY() + "");
						case 5:
							return Collections.singletonList(targ.getZ() + "");
						}
					}
				}
			}
		}
		return null;
	}
}
