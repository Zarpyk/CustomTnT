package guerrero61.customtnt.mobs.enderdragon.dragonskills;

import guerrero61.customtnt.Main;
import guerrerocraft61.particleapi.particletypes.LineParticle;
import net.minecraft.server.v1_16_R1.DamageSource;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class DragonSkill1 {

    private final Main main;
    public static String skillName = "El aliento del Dragon";

    public DragonSkill1(Main m) {
        main = m;
    }

    public void Skill1(Location particleLocation, Player player) {
        Skill1(particleLocation, player.getLocation());
    }

    public void Skill1(Location particleLocation, Location location) {
        Main.debug("Skill 1");
        new LineParticle(particleLocation, location, Particle.DRAGON_BREATH, 100, 0.04, true);
        LineParticle particle = new LineParticle(particleLocation, location, Particle.DRAGON_BREATH, 100, 0.04, true);
        new BukkitRunnable() {
            public void run() {
                if (particle.bukkitTask.isCancelled()) {
                    Main.debug("Cancel");
                    Bukkit.getScheduler().runTaskLater(main, this::cancel, 40L);
                }
                for (Player target : Bukkit.getOnlinePlayers()) {
                    if (target.getLocation().distance(particle.a) < 2) {
                        Main.debug("Damage");
                        ((CraftPlayer) target).getHandle().damageEntity(DamageSource.MAGIC, 15);
                    }
                }
            }
        }.runTaskTimer(main, 0, 0);
    }
}
