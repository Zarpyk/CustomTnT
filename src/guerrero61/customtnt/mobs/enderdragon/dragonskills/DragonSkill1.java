package guerrero61.customtnt.mobs.enderdragon.dragonskills;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mobs.enderdragon.TnTDragon;
import guerrerocraft61.particleapi.particletypes.LineParticle;
import net.minecraft.world.damagesource.DamageSource;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
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
        player.sendMessage(
                Formatter.FText(TnTDragon.dragonName + " &6&lha usado la habilidad &c&l" + skillName + " &6&len ti."));
    }

    public void Skill1(Location particleLocation, Location location) {
        Main.debug("Skill 1");
        new LineParticle(particleLocation, location, Particle.DRAGON_BREATH, 100, 0.04, true);
        LineParticle particle = new LineParticle(particleLocation, location, Particle.DRAGON_BREATH, 100, 0.04, true);
        new BukkitRunnable() {
            public void run() {
                if(particle.bukkitTask.isCancelled()) {
                    Main.debug("Cancel");
                    Bukkit.getScheduler().runTaskLater(main, this::cancel, 40L);
                }
                for (Player target : Bukkit.getOnlinePlayers()) {
                    if(target.getWorld().equals(particle.a.getWorld())) {
                        if(target.getLocation().distance(particle.a) < 2) {
                            Main.debug("Damage");
                            if(target.getGameMode().equals(GameMode.SURVIVAL) ||
                               target.getGameMode().equals(GameMode.ADVENTURE)) {
                                Main.debug("gamemode");
                                ((CraftPlayer) target).getHandle().damageEntity(DamageSource.a, 15);
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(main, 0, 0);
    }
}
