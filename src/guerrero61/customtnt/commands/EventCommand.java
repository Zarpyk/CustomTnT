package guerrero61.customtnt.commands;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.events.EventClass.Event;
import guerrero61.customtnt.events.mobs.Mobs;
import guerrero61.customtnt.events.mobs.WitherSkeleton;
import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mainutils.config.Config;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class EventCommand {

    private final Main main;

    public EventCommand(Main main) {
        this.main = main;
    }

    public boolean onCommand(Player player, String[] args) {
        //tnt event <mob/block> <type> <color> <style> <multiply> <lootMultiply> <time> <eventName...>
        if(args.length >= 9) {
            switch (args[1].toLowerCase()) {
                case "mob": {
                    switch (args[2].toLowerCase()) {
                        case "wither_skeleton": {
                            BarColor barColor;
                            BarStyle barStyle;
                            Event event;
                            int timeInSeconds;
                            try {
                                barColor = BarColor.valueOf(args[3].toUpperCase());
                                barStyle = BarStyle.valueOf(args[4].toUpperCase());
                                event = new Event(Mobs.MobType.valueOf(args[2]).getEntityType(),
                                        Integer.parseInt(args[5]), Integer.parseInt(args[6]));
                                timeInSeconds = Integer.parseInt(args[7]);
                            } catch (Exception e) {
                                player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsError)));
                                return true;
                            }
                            List<String> stringList = new LinkedList<>(Arrays.asList(args));
                            stringList.remove(7);
                            stringList.remove(6);
                            stringList.remove(5);
                            stringList.remove(4);
                            stringList.remove(3);
                            stringList.remove(2);
                            stringList.remove(1);
                            stringList.remove(0);
                            String string = Formatter.FText(String.join(" ", stringList), true);
                            new WitherSkeleton(main).createEvent(string, barColor, barStyle, event, timeInSeconds);
                        }
                        case "ghast": {

                        }
                    }
                    break;
                }
                case "block": {
                    break;
                }
            }
        } else {
            player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsMiss)));
        }
        return true;
    }
}
