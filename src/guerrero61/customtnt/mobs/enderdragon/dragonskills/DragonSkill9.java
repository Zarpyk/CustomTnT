package guerrero61.customtnt.mobs.enderdragon.dragonskills;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mainutils.config.ConfigClass;
import guerrero61.customtnt.mobs.enderdragon.TnTDragon;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Objects;

public class DragonSkill9 extends ConfigClass {

    private final Main main;

    public static String skillName = "Lluvia de TnT";
    public static final int tntFuseTick = 100;
    public static final int timeBetweenTnT = 20;
    public static final int tntCount = 10;
    public static final float tntPower = 9.0f;

    public DragonSkill9(Main m) {
        main = m;
    }

    public void Skill9(EnderDragon enderDragon) {
        Main.debug("Skill 9");
        if (dataConfig == null) {
            protectedFileName = TnTDragon.fileName;
            dataConfig = CreateConfig(TnTDragon.fileName);
        }
        for (String key : Objects.requireNonNull(getConfigurationSection("participate")).getKeys(false)) {
            Main.debug("Skill9:" + key);
            Player player = Bukkit.getPlayer(key);
            if (player != null) {
                player.sendMessage(Formatter
                        .FText(TnTDragon.dragonName + " &6&lha usado la habilidad &c&l" + skillName));
            }
        }
        Location dragonLocation = enderDragon.getLocation();
        final double radius = 5;
        final double addAngle = 36;
        boolean noPlayerInRadius = true;
        for (Player target : Bukkit.getOnlinePlayers()) {
            if (target.getWorld().equals(enderDragon.getWorld())) {
                if (target.getLocation().distance(dragonLocation) < radius * 2) {
                    noPlayerInRadius = false;
                }
            }
        }
        boolean finalNoPlayerInRadius = noPlayerInRadius;
        BukkitTask task = new BukkitRunnable() {
            double angle = 0;
            double x = (dragonLocation.getX() + radius * Math.cos(Math.toRadians(angle)));
            double realY = -5;
            double y = dragonLocation.getY() + realY;
            double z = (dragonLocation.getZ() + radius * Math.sin(Math.toRadians(angle)));
            Location tntLocation = new Location(dragonLocation.getWorld(), dragonLocation.getX(), dragonLocation
                    .getY(), dragonLocation.getZ());

            public void run() {
                if (!enderDragon.isValid()) {
                    cancel();
                }

                tntLocation = tntLocation.set(x, y, z);
                Main.debug("TnT Location: " + tntLocation.getX() + " " + tntLocation.getY() + " " + tntLocation.getZ());
                Main.debug("Dragon Location: " + dragonLocation.getX() + " " + dragonLocation
                        .getY() + " " + dragonLocation.getZ());
                TNTPrimed tnt = enderDragon.getWorld().spawn(tntLocation, TNTPrimed.class);
                tnt.setIsIncendiary(true);
                tnt.setGlowing(true);
                tnt.setFuseTicks(tntFuseTick);
                tnt.setGravity(finalNoPlayerInRadius);
                Bukkit.getScheduler().runTaskLater(main, () -> tnt.getWorld()
                        .createExplosion(tnt.getLocation().getX(), tnt.getLocation().getY(), tnt.getLocation()
                                .getZ(), tntPower, true, true), tntFuseTick);
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
    }
}
