package guerrero61.customtnt.commands.tabcompleter;

import guerrero61.customtnt.events.mobs.Mobs.MobType;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandCompleterMethods {

    protected List<String> sortList(List<String> list, String[] args) {
        List<String> numberListOld = new ArrayList<>(list); //in your case COMMANDS
        List<String> newList = new ArrayList<>();

        for (String s : numberListOld)
            if(s.toLowerCase().startsWith(args[args.length - 1])) newList.add(s);
        return newList;
    }

    protected List<String> soundList() {
        List<String> stringList = new ArrayList<>();
        for (Object object : Sound.class.getEnumConstants()) {
            stringList.add(object.toString());
        }
        return stringList;
    }

    protected List<String> playerList() {
        List<String> stringList = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            stringList.add(player.getName());
        }
        return stringList;
    }

    protected List<String> targetBlock(Player player, String[] args) {
        return targetBlock(player, args, false);
    }

    protected List<String> targetBlock(Player player, String[] args, boolean twoCoord) {
        Block targ = player.getTargetBlock(null, 5);
        if(twoCoord) {
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

    protected List<String> mobList() {
        List<String> stringList = new ArrayList<>();
        for (Object object : MobType.class.getEnumConstants()) {
            stringList.add(object.toString());
        }
        return stringList;
    }

    protected List<String> barColorList() {
        List<String> stringList = new ArrayList<>();
        for (Object object : BarColor.class.getEnumConstants()) {
            stringList.add(object.toString());
        }
        return stringList;
    }

    protected List<String> barStyleList() {
        List<String> stringList = new ArrayList<>();
        for (Object object : BarStyle.class.getEnumConstants()) {
            stringList.add(object.toString());
        }
        return stringList;
    }
}
