package guerrero61.customtnt.commands;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mainutils.config.Config;
import guerrero61.customtnt.mobs.enderdragon.dragonskills.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class DragonSkills {

    private final Main main;
    boolean isPlayer;
    Player player;

    public DragonSkills(Main main) {
        this.main = main;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            isPlayer = true;
            player = (Player) sender;
        } else {
            isPlayer = false;
            Main.consoleMsg(Formatter.FText(Config.getString(Config.Options.ErrorsNoConsole)));
            return true;
        }
        Location playerLocation = player.getLocation();
        switch (args[1].toLowerCase()) {
            case "1":
                if (args.length != 5 && args.length != 8) {
                    player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsMiss)));
                    return true;
                }
                try {
                    Location startLocation1 = new Location(playerLocation.getWorld(), Double.parseDouble(args[2]),
                            Double.parseDouble(args[3]), Double.parseDouble(args[4]));
                    if (args.length == 5) {
                        new DragonSkill1(main).Skill1(startLocation1, player);
                    } else {
                        Location secondLocation1 = new Location(playerLocation.getWorld(), Double.parseDouble(args[5]),
                                Double.parseDouble(args[6]), Double.parseDouble(args[7]));
                        new DragonSkill1(main).Skill1(startLocation1, secondLocation1);
                    }
                } catch (Exception e) {
                    player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsError)));
                    return true;
                }
                return true;
            case "2":
                if (args.length == 2) {
                    new DragonSkill2(main).Skill2(player);
                } else if (args.length == 5) {
                    Location newLocation2;
                    try {
                        newLocation2 = new Location(playerLocation.getWorld(), Double.parseDouble(args[2]),
                                Double.parseDouble(args[3]), Double.parseDouble(args[4]));
                    } catch (Exception e) {
                        player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsError)));
                        return true;
                    }
                    new DragonSkill2(main).Skill2(newLocation2);
                } else {
                    player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsMiss)));
                }
                return true;
            case "3":
                if (args.length == 2) {
                    new DragonSkill3(main).Skill3(player);
                }
                return true;
            case "7":
                if (args.length == 2) {
                    new DragonSkill7(main).Skill7(player);
                } else {
                    player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsMiss)));
                }
                return true;
            case "9":
                EnderDragon enderDragon;
                if (args.length == 2) {
                    enderDragon = (EnderDragon) playerLocation.getWorld().spawnEntity(playerLocation,
                            EntityType.ENDER_DRAGON);
                    enderDragon.setAI(false);
                    new DragonSkill9(main).Skill9(enderDragon);
                } else if (args.length == 5) {
                    Location newLocation9;
                    try {
                        newLocation9 = new Location(playerLocation.getWorld(), Double.parseDouble(args[2]),
                                Double.parseDouble(args[3]), Double.parseDouble(args[4]));
                    } catch (Exception e) {
                        player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsError)));
                        return true;
                    }
                    enderDragon = (EnderDragon) newLocation9.getWorld()
                            .spawnEntity(newLocation9, EntityType.ENDER_DRAGON);
                    enderDragon.setAI(false);
                    new DragonSkill9(main).Skill9(enderDragon);
                } else {
                    player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsMiss)));
                    return true;
                }
                EnderDragon finalEnderDragon = enderDragon;
                Bukkit.getScheduler().runTaskLater(main, finalEnderDragon::remove,
                        (DragonSkill9.timeBetweenTnT * DragonSkill9.tntCount) + DragonSkill9.tntFuseTick);
                return true;
            default:
                player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsError)));
        }
        return true;
    }
}
