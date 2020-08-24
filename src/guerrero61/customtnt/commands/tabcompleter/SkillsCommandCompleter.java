package guerrero61.customtnt.commands.tabcompleter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SkillsCommandCompleter extends CommandCompleterMethods {

    public List<String> use(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2) {
            List<String> count = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                count.add(Integer.toString(i));
            }
            return sortList(count, args);
        }
        switch (args[1]) {
            //tnt skills 1 <x1> <y2> <z3> <<x2>> <<y2>> <<z2>> OK
            //tnt skills 2 <player/x> <y> <z> OK
            //tnt skills 3 <player> OK
            //tnt skills 4 <player> OK
            //tnt skills 7 <player> OK
            //tnt skills 8 <player> OK
            //tnt skills 9 <player/x> <y> <z> OK
            //tnt skills 10 <player/x> <y> <z> OK
            case "1": {
                Player player = (Player) sender;
                return targetBlock(player, args, true);
            }
            case "2":
            case "6":
            case "9":
            case "10": {
                Player player = (Player) sender;
                List<String> list = targetBlock(player, args);
                if (args.length == 3) {
                    list.addAll(playerList());
                }
                return sortList(list, args);
            }
            case "3":
            case "4":
            case "5":
            case "7":
            case "8": {
                if (args.length == 3) {
                    return sortList(playerList(), args);
                }
            }
            default: {
                return null;
            }
        }
    }

}
