package guerrero61.customtnt.commands.tabcompleter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class EventCommandCompleter extends CommandCompleterMethods {

    public List<String> use(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            //tnt event <mob/block> <type> <color> <style> <multiply> <lootMultiply> <time> <eventName...>
            case 2: {
                return sortList(new ArrayList<>(List.of("mob", "block")), args);
            }
            case 3: {
                switch (args[1]) {
                    case "mob": {
                        return sortList(mobList(), args);
                    }
                    case "block": {
                        return null;
                    }
                }
            }
            case 4: {
                return sortList(barColorList(), args);
            }
            case 5: {
                return sortList(barStyleList(), args);
            }
            case 6: {
                switch (args[1]) {
                    case "mob": {
                        return sortList(new ArrayList<>(List.of("multiply", "0", "1", "2", "3", "4", "5")), args);
                    }
                    case "block": {
                        return null;
                    }
                }
            }
            case 7: {
                switch (args[1]) {
                    case "mob": {
                        return sortList(new ArrayList<>(List.of("lootMultiply", "0", "1", "2", "3", "4", "5")), args);
                    }
                    case "block": {
                        return null;
                    }
                }
            }
            case 8: {
                return sortList(new ArrayList<>(List
                        .of("time", "60", "120", "240", "480", "960", "1920", "3200", "6400", "19200", "38400", "76800")), args);
            }
            case 9: {
                return sortList(new ArrayList<>(List.of("eventName...")), args);
            }
            default: {
                return null;
            }
        }
    }
}
