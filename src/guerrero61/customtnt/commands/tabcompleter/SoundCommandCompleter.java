package guerrero61.customtnt.commands.tabcompleter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class SoundCommandCompleter extends CommandCompleterMethods {

    public List<String> use(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            case 2: {
                return sortList(soundList(), args);
            }
            case 3: {
                return sortList(new ArrayList<>(List.of("volume", "0", "0.25", "0.5", "0.75", "1")), args);
            }
            case 4: {
                return sortList(new ArrayList<>(List.of("pitch", "0", "0.5", "1", "1,5", "2")), args);
            }
            default: {
                return null;
            }
        }
    }
}
