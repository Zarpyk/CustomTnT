package guerrero61.customtnt.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mainutils.config.Config;
import guerrero61.customtnt.mobs.dragonskills.DragonSkill1;
import guerrero61.customtnt.mobs.dragonskills.DragonSkill2;
import guerrero61.customtnt.mobs.dragonskills.DragonSkill3;

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
			Location startLocation1 = new Location(playerLocation.getWorld(), Double.parseDouble(args[2]),
					Double.parseDouble(args[3]), Double.parseDouble(args[4]));
			if (args.length == 5) {
				new DragonSkill1(main).Skill1(startLocation1, playerLocation);
			} else if (args.length == 8) {
				Location secondLocation1 = new Location(playerLocation.getWorld(), Double.parseDouble(args[5]),
						Double.parseDouble(args[6]), Double.parseDouble(args[7]));
				new DragonSkill1(main).Skill1(startLocation1, secondLocation1);
			} else {
				player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsMiss)));
			}
			return true;
		case "2":
			if (args.length == 2) {
				new DragonSkill2(main).Skill2(playerLocation);
			} else if (args.length == 5) {
				Location newLocation2 = new Location(playerLocation.getWorld(), Double.parseDouble(args[2]),
						Double.parseDouble(args[3]), Double.parseDouble(args[4]));
				new DragonSkill2(main).Skill2(newLocation2);
			} else {
				player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsMiss)));
			}
			return true;
		case "3":
			EnderDragon enderDragon;
			if (args.length == 2) {
				enderDragon = (EnderDragon) playerLocation.getWorld().spawnEntity(playerLocation,
						EntityType.ENDER_DRAGON);
				enderDragon.setAI(false);
				new DragonSkill3(main).Skill3(enderDragon);
			} else if (args.length == 5) {
				Location newLocation3 = new Location(playerLocation.getWorld(), Double.parseDouble(args[2]),
						Double.parseDouble(args[3]), Double.parseDouble(args[4]));
				enderDragon = (EnderDragon) newLocation3.getWorld().spawnEntity(newLocation3, EntityType.ENDER_DRAGON);
				enderDragon.setAI(false);
				new DragonSkill3(main).Skill3(enderDragon);
			} else {
				player.sendMessage(Formatter.FText(Config.getString(Config.Options.ErrorsArgsMiss)));
				return true;
			}
			EnderDragon finalEnderDragon = enderDragon;
			Bukkit.getScheduler().runTaskLater(main, finalEnderDragon::remove,
					(DragonSkill3.timeBetweenTnT * DragonSkill3.tntCount) + DragonSkill3.tntFuseTick);
			return true;
		}
		return true;
	}
}
