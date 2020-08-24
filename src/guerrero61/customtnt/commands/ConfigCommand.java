package guerrero61.customtnt.commands;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mainutils.config.Config;
import guerrero61.customtnt.mainutils.config.ConfigClass;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class ConfigCommand {

    private final Main main;

    public ConfigCommand(Main main) {
        this.main = main;
    }

    public boolean onCommand(Player player, String[] args) {
        //tnt config <config> <get/set/delete> <type> <key> <content>
        if (args.length >= 5) {
            ConfigClass configClass = new ConfigClass(args[1]);
            String key = args[4];
            switch (args[3].toLowerCase()) {
                case "string": {
                    switch (args[2]) {
                        case "get": {
                            player.sendMessage(Formatter.FText(configClass.getString(key)));
                            return true;
                        }
                        case "set": {
                            if (args.length < 6) {
                                player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsMiss)));
                                return true;
                            }
                            List<String> stringList = Arrays.asList(args);
                            stringList.remove(4);
                            stringList.remove(3);
                            stringList.remove(2);
                            stringList.remove(1);
                            stringList.remove(0);
                            String string = String.join(" ", stringList);
                            Main.debug(string);
                            configClass.set(key, string);
                            player.sendMessage(Formatter
                                    .FText(Config.getString(Config.Options.ConfigCommandSet).replace("%key%", key)
                                            .replace("%variable%", string)));
                            return true;
                        }
                        case "delete": {
                            configClass.set(key, (String) null);
                            player.sendMessage(Formatter
                                    .FText(Config.getString(Config.Options.ConfigCommandDelete).replace("%key%", key)));
                            return true;
                        }
                        default: {
                            player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsError)));
                            return true;
                        }
                    }
                }
                case "int": {
                    switch (args[2]) {
                        case "get": {
                            player.sendMessage(Formatter.FText(Integer.toString(configClass.getInt(key))));
                            return true;
                        }
                        case "set": {
                            if (args.length != 6) {
                                player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsMiss)));
                                return true;
                            }
                            try {
                                int i = Integer.parseInt(args[5]);
                                configClass.set(key, i);
                            } catch (Exception e) {
                                player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsError)));
                                return true;
                            }
                            player.sendMessage(Formatter
                                    .FText(Config.getString(Config.Options.ConfigCommandSet).replace("%key%", key)
                                            .replace("%variable%", args[5])));
                            return true;
                        }
                        case "delete": {
                            configClass.set(key, (Integer) null);
                            player.sendMessage(Formatter
                                    .FText(Config.getString(Config.Options.ConfigCommandDelete).replace("%key%", key)));
                            return true;
                        }
                        default: {
                            player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsError)));
                            return true;
                        }
                    }
                }
                case "double": {
                    switch (args[2]) {
                        case "get": {
                            player.sendMessage(Formatter.FText(Double.toString(configClass.getDouble(key))));
                            return true;
                        }
                        case "set": {
                            if (args.length != 6) {
                                player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsMiss)));
                                return true;
                            }
                            try {
                                double d = Double.parseDouble(args[5]);
                                configClass.set(key, d);
                            } catch (Exception e) {
                                player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsError)));
                                return true;
                            }
                            player.sendMessage(Formatter
                                    .FText(Config.getString(Config.Options.ConfigCommandSet).replace("%key%", key)
                                            .replace("%variable%", args[5])));
                            return true;
                        }
                        case "delete": {
                            configClass.set(key, (Double) null);
                            player.sendMessage(Formatter
                                    .FText(Config.getString(Config.Options.ConfigCommandDelete).replace("%key%", key)));
                            return true;
                        }
                        default: {
                            player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsError)));
                            return true;
                        }
                    }
                }
                case "bool": {
                    switch (args[2]) {
                        case "get": {
                            player.sendMessage(Formatter.FText(Boolean.toString(configClass.getBool(key))));
                            return true;
                        }
                        case "set": {
                            if (args.length != 6) {
                                player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsMiss)));
                                return true;
                            }
                            if (args[5].equalsIgnoreCase("true") || args[5].equalsIgnoreCase("false")) {
                                boolean b = Boolean.parseBoolean(args[5]);
                                configClass.set(key, b);
                            } else {
                                player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsError)));
                                return true;
                            }
                            player.sendMessage(Formatter
                                    .FText(Config.getString(Config.Options.ConfigCommandSet).replace("%key%", key)
                                            .replace("%variable%", args[5])));
                            return true;
                        }
                        case "delete": {
                            configClass.set(key, (Boolean) null);
                            player.sendMessage(Formatter
                                    .FText(Config.getString(Config.Options.ConfigCommandDelete).replace("%key%", key)));
                            return true;
                        }
                        default: {
                            player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsError)));
                            return true;
                        }
                    }
                }
            }
        } else {
            player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsMiss)));
        }
        return true;
    }
}
