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

import java.util.Objects;

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
            case "1": //tnt skills 1 <x1> <y2> <z3> <<x2>> <<y2>> <<z2>>
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
            case "2": //tnt skills 2 <player/x> <y> <z>
                if (args.length == 2) {
                    new DragonSkill2(main).Skill2(player);
                } else if (args.length == 3) {
                    try {
                        new DragonSkill2(main).Skill2(Objects.requireNonNull(Bukkit.getPlayer(args[2])));
                    } catch (Exception e) {
                        player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsError)));
                        return true;
                    }
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
            case "3": //tnt skills 3 <player>
                if (args.length == 2) {
                    new DragonSkill3(main).Skill3(player);
                } else if (args.length == 3) {
                    try {
                        new DragonSkill3(main).Skill3(Objects.requireNonNull(Bukkit.getPlayer(args[2])));
                    } catch (Exception e) {
                        player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsError)));
                        return true;
                    }
                } else {
                    player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsMiss)));
                }
                return true;
            case "4": //tnt skills 4 <player>
                if (args.length == 2) {
                    new DragonSkill4(main).Skill4(player);
                } else if (args.length == 3) {
                    try {
                        new DragonSkill4(main).Skill4(Objects.requireNonNull(Bukkit.getPlayer(args[2])));
                    } catch (Exception e) {
                        player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsError)));
                        return true;
                    }
                } else {
                    player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsMiss)));
                }
                return true;
            case "5": //tnt skills 5 <player>
                if (args.length == 2) {
                    new DragonSkill5(main).Skill5(player);
                } else if (args.length == 3) {
                    try {
                        new DragonSkill5(main).Skill5(Objects.requireNonNull(Bukkit.getPlayer(args[2])));
                    } catch (Exception e) {
                        player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsError)));
                        return true;
                    }
                } else {
                    player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsMiss)));
                }
                return true;
            case "6":
                EnderDragon enderDragon6;
                if (args.length == 2) {
                    enderDragon6 = (EnderDragon) playerLocation.getWorld().spawnEntity(playerLocation,
                            EntityType.ENDER_DRAGON);
                    enderDragon6.setAI(false);
                    new DragonSkill6(main).Skill6(enderDragon6);
                } else if (args.length == 3) {
                    try {
                        enderDragon6 = (EnderDragon) Objects.requireNonNull(Bukkit.getPlayer(args[2])).getLocation()
                                .getWorld().spawnEntity(playerLocation,
                                        EntityType.ENDER_DRAGON);
                        enderDragon6.setAI(false);
                        new DragonSkill6(main).Skill6(enderDragon6);
                    } catch (Exception e) {
                        player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsError)));
                        return true;
                    }
                } else if (args.length == 5) {
                    Location newLocation6;
                    try {
                        newLocation6 = new Location(playerLocation.getWorld(), Double.parseDouble(args[2]),
                                Double.parseDouble(args[3]), Double.parseDouble(args[4]));
                    } catch (Exception e) {
                        player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsError)));
                        return true;
                    }
                    enderDragon6 = (EnderDragon) newLocation6.getWorld()
                            .spawnEntity(newLocation6, EntityType.ENDER_DRAGON);
                    enderDragon6.setAI(false);
                    new DragonSkill6(main).Skill6(enderDragon6);
                }
                return true;
            case "7": //tnt skills 7 <player>
                if (args.length == 2) {
                    new DragonSkill7(main).Skill7(player);
                } else if (args.length == 3) {
                    try {
                        new DragonSkill7(main).Skill7(Objects.requireNonNull(Bukkit.getPlayer(args[2])));
                    } catch (Exception e) {
                        player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsError)));
                        return true;
                    }
                } else {
                    player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsMiss)));
                }
                return true;
            case "8": //tnt skills 8 <player>
                if (args.length == 2) {
                    new DragonSkill8(main).Skill8(player);
                } else if (args.length == 3) {
                    try {
                        new DragonSkill8(main).Skill8(Objects.requireNonNull(Bukkit.getPlayer(args[2])));
                    } catch (Exception e) {
                        player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsError)));
                        return true;
                    }
                } else {
                    player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsMiss)));
                }
                return true;
            case "9": //tnt skills 9 <player/x> <y> <z>
                EnderDragon enderDragon9;
                if (args.length == 2) {
                    enderDragon9 = (EnderDragon) playerLocation.getWorld().spawnEntity(playerLocation,
                            EntityType.ENDER_DRAGON);
                    enderDragon9.setAI(false);
                    new DragonSkill9(main).Skill9(enderDragon9);
                } else if (args.length == 3) {
                    try {
                        enderDragon9 = (EnderDragon) Objects.requireNonNull(Bukkit.getPlayer(args[2]))
                                .getLocation()
                                .getWorld().spawnEntity(playerLocation,
                                        EntityType.ENDER_DRAGON);
                        enderDragon9.setAI(false);
                        new DragonSkill9(main).Skill9(enderDragon9);
                    } catch (Exception e) {
                        player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsError)));
                        return true;
                    }
                } else if (args.length == 5) {
                    Location newLocation9;
                    try {
                        newLocation9 = new Location(playerLocation.getWorld(), Double.parseDouble(args[2]),
                                Double.parseDouble(args[3]), Double.parseDouble(args[4]));
                    } catch (Exception e) {
                        player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsError)));
                        return true;
                    }
                    enderDragon9 = (EnderDragon) newLocation9.getWorld()
                            .spawnEntity(newLocation9, EntityType.ENDER_DRAGON);
                    enderDragon9.setAI(false);
                    new DragonSkill9(main).Skill9(enderDragon9);
                } else {
                    player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsMiss)));
                    return true;
                }
                EnderDragon finalEnderDragon9 = enderDragon9;
                Bukkit.getScheduler().runTaskLater(main, finalEnderDragon9::remove,
                        (DragonSkill9.timeBetweenTnT * DragonSkill9.tntCount) + DragonSkill9.tntFuseTick);
                return true;
            case "10": //tnt skills 10 <player/x> <y> <z>
                if (args.length == 2) {
                    new DragonSkill10(main).Skill10(playerLocation, player);
                } else if (args.length == 3) {
                    try {
                        new DragonSkill10(main)
                                .Skill10(Objects.requireNonNull(Bukkit.getPlayer(args[2]))
                                        .getLocation(), Objects
                                        .requireNonNull(Bukkit.getPlayer(args[2])));
                    } catch (Exception e) {
                        player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsError)));
                        return true;
                    }
                } else if (args.length == 5) {
                    Location newLocation10;
                    try {
                        newLocation10 = new Location(playerLocation.getWorld(), Double.parseDouble(args[2]),
                                Double.parseDouble(args[3]), Double.parseDouble(args[4]));
                    } catch (Exception e) {
                        player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsError)));
                        return true;
                    }
                    new DragonSkill10(main).Skill10(newLocation10, player);
                }
                return true;
            default:
                player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsError)));
        }
        return true;
    }
}
