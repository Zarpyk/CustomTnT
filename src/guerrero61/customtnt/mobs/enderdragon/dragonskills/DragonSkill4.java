package guerrero61.customtnt.mobs.enderdragon.dragonskills;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import guerrero61.customtnt.Main;
import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mobs.enderdragon.TnTDragon;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DragonSkill4 implements Listener {

    public static String skillName = "Mob Random";
    public static String[] mobNames = new String[]{"&6Esbirro del Dragon", "&6Soldado del Dragon",
            "&6Esclavo del Dragon", "&6Guerrero del Dragon", "&6Arma del Dragon", "&6Hijo del Dragon"};
    public static int minMob = 4;
    public static int maxMob = 10;

    private BukkitTask task;

    public void Skill4(Player player) {
        Main.debug("Skill 4");
        player.sendMessage(
                Formatter.FText(TnTDragon.dragonName + " &6&lha usado la habilidad &c&l" + skillName + " &6&len ti."));
        int random = Main.random(minMob, maxMob);
        for (int i = 0; i < random; i++) {
            try {
                int x = Main.random(0, 3);
                int y = Main.random(0, 3);
                int z = Main.random(0, 3);
                int witherChance = Main.random(0, 50);
                if(witherChance == 50) {
                    Wither entity = (Wither) player.getLocation().getWorld().spawnEntity(
                            player.getLocation().add(x, y, z), EntityType.WITHER);
                    entity.clearLootTable();
                    entity.setGlowing(true);
                    int randomName = Main.random(0, mobNames.length - 1);
                    entity.setCustomName(Formatter.FText(mobNames[randomName], true, player));
                    entity.setCustomNameVisible(true);
                    Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(50);
                    task = new BukkitRunnable() {
                        @Override
                        public void run() {
                            try {
                                entity.setTarget(player);
                                if(!entity.isValid()) {
                                    cancel();
                                }
                            } catch (Exception exception) {
                                cancel();
                            }
                        }
                    }.runTaskTimer(Main.getPlugin(), 0, 20L);
                } else {
                    Entity entity = player.getLocation().getWorld().spawnEntity(player.getLocation().add(x, y, z),
                            EntityType.valueOf(randomMob()));
                    entity.setGlowing(true);
                    int randomName = Main.random(0, mobNames.length - 1);
                    entity.setCustomName(Formatter.FText(mobNames[randomName], true, player));
                    entity.setCustomNameVisible(true);
                }
            } catch (Exception e) {
                Bukkit.broadcastMessage(Formatter.FText("&cHubo un error al invocar a: " + randomMob()));
            }
        }
    }

    private String randomMob() {
        List<String> mobList = new ArrayList<>(
                List.of("BLAZE", "CAVE_SPIDER", "CREEPER", "DROWNED", "ENDERMAN", "ENDERMITE", "EVOKER", "GHAST",
                        "HOGLIN", "HUSK", "MAGMA_CUBE", "PHANTOM", "PIGLIN", "PILLAGER", "RAVAGER", "SILVERFISH",
                        "SKELETON", "SLIME", "SPIDER", "STRAY", "VEX", "VINDICATOR", "WITCH", "WITHER_SKELETON",
                        "ZOMBIE", "ZOGLIN", "ZOMBIFIED_PIGLIN"));
        int random = Main.random(0, mobList.size() - 1);
        return mobList.get(random);
    }

    @EventHandler
    public void onPlayerDeath(PlayerPostRespawnEvent event) {
        Player player = event.getPlayer();
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 600, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 1200, 3));
        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 1200, 1));
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if(event.getEntityType().equals(EntityType.WITHER)) {
            for (String name : mobNames) {
                if(Main.contains(name, event.getEntity().getCustomName())) {
                    event.getDrops().clear();
                    if(task != null) {
                        task.cancel();
                    }
                    return;
                }
            }
        }
    }
}
