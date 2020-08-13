package guerrero61.customtnt.commands.tabcompleter;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainCommandCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            if (!sender.hasPermission("ctnt.*")) {
                return null;
            }
        }

        if (args.length == 1) {
            return sortList(new ArrayList<>(List.of("check", "reload", "debug", "skills", "sounds")), args);
        }

        if (args.length > 1) {
            if (sender instanceof Player) {
                if (args[0].equals("skills")) {
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
        }
        return null;
    }

    private List<String> sortList(List<String> list, String[] args) {
        List<String> numberListOld = new ArrayList<>(list); //in your case COMMANDS
        List<String> newList = new ArrayList<>();

        for (String s : numberListOld)
            if (s.toLowerCase().startsWith(args[args.length - 1]))
                newList.add(s);
        return newList;
    }

    private List<String> soundList() {
        List<String> stringList = new ArrayList<>();
        for (Object object : Sound.class.getEnumConstants()) {
            stringList.add(object.toString());
        }
        return stringList;
    }

    private List<String> playerList() {
        List<String> stringList = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            stringList.add(player.getName());
        }
        return stringList;
    }

    private List<String> targetBlock(Player player, String[] args) {
        return targetBlock(player, args, false);
    }

    private List<String> targetBlock(Player player, String[] args, boolean twoCoord) {
        Block targ = player.getTargetBlock(null, 5);
        if (twoCoord) {
            switch (args.length) {
                case 3:
                case 6:
                    return sortList(Collections.singletonList(targ.getX() + ""), args);
                case 4:
                case 7:
                    return sortList(Collections.singletonList(targ.getY() + ""), args);
                case 5:
                case 8:
                    return sortList(Collections.singletonList(targ.getZ() + ""), args);
            }
        } else {
            switch (args.length) {
                case 3:
                    return sortList(Collections.singletonList(targ.getX() + ""), args);
                case 4:
                    return sortList(Collections.singletonList(targ.getY() + ""), args);
                case 5:
                    return sortList(Collections.singletonList(targ.getZ() + ""), args);
            }
        }
        return null;
    }
}
