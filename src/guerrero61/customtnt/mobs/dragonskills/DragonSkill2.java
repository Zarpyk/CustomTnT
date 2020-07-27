package guerrero61.customtnt.mobs.dragonskills;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.TNTPrimed;

import guerrero61.customtnt.Main;

public class DragonSkill2 {

	private final Main main;

	public DragonSkill2(Main m) {
		main = m;
	}

	public void Skill2(Location playerLocation) {
		Main.debug("Skill 2");
		TNTPrimed tnt = playerLocation.getWorld().spawn(playerLocation.add(0, 2, 0), TNTPrimed.class);
		tnt.setIsIncendiary(true);
		tnt.setGlowing(true);
		tnt.setFuseTicks(22);
		Bukkit.getScheduler().runTaskLater(main, () -> tnt.getWorld().createExplosion(tnt.getLocation().getX(),
				tnt.getLocation().getY(), tnt.getLocation().getZ(), 7.0f, true, true), 22L);
	}

}
