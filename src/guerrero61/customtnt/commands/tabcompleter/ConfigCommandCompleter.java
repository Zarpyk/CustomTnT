package guerrero61.customtnt.commands.tabcompleter;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mainutils.config.ConfigClass;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigCommandCompleter extends CommandCompleterMethods {

    public List<String> use(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            case 2: {
                File file = new File(String.valueOf(Main.getPlugin().getDataFolder()));
                List<String> stringList = new ArrayList<>();
                for (String fileName : file.list()) {
                    stringList.add(fileName.replace(".yml", ""));
                }
                return sortList(stringList, args);
            }
            case 3: {
                return sortList(new ArrayList<>(List.of("get", "set", "delete")), args);
            }
            case 4: {
                return sortList(new ArrayList<>(List.of("string", "int", "double", "bool")), args);
            }
            case 5: {
                ConfigClass configClass = new ConfigClass(args[1]);
                List<String> stringList = new ArrayList<>();
                for (String key : configClass.getConfigurationSection("").getKeys(true)) {
                    switch (args[3].toLowerCase()) {
                        case "string": {
                            if(!Main.isNumeric(configClass.getString(key)) &&
                               !Main.isBool(configClass.getString(key)) &&
                               !configClass.getString(key).contains("MemorySection[path='")) {
                                stringList.add(key);
                            }
                            break;
                        }
                        case "int": {
                            if(Main.isNumeric(configClass.getString(key)) &&
                               !configClass.getString(key).contains(".")) {
                                stringList.add(key);
                            }
                            break;
                        }
                        case "double": {
                            if(Main.isNumeric(configClass.getString(key)) && configClass.getString(key).contains(".")) {
                                stringList.add(key);
                            }
                            break;
                        }
                        case "bool": {
                            if(Main.isBool(configClass.getString(key))) {
                                stringList.add(key);
                            }
                            break;
                        }
                    }
                }
                if(stringList.size() == 0) {
                    return sortList(new ArrayList<>(List.of("NoHayNadaQueSea" + Formatter.Capitalize(args[3]))), args);
                } else {
                    return sortList(stringList, args);
                }
            }
            default: {
                return null;
            }
        }
    }
}
