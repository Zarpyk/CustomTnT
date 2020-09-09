package guerrero61.customtnt.mobs.enderdragon.dragonskills;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mainutils.config.ConfigClass;
import guerrero61.customtnt.mobs.enderdragon.TnTDragon;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
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

public class DragonSkill8 extends ConfigClass implements Listener {

    private final Main main;

    public static String skillName = "Sacrificio de Endermans";
    public static int killEndermanTimer = 120;
    public static String entityType = "ENDERMAN";

    public DragonSkill8(Main m) {
        main = m;
    }

    public void Skill8(EnderDragon enderDragon) {
        Main.debug("Skill 8");
        if(dataConfig == null) {
            protectedFileName = TnTDragon.fileName;
            dataConfig = CreateConfig(TnTDragon.fileName);
        }

        if(getBool("skill8Active")) {
            Bukkit.broadcastMessage(
                    Formatter.FText("&c&lERROR: SACRIFICIO DE ENDERMANS YA ESTA ACTIVO PERO SE REPITIO(¿?)"));
            Bukkit.broadcastMessage(
                    Formatter.FText("&c&lERROR: SACRIFICIO DE ENDERMANS YA ESTA ACTIVO PERO SE REPITIO(¿?)"));
            Bukkit.broadcastMessage(
                    Formatter.FText("&c&lERROR: SACRIFICIO DE ENDERMANS YA ESTA ACTIVO PERO SE REPITIO(¿?)"));
            return;
        }
        set("skill8Active", true);

        for (String key : Objects.requireNonNull(getConfigurationSection("participate")).getKeys(false)) {
            Player player = Bukkit.getPlayer(key);
            assert player != null;
            player.sendMessage(Formatter.FText(TnTDragon.dragonName + " &4&lha usado la habilidad &2&l" + skillName));
            player.sendMessage(Formatter.FText(
                    "&4&lTeneis que matar a todos los endermans en menos de &2&l" + killEndermanTimer +
                    "&4&ls o estos crearan una MEGA-Explosion"));
        }

        set("cancelEndermanSpawn", true);
        BukkitTask bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                List<Player> nearbyPlayers = new ArrayList<>();
                for (Entity entity2 : Bukkit.getOnlinePlayers()) {
                    if(entity2 instanceof Player) {
                        if(entity2.getWorld().equals(enderDragon.getWorld())) {
                            nearbyPlayers.add((Player) entity2);
                        }
                    }
                }
                if(nearbyPlayers.size() > 0) {
                    for (Entity entity : enderDragon.getNearbyEntities(250, 100, 250)) {
                        try {
                            if(entity.getType().equals(EntityType.valueOf(entityType))) {
                                Player nearbyPlayer = Collections.min(nearbyPlayers,
                                        Comparator.comparing(s -> s.getLocation().distance(entity.getLocation())));
                                entity.teleport(nearbyPlayer.getLocation());
                                entity.getWorld().createExplosion(entity.getLocation(), 3.0f, false, true);
                                if(!entity.isDead()) {
                                    entity.remove();
                                }
                            }
                        } catch (Exception e) {
                            Main.consoleMsg(Formatter.FText("&cHabilidad 8: EntityType error"));
                        }
                    }
                }
                final BossBar bossBar = Bukkit.getBossBar(new NamespacedKey(main, "EndermanBossBar"));
                assert bossBar != null;
                removeBossBar(bossBar);
            }
        }.runTaskLater(main, killEndermanTimer * 20);

        BossBar bossBar = Bukkit.createBossBar(new NamespacedKey(main, "EndermanBossBar"),
                Formatter.FText("&c&lSacrificio de enderman", true), BarColor.RED, BarStyle.SEGMENTED_12);

        for (String key : Objects.requireNonNull(getConfigurationSection("participate")).getKeys(false)) {
            Player player = Bukkit.getPlayer(key);
            if(player != null) {
                bossBar.addPlayer(player);
            }
        }

        new BukkitRunnable() {
            double timer;

            @Override
            public void run() {
                if(!getBool("skill8Active") || !enderDragon.isValid()) {
                    removeBossBar(bossBar);
                    bukkitTask.cancel();
                    cancel();
                }
                for (String key : Objects.requireNonNull(getConfigurationSection("participate")).getKeys(false)) {
                    Player player = Bukkit.getPlayer(key);
                    if(player != null) {
                        bossBar.setColor(BarColor.RED);
                        bossBar.setStyle(BarStyle.SEGMENTED_12);
                        bossBar.addPlayer(player);
                    }
                }
                if(timer >= killEndermanTimer) {
                    removeBossBar(bossBar);
                    cancel();
                }
                timer++;
                if(Bukkit.getBossBar(new NamespacedKey(main, "EndermanBossBar")) != null) {
                    Objects.requireNonNull(Bukkit.getBossBar(new NamespacedKey(main, "EndermanBossBar"))).setProgress(
                            (killEndermanTimer - timer) / killEndermanTimer);
                }
                if(Bukkit.getBossBar(new NamespacedKey(main, "EndermanBossBar")) == null || !enderDragon.isValid()) {
                    removeBossBar(bossBar);
                    bukkitTask.cancel();
                    cancel();
                }
            }
        }.runTaskTimer(main, 0, 20);
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        if(event.getEntityType().equals(EntityType.valueOf(entityType))) {
            if(dataConfig != null && getBool("cancelEndermanSpawn") != null) {
                if(getBool("cancelEndermanSpawn")) {
                    event.setCancelled(true);
                }
            }
        }
    }

    private void removeBossBar(BossBar bossBar) {
        if(bossBar != null) {
            for (String key : Objects.requireNonNull(getConfigurationSection("participate")).getKeys(false)) {
                Player player = Bukkit.getPlayer(key);
                if(player != null) {
                    bossBar.removePlayer(player);
                }
            }
            bossBar.setVisible(false);
            Bukkit.removeBossBar(new NamespacedKey(main, "EndermanBossBar"));
            Main.debug("skill8 false 2");
        }
        set("skill8Active", false);
        set("cancelEndermanSpawn", false);
    }
}
