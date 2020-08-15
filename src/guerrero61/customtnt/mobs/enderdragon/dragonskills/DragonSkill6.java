package guerrero61.customtnt.mobs.enderdragon.dragonskills;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mainutils.config.ConfigBuilder;
import guerrero61.customtnt.mobs.enderdragon.TnTDragon;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class DragonSkill6 extends ConfigBuilder implements Listener {

    private final Main main;

    public static String skillName = "Sacrificio de Endermans";
    public static int killEndermanTimer = 120;
    public static String entityType = "ENDERMAN";

    public DragonSkill6(Main m) {
        super(TnTDragon.fileName);
        main = m;
    }

    public void Skill6(EnderDragon enderDragon) {
        Main.debug("Skill 6");
        set("skill6Active", true);

        for (String key : Objects.requireNonNull(getConfigurationSection("participate"))
                .getKeys(false)) {
            Player player = Bukkit.getPlayer(key);
            assert player != null;
            player.sendMessage(Formatter
                    .FText(TnTDragon.dragonName + " &4&lha usado la habilidad &2&l" + skillName));
            player.sendMessage(Formatter
                    .FText("&4&lTeneis que matar a todos los endermans en menos de &2&l" + killEndermanTimer + "&4&ls o estos crearan una MEGA-Explosion"));
        }

        set("cancelEndermanSpawn", true);
        BukkitTask bukkitTask = new BukkitRunnable() {
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
                final BossBar bossBar = Bukkit.getBossBar(new NamespacedKey(main, "EndermanBossBar"));
                for (String key : Objects
                        .requireNonNull(getConfigurationSection("participate"))
                        .getKeys(false)) {
                    Player player = Bukkit.getPlayer(key);
                    assert player != null;
                    assert bossBar != null;
                    bossBar.removePlayer(player);
                }
                Bukkit.removeBossBar(new NamespacedKey(main, "EndermanBossBar"));
                set("cancelEndermanSpawn", false);
            }
        }.runTaskLater(main, killEndermanTimer * 20);

        BossBar bossBar = Bukkit.createBossBar(new NamespacedKey(main, "EndermanBossBar"), Formatter
                .FText("&c&lSacrificio de enderman", true), BarColor.RED, BarStyle.SEGMENTED_12, BarFlag.CREATE_FOG);
        for (String key : Objects.requireNonNull(getConfigurationSection("participate"))
                .getKeys(false)) {
            Player player = Bukkit.getPlayer(key);
            assert player != null;
            bossBar.addPlayer(player);
        }

        new BukkitRunnable() {
            double timer;

            @Override
            public void run() {
                if (!getBool("skill6Active")) {
                    Bukkit.removeBossBar(new NamespacedKey(main, "EndermanBossBar"));
                    bukkitTask.cancel();
                    cancel();
                }
                if (timer >= killEndermanTimer) {
                    cancel();
                }
                timer++;

                Objects.requireNonNull(Bukkit.getBossBar(new NamespacedKey(main, "EndermanBossBar")))
                        .setProgress((killEndermanTimer - timer) / killEndermanTimer);
            }
        }.runTaskTimer(main, 0, 20);
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType().equals(EntityType.valueOf(entityType))) {
            if (getBool("cancelEndermanSpawn")) {
                event.setCancelled(true);
            }
        }
    }
}
