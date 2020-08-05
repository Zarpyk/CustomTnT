package guerrero61.customtnt.commands.tabcompleter;

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
            return sortList(new ArrayList<>(List.of("check", "reload", "debug", "skills", "sounds")), args[0]);
        }

        if (args.length > 1) {
            if (sender instanceof Player) {
                if (args[0].equals("skills")) {
                    if (args.length == 2) {
                        List<String> count = new ArrayList<>();
                        for (int i = 1; i <= 10; i++) {
                            count.add(Integer.toString(i));
                        }
                        return sortList(count, args[1]);
                    }
                    switch (args[1]) {
                        case "sound": {
                            switch (args.length) {
                                case 3:
                                    return sortList(new ArrayList<>(soundList()), args[2]);
                                case 4:
                                    return sortList(new ArrayList<>(List.of("Volume")), args[3]);
                                case 5:
                                    return sortList(new ArrayList<>(List.of("Pitch")), args[4]);
                            }
                        }
                        case "1": {
                            Player player = (Player) sender;
                            return targetBlock(player, args, true);
                        }
                        case "2":
                        case "3":
                        case "7": {
                            Player player = (Player) sender;
                            return targetBlock(player, args);
                        }
                    }
                }
            }
        }
        return null;
    }

    private List<String> sortList(List<String> list, String args) {
        List<String> numberListOld = new ArrayList<>(list); //in your case COMMANDS

        list.clear();

        for (String s : numberListOld)
            if (s.toLowerCase().startsWith(args.toLowerCase()))
                list.add(s);
        return list;
    }

    private List<String> soundList() {
        List<String> stringList = new ArrayList<>();
        for (Object object : Sound.class.getEnumConstants()) {
            stringList.add(object.toString());
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
                    return Collections.singletonList(targ.getX() + "");
                case 4:
                case 7:
                    return Collections.singletonList(targ.getY() + "");
                case 5:
                case 8:
                    return Collections.singletonList(targ.getZ() + "");
            }
        } else {
            switch (args.length) {
                case 3:
                    return Collections.singletonList(targ.getX() + "");
                case 4:
                    return Collections.singletonList(targ.getY() + "");
                case 5:
                    return Collections.singletonList(targ.getZ() + "");
            }
        }
        return null;
    }
}
