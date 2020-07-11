package guerrero61.tntcore.commands;

import guerrero61.tntcore.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Check {

    public Check(boolean isPlayer, CommandSender sender) {
        if (isPlayer) {
            Player player = (Player) sender;
            String playerMsg = Main.getString("Messages.Check.player");
            player.sendMessage(Main.FText(playerMsg));
        } else {
            String consoleMsg = Main.getString("Messages.Check.console");
            Bukkit.getConsoleSender().sendMessage(Main.FText(consoleMsg));
        }
    }
}
