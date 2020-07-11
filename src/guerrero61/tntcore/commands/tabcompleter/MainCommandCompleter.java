package guerrero61.tntcore.commands.tabcompleter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class MainCommandCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 1) {
            List<String> subcommands = new ArrayList<>();
            subcommands.add("check");
            subcommands.add("reload");
            return subcommands;
        }
        return null;
    }

}
