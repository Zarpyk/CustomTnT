package guerrero61.customtnt.mobs.enderdragon.dragonskills;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mobs.enderdragon.TnTDragon;
import org.bukkit.Bukkit;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

public class DragonSkill6 {

    private final Main main;
    public static String skillName = "Sacrificio de Endermans";
    public static int killEndermanTimer = 10;
    public static String entityType = "ENDERMAN";

    public DragonSkill6(Main m) {
        main = m;
    }

    public void Skill6(EnderDragon enderDragon) {
        Main.debug("Skill 6");
        Bukkit.broadcastMessage(Formatter
                .FText(TnTDragon.dragonName + " &4&lha usado la habilidad &2&l" + skillName));
        Bukkit.broadcastMessage(Formatter
                .FText("&4&lTeneis que matar a todos los endermans en menos de &2&l" + killEndermanTimer + "&4&ls o estos crearan una MEGA-Explosion"));
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Entity entity : enderDragon.getNearbyEntities(500, 100, 500)) {
                    try {
                        if (entity.getType().equals(EntityType.valueOf(entityType))) {
                            entity.getWorld().createExplosion(entity.getLocation(), 30.0f, false, true);
                            if (!entity.isDead()) {
                                entity.remove();
                            }
                        }
                    } catch (Exception e) {
                        Main.consoleMsg(Formatter.FText("&cHabilidad 6: EntityType error"));
                    }
                }
            }
        }.runTaskLater(main, killEndermanTimer * 20);
    }
}
