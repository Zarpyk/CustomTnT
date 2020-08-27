package guerrero61.customtnt.commands;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mainutils.config.Config;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Check {

    public Check(boolean isPlayer, CommandSender sender) {
        if(isPlayer) {
            Player player = (Player) sender;
            String playerMsg = Config.getString(Config.Options.CheckPlayer);
            player.sendMessage(Formatter.FText(playerMsg));
        } else {
            String consoleMsg = Config.getString(Config.Options.CheckConsole);
            Main.consoleMsg(Formatter.FText(consoleMsg));
        }
    }
}
