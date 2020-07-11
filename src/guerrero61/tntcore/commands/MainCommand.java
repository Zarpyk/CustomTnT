package guerrero61.tntcore.commands;

import guerrero61.tntcore.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor {

    private final Main main;
    boolean isPlayer;
    Player player;

    public MainCommand(Main main) {
        this.main = main;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            isPlayer = true;
            player = (Player) sender;
        } else isPlayer = false;

        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "check":
                    new Check(isPlayer, sender);
                    return true;
                case "reload":
                    main.reloadConfig();
                    if (isPlayer) {
                        player.sendMessage(Main.FText(Main.getString("Messages.Reload")));
                    } else {
                        Bukkit.getConsoleSender().sendMessage(Main.FText(Main.getString("Messages.Reload")));
                    }
                    return true;
                default:
                    if (isPlayer) {
                        player.sendMessage(Main.FText(Main.getString("Messages.no-exist")));
                    } else {
                        Bukkit.getConsoleSender().sendMessage(Main.FText(Main.getString("Messages.no-exist")));
                    }
                    return true;
            }
        } else {
            String[] commands = new String[]{
                    Main.FTextNPrefix("&a--------&c&lTnTCore&a--------"),
                    Main.FTextNPrefix("&6/tnt check &7- Comprobación basica del funcionamiento."),
                    Main.FTextNPrefix("&6/tnt reload &7- Sirve para recargar la configuración."),
                    Main.FTextNPrefix("&a--------&c&l-------&a--------"),
            };
            if (isPlayer) {
                for (String s : commands) {
                    player.sendMessage(Main.FText(s));
                }
                return true;
            } else {
                Bukkit.getConsoleSender()
                        .sendMessage(Main.FText(Main.getString("Messages.no-exist")));
                return false;
            }
        }
    }
}
