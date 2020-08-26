package guerrero61.customtnt.commands.tabcompleter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MainCommandCompleter extends CommandCompleterMethods implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            if (!sender.hasPermission("ctnt.*")) {
                return null;
            }
        }

        if (args.length == 1) {
            return sortList(new ArrayList<>(List
                    .of("check", "reload", "debug", "skills", "sounds", "config", "event", "uninmune", "noai", "yesai")), args);
        }

        if (args.length > 1) {
            if (sender instanceof Player) {
                switch (args[0]) {
                    case "skills": {
                        return new SkillsCommandCompleter().use(sender, command, label, args);
                    }
                    case "sounds": {
                        return new SoundCommandCompleter().use(sender, command, label, args);
                    }
                    case "config": {
                        return new ConfigCommandCompleter().use(sender, command, label, args);
                    }
                    case "event": {
                        return new EventCommandCompleter().use(sender, command, label, args);
                    }
                }
            }
        }
        return null;
    }
}
