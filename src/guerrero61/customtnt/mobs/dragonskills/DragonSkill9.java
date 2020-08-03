package guerrero61.customtnt.mobs.dragonskills;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import guerrero61.customtnt.Main;

public class DragonSkill9 {

	private final Main main;
	public static final int tntFuseTick = 100;
	public static final int timeBetweenTnT = 20;
	public static final int tntCount = 10;
	public static final float tntPower = 9.0f;

	public DragonSkill9(Main m) {
		main = m;
	}

	public void Skill9(EnderDragon enderDragon) {
		Main.debug("Skill 9");
		enderDragon.setInvulnerable(true);
		Location dragonLocation = enderDragon.getLocation();
		final double radius = 5;
		final double addAngle = 36;
		boolean noPlayerInRadius = true;
		for (Player target : Bukkit.getOnlinePlayers()) {
			Main.debug("Player distance: " + target.getLocation().distance(dragonLocation));
			if (target.getLocation().distance(dragonLocation) < radius * 2) {
				noPlayerInRadius = false;
			}
		}
		boolean finalNoPlayerInRadius = noPlayerInRadius;
		BukkitTask task = new BukkitRunnable() {
			double angle = 0;
			double x = (dragonLocation.getX() + radius * Math.cos(Math.toRadians(angle)));
			double realY = -5;
			double y = dragonLocation.getY() + realY;
			double z = (dragonLocation.getZ() + radius * Math.sin(Math.toRadians(angle)));
			Location tntLocation = new Location(dragonLocation.getWorld(), dragonLocation.getX(), dragonLocation.getY(),
					dragonLocation.getZ());

			public void run() {
				tntLocation = tntLocation.set(x, y, z);
				Main.debug("TnT Location: " + tntLocation.getX() + " " + tntLocation.getY() + " " + tntLocation.getZ());
				Main.debug("Dragon Location: " + dragonLocation.getX() + " " + dragonLocation.getY() + " "
						+ dragonLocation.getZ());
				TNTPrimed tnt = enderDragon.getWorld().spawn(tntLocation, TNTPrimed.class);
				tnt.setIsIncendiary(true);
				tnt.setGlowing(true);
				tnt.setFuseTicks(tntFuseTick);
				tnt.setGravity(finalNoPlayerInRadius);
				Bukkit.getScheduler()
						.runTaskLater(
								main, () -> tnt.getWorld().createExplosion(tnt.getLocation().getX(),
										tnt.getLocation().getY(), tnt.getLocation().getZ(), tntPower, true, true),
								tntFuseTick);
				angle += addAngle;
				if (angle >= 360) {
					angle -= 360;
				}
				x = (dragonLocation.getX() + radius * Math.cos(Math.toRadians(angle)));
				Main.debug("RealY value 1: " + realY);
				realY += 1;
				Main.debug("RealY value 2: " + realY);
				y = dragonLocation.getY() + realY;
				z = (dragonLocation.getZ() + radius * Math.sin(Math.toRadians(angle)));
				tntLocation = tntLocation.set(x, y, z);
			}
		}.runTaskTimer(main, 0, timeBetweenTnT);
		Bukkit.getScheduler().runTaskLater(main, task::cancel, timeBetweenTnT * tntCount);
		Bukkit.getScheduler().runTaskLater(main, () -> enderDragon.setInvulnerable(false),
				(timeBetweenTnT * tntCount) + tntFuseTick);
	}
}
