package guerrero61.customtnt.mobs.enderdragon;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mobs.enderdragon.dragonskills.*;
import guerrerocraft61.particleapi.VectorMath;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class TnTDragon implements Listener {

    private final Main main;
    public static File dragonDataFile;
    public static FileConfiguration dragonDataConfig;

    public static String dragonName = "&cTnT Dragon";
    private static final int maxHealth = 5000;
    private static final boolean playerScaling = false;
    private static final int perPlayerHealth = 300;

    private int habilityCooldown = 15;
    private final int getAutoHabilityCooldown = 30;
    private int autoHabilityCooldown;
    private int sethabilityCooldown;
    private boolean canUseSkill;
    private boolean schedulerActivated = false;
    private boolean autoHabilitySchedulerActivated = false;

    private DragonPhase dragonPhase = DragonPhase.INITIAL;

    public TnTDragon(Main m) {
        main = m;
        sethabilityCooldown = habilityCooldown;
        autoHabilityCooldown = getAutoHabilityCooldown;
        canUseSkill = false;
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        DragonData();
        Entity entity = event.getEntity();
        if (!entity.getType().equals(EntityType.ENDER_DRAGON)) {
            return;
        }
        EnderDragon enderDragon = (EnderDragon) entity;
        enderDragon.setCustomName(Formatter.FText(dragonName, true));
        enderDragon.setCustomNameVisible(true);
        enderDragon.setGlowing(true);

        if (!playerScaling) {
            Objects.requireNonNull(enderDragon.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(maxHealth);
            enderDragon.setHealth(maxHealth);
        } else {
            int finalHealth = 0;
            for (Player player : Bukkit.getOnlinePlayers()) {
                finalHealth += perPlayerHealth;
                set("participate." + player.getName(), true);
            }
            Objects.requireNonNull(enderDragon.getAttribute(Attribute.GENERIC_MAX_HEALTH))
                    .setBaseValue(finalHealth);
            enderDragon.setHealth(finalHealth);
        }

        enderDragon.setMaximumNoDamageTicks(0);
        enderDragon.setNoDamageTicks(0);
        enderDragon.clearLootTable();
        if (!schedulerActivated) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Main.debug(Integer.toString(sethabilityCooldown));
                    if (sethabilityCooldown > 0 && !canUseSkill) {
                        sethabilityCooldown--;
                    } else if (sethabilityCooldown == 0) {
                        canUseSkill = true;
                    }

                    if (enderDragon.getHealth() <= 0) {
                        cancel();
                    }
                }
            }.runTaskTimer(main, 0, 20L);
            schedulerActivated = true;
        }
        if (!autoHabilitySchedulerActivated) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Main.debug(Integer.toString(autoHabilityCooldown));
                    if (autoHabilityCooldown > 0) {
                        autoHabilityCooldown--;
                    } else {
                        List<Player> playerList = new ArrayList<>();
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (player.getLocation().getWorld().equals(enderDragon.getWorld())) {
                                if (player.getLocation().distance(enderDragon.getLocation()) < 100) {
                                    playerList.add(player);
                                }
                            }
                        }
                        Player selectedTarget = playerList.get(Main.random(0, playerList.size() - 1));
                        Arrow arrow = (Arrow) selectedTarget.getWorld()
                                .spawnEntity(new Location(selectedTarget.getWorld(), 0, 500, 0), EntityType.ARROW);
                        arrow.setShooter(selectedTarget);
                        arrow.setSilent(true);
                        onDamage(new EntityDamageByEntityEvent(arrow, enderDragon, EntityDamageEvent.DamageCause.CUSTOM, 0));
                        arrow.remove();
                        autoHabilityCooldown = getAutoHabilityCooldown;
                    }
                    if (enderDragon.getHealth() <= 0) {
                        cancel();
                    }
                }

            }.runTaskTimer(main, 0, 20L);
            autoHabilitySchedulerActivated = true;
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if (!entity.getType().equals(EntityType.ENDER_DRAGON)) {
            return;
        }

        if (!schedulerActivated) {
            new BukkitRunnable() {
                public void run() {
                    Main.debug(Integer.toString(sethabilityCooldown));
                    if (sethabilityCooldown > 0 && !canUseSkill) {
                        sethabilityCooldown--;
                    } else if (sethabilityCooldown == 0) {
                        canUseSkill = true;
                    }

                    if (((EnderDragon) entity).getHealth() <= 0) {
                        cancel();
                    }
                }
            }.runTaskTimer(main, 0, 20L);
            schedulerActivated = true;
        }
        Main.debug("EntityDamageByEntityEvent can use skill");
        Entity damager = event.getDamager();
        if (!damager.getType().equals(EntityType.PLAYER) && !damager.getType().equals(EntityType.ARROW)
                && !damager.getType().equals(EntityType.SPECTRAL_ARROW)
                && !damager.getType().equals(EntityType.TRIDENT))
            return;
        Player player;
        if (damager.getType().equals(EntityType.ARROW)) {
            player = (Player) ((Arrow) damager).getShooter();
        } else if (damager.getType().equals(EntityType.SPECTRAL_ARROW)) {
            player = (Player) ((SpectralArrow) damager).getShooter();
        } else if (damager.getType().equals(EntityType.TRIDENT)) {
            player = (Player) ((Trident) damager).getShooter();
        } else {
            player = (Player) damager;
        }
        assert player != null;

        EnderDragon enderDragon = (EnderDragon) entity;

        if (!damager.getType().equals(EntityType.ARROW) || (damager.getType()
                .equals(EntityType.ARROW) && !damager.isSilent())) {
            Bukkit.broadcastMessage(Formatter
                    .FText("&e&lVida del dragon: &c&l" + Main.round(enderDragon.getHealth(), 2) + "/" + Objects
                            .requireNonNull(enderDragon
                                    .getAttribute(Attribute.GENERIC_MAX_HEALTH))
                            .getValue())); //TODO AutoSkill don't trigger this
            set("damage." + player.getName(), Main
                    .round(getDouble("damage." + player.getName()) + event.getDamage(), 2));
        }
        if (enderDragon.getHealth() > DragonPhase.INITIAL.getHealth()) {
            dragonPhase = DragonPhase.INITIAL;
        } else if (enderDragon.getHealth() < DragonPhase.INITIAL.getHealth()
                && enderDragon.getHealth() > DragonPhase.MID.getHealth()) {
            dragonPhase = DragonPhase.MID;
        } else if (enderDragon.getHealth() < DragonPhase.MID.getHealth()
                && enderDragon.getHealth() > DragonPhase.END.getHealth()) {
            dragonPhase = DragonPhase.END;
        } else if (enderDragon.getHealth() < DragonPhase.FINAL.getHealth()) {
            dragonPhase = DragonPhase.FINAL;
        }
        enderDragon
                .setCustomName(Formatter.FText(dragonName + " - Fase " + dragonPhase.getPhaseName(), true));

        int skillNumber = 0;
        if (dragonPhase == DragonPhase.INITIAL) {
            skillNumber = Main.random(1, 4);
            habilityCooldown = DragonPhase.INITIAL.getSkillCooldown();
        } else if (dragonPhase == DragonPhase.MID) {
            skillNumber = Main.random(1, 7);
            habilityCooldown = DragonPhase.MID.getSkillCooldown();
        } else if (dragonPhase == DragonPhase.END) {
            skillNumber = Main.random(1, 10);
            habilityCooldown = DragonPhase.END.getSkillCooldown();
        } else if (dragonPhase == DragonPhase.FINAL) {
            skillNumber = Main.random(1, 10);
            habilityCooldown = DragonPhase.FINAL.getSkillCooldown();
        }

        Main.debug("Skill number: " + skillNumber);

        switch (skillNumber) {
            case 1:
                if (!damager.getType().equals(EntityType.PLAYER)) {
                    resetHability();
                    Location dragonLocation = enderDragon.getLocation();
                    Location playerLocation = player.getLocation();
                    Vector direction = VectorMath.directionVector(playerLocation, dragonLocation);
                    enderDragon.teleport(enderDragon.getLocation().setDirection(direction));
                    dragonLocation = enderDragon.getLocation();
                    Location finalDragonLocation = dragonLocation;
                    new DragonSkill1(main).Skill1(finalDragonLocation, player);
                }
                return;
            case 2:
                if (canUseSkill) {
                    resetHability();
                    new DragonSkill2(main).Skill2(player);
                }
                return;
            case 3:
                if (canUseSkill) {
                    resetHability();
                    new DragonSkill3(main).Skill3(player);
                }
                return;
            case 4:
                if (canUseSkill) {
                    resetHability();
                    new DragonSkill4(main).Skill4(player);
                }
                return;
            case 5:
                if (canUseSkill) {
                    resetHability();
                    new DragonSkill5(main).Skill5(player);
                }
                return;
            case 6:
                if (canUseSkill) {
                    resetHability();
                    new DragonSkill6(main).Skill6(enderDragon);
                }
                return;
            case 7:
                if (canUseSkill) {
                    resetHability();
                    new DragonSkill7(main).Skill7(player);
                }
                return;
            case 8:
                if (canUseSkill) {
                    resetHability();
                    new DragonSkill8(main).Skill8(player, enderDragon);
                }
                return;
            case 9:
                if (canUseSkill) {
                    resetHability();
                    new DragonSkill9(main).Skill9(enderDragon);
                }
                return;
            case 10:
                if (canUseSkill) {
                    resetHability();
                    new DragonSkill10(main).Skill10(enderDragon, player);
                }
                return;
            default:
                Main.consoleMsg(Formatter.FText("&cError al intentar usar habilidades del Dragon de TnT"));
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if (e.getEntity().getType() == EntityType.ENDER_DRAGON
                && Objects.equals(e.getEntity().getCustomName(), Formatter.FText(TnTDragon.dragonName, true))) {
            e.getDrops().clear();
            SortedSet<PlayerData> topList =
                    new TreeSet<>(Comparator.comparingDouble(toplist -> toplist.playerDamage));
            for (String key : Objects.requireNonNull(dragonDataConfig.getConfigurationSection("damage"))
                    .getKeys(false)) {
                Main.debug(key);
                if (getDouble("damage." + key) != 0) {
                    PlayerData playerData = new PlayerData();
                    playerData.playerName = key;
                    playerData.playerDamage = Main.round(getDouble("damage." + key), 2);
                    topList.add(playerData);
                }
            }
            for (PlayerData data : topList) {
                Bukkit.broadcastMessage(Formatter
                        .FText("&a" + data.playerName + " hizo &5" + data.playerDamage + "&a de daño", true));
            }
            /*//noinspection ResultOfMethodCallIgnored
            dragonDataFile.delete();
            dragonDataFile = null;
            dragonDataConfig = null;*/
        }
    }

    private void resetHability() {
        canUseSkill = false;
        sethabilityCooldown = habilityCooldown;
    }

    public enum DragonPhase {
        INITIAL("&a&lInicial", 3500, 15), MID("&e&lMedio", 2000, 10), END("&c&lFinal", 1000, 5), FINAL("&4&lEnrabiado", 500, 0);

        String phaseName;
        int health;
        int skillCooldown;

        DragonPhase(String phaseName, int health, int skillCooldown) {
            this.phaseName = phaseName;
            this.health = health;
            this.skillCooldown = skillCooldown;
        }

        public String getPhaseName() {
            return phaseName;
        }

        public int getHealth() {
            return health;
        }

        public int getSkillCooldown() {
            return skillCooldown;
        }
    }

    public static class PlayerData {
        public String playerName;
        public Double playerDamage;
    }

    private void DragonData() {
        dragonDataFile = new File(main.getDataFolder(), "dragon-data.yml");
        dragonDataConfig = YamlConfiguration.loadConfiguration(dragonDataFile);
        if (!dragonDataFile.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                dragonDataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void set(String key, String value) {
        dragonDataConfig.set(key, value);
        try {
            dragonDataConfig.save(dragonDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void set(String key, Integer value) {
        dragonDataConfig.set(key, value);
        try {
            dragonDataConfig.save(dragonDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void set(String key, Double value) {
        dragonDataConfig.set(key, value);
        try {
            dragonDataConfig.save(dragonDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void set(String key, Boolean value) {
        dragonDataConfig.set(key, value);
        try {
            dragonDataConfig.save(dragonDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getString(String key) {
        return dragonDataConfig.getString(key);
    }

    public static Integer getInt(String key) {
        return dragonDataConfig.getInt(key);
    }

    public static Double getDouble(String key) {
        return dragonDataConfig.getDouble(key);
    }

    public static Boolean getBool(String key) {
        return dragonDataConfig.getBoolean(key);
    }
}
