package guerrero61.customtnt.mobs.enderdragon.dragonskills;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mobs.enderdragon.TnTDragon;
import org.bukkit.Bukkit;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DragonSkill6 implements Listener {

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
        TnTDragon.set("cancelEndermanSpawn", true);
        new BukkitRunnable() {
            @Override
            public void run() {
                List<Player> nearbyPlayers = new ArrayList<>();
                for (Entity entity2 : Bukkit.getOnlinePlayers()) {
                    if (entity2 instanceof Player) {
                        if (entity2.getWorld().equals(enderDragon.getWorld())) {
                            nearbyPlayers.add((Player) entity2);
                        }
                    }
                }
                for (Entity entity : enderDragon.getNearbyEntities(250, 100, 250)) {
                    try {
                        if (entity.getType().equals(EntityType.valueOf(entityType))) {
                            Player nearbyPlayer = Collections.min(nearbyPlayers, Comparator
                                    .comparing(s -> s.getLocation().distance(entity.getLocation())));
                            entity.teleport(nearbyPlayer.getLocation());
                            entity.getWorld().createExplosion(entity.getLocation(), 3.0f, false, true);
                            if (!entity.isDead()) {
                                entity.remove();
                            }
                        }
                    } catch (Exception e) {
                        Main.consoleMsg(Formatter.FText("&cHabilidad 6: EntityType error"));
                    }
                }
                TnTDragon.set("cancelEndermanSpawn", false);
            }
        }.

                runTaskLater(main, killEndermanTimer * 20);
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType().equals(EntityType.valueOf(entityType))) {
            if (TnTDragon.getBool("cancelEndermanSpawn")) {
                event.setCancelled(true);
            }
        }
    }
}
