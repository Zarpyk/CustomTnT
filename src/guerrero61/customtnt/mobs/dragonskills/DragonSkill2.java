package guerrero61.customtnt.mobs.dragonskills;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;

import guerrero61.customtnt.Main;

public class DragonSkill2 {

	private final Main main;
	public static int tntFuseTick = 22;

	public DragonSkill2(Main m) {
		main = m;
	}

	public void Skill2(Player player) {
		Skill2(player.getLocation());
	}

	public void Skill2(Location location) {
		Main.debug("Skill 2");
		TNTPrimed tnt = location.getWorld().spawn(location.add(0, 2, 0), TNTPrimed.class);
		tnt.setIsIncendiary(true);
		tnt.setGlowing(true);
		tnt.setFuseTicks(tntFuseTick);
		Bukkit.getScheduler().runTaskLater(main, () -> tnt.getWorld().createExplosion(tnt.getLocation().getX(),
				tnt.getLocation().getY(), tnt.getLocation().getZ(), 7.0f, true, true), tntFuseTick);
	}

}
