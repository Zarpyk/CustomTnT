package guerrero61.customtnt.mobs.enderdragon;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mainutils.config.Config;
import guerrero61.customtnt.mainutils.config.ConfigBuilder;
import guerrero61.customtnt.mobs.enderdragon.dragonskills.*;
import guerrerocraft61.particleapi.VectorMath;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.math.RoundingMode;
import java.util.*;

public class TnTDragon extends ConfigBuilder implements Listener {

    private final Main main;
    public static String fileName = "dragon-data";

    public static String dragonNameColor = "&c";
    public static String dragonName = dragonNameColor + "TnT Dragon";
    private double maxHealth = 0;

    private int habilityCooldown = 15;
    private final int getAutoHabilityCooldown = 30;
    private int autoHabilityCooldown;
    private int sethabilityCooldown;
    private boolean canUseSkill;
    private boolean schedulerActivated = false;
    private boolean autoHabilitySchedulerActivated = false;

    private DragonPhase dragonPhase = DragonPhase.INITIAL;

    private final int perPlayerScales = 6;
    private final int perPlayerXP = 3097;
    private int totalScaleAmount = 0;
    private int totalXPAmount = 0;

    public TnTDragon(Main m) {
        super(fileName);
        main = m;
        sethabilityCooldown = habilityCooldown;
        autoHabilityCooldown = getAutoHabilityCooldown;
        canUseSkill = false;
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        Entity entity = event.getEntity();
        if (!entity.getType().equals(EntityType.ENDER_DRAGON)) {
            return;
        }
        EnderDragon enderDragon = (EnderDragon) entity;
        if (Config.getBool(Config.Options.CustomDragonFirstTime)) {
            maxHealth = Config.getDouble(Config.Options.CustomDragonFirstTimeHealth);
            totalScaleAmount = 64;
            totalXPAmount = 30970;
            for (Player player : Bukkit.getOnlinePlayers()) {
                set("participate." + player.getName(), true);
            }
            Config.set(Config.Options.CustomDragonFirstTime, false);
        } else if (!Config
                .getBool(Config.Options.CustomDragonPerPlayerDificultyScalingMode)) {
            maxHealth = Config.getDouble(Config.Options.CustomDragonStaticHealth);
            totalScaleAmount = 64;
            totalXPAmount = 30970;
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getWorld().equals(enderDragon.getWorld()) && player.getLocation()
                        .distance(enderDragon.getLocation()) < 300) {
                    set("participate." + player.getName(), true);
                }
            }
        } else if (Config
                .getBool(Config.Options.CustomDragonPerPlayerDificultyScalingMode)) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getWorld().equals(enderDragon.getWorld()) && player.getLocation()
                        .distance(enderDragon.getLocation()) < 300) {
                    maxHealth += Config.getDouble(Config.Options.CustomDragonPerPlayerHealth);
                    totalScaleAmount += perPlayerScales;
                    totalXPAmount += perPlayerXP;
                    set("participate." + player.getName(), true);
                }
            }
        }

        if (getDouble("maxHealth") == 0) {
            set("maxHealth", maxHealth);
        }

        if (getDouble("dragonHealth") == 0) {
            Objects.requireNonNull(enderDragon.getAttribute(Attribute.GENERIC_MAX_HEALTH))
                    .setBaseValue(maxHealth);
            enderDragon.setHealth(maxHealth);
        } else {
            maxHealth = getDouble("maxHealth");
            Objects.requireNonNull(enderDragon.getAttribute(Attribute.GENERIC_MAX_HEALTH))
                    .setBaseValue(maxHealth);
            enderDragon.setHealth(getDouble("dragonHealth"));
        }

        setDragonPhase(enderDragon, Main.round(enderDragon.getHealth(), 1));
        enderDragon.setCustomNameVisible(true);
        enderDragon.setGlowing(true);
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
                        for (String key : Objects
                                .requireNonNull(getConfigurationSection("participate"))
                                .getKeys(false)) {
                            Main.debug("TnTDragon:" + key);
                            Player player = Bukkit.getPlayer(key);
                            if (player != null) {
                                if (player.getLocation().getWorld().equals(enderDragon.getWorld())) {
                                    playerList.add(player);
                                }
                            } else {
                                Main.consoleMsg(Formatter.FText("&c&lERROR: " + key + " no existe"));
                            }
                        }
                        Player selectedTarget = playerList
                                .get(playerList.size() > 0 ? Main.random(0, playerList.size() - 1) : 0);
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
        if (!Main.contains(entity.getCustomName(), dragonName)) {
            return;
        }
        if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) || event.getCause()
                .equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)) {
            event.setDamage(0);
            event.setCancelled(true);
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

        if (!getBool("participate." + player.getName())) {
            event.setDamage(0);
            event.setCancelled(true);
            return;
        }

        EnderDragon enderDragon = (EnderDragon) entity;

        if (!damager.getType().equals(EntityType.ARROW) || (damager.getType()
                .equals(EntityType.ARROW) && !damager.isSilent())) {
            set("dragonHealth", Main.round(enderDragon.getHealth() - event.getDamage(), 1));
            set("damage." + player.getName(), Main
                    .round(getDouble("damage." + player.getName()) + event.getDamage(), 2));
            set("totalDamage", Main
                    .round(getDouble("totalDamage") + event.getDamage(), 2));
        }

        Main.debug(event.getDamage() + "");
        setDragonPhase(enderDragon, Main.round(enderDragon.getHealth() - event.getDamage(), 1));

        int skillNumber = 0;
        if (dragonPhase == DragonPhase.INITIAL) {
            skillNumber = Main.random(1, 4);
            habilityCooldown = DragonPhase.INITIAL.getSkillCooldown();
        } else if (dragonPhase == DragonPhase.MID) {
            skillNumber = Main.random(1, 7);
            if (skillNumber == 6 && getBool("skill6Active")) {
                int random = Main.random(1, 2);
                if (random == 1) skillNumber = Main.random(1, 5);
                if (random == 2) skillNumber = 7;
            }
            habilityCooldown = DragonPhase.MID.getSkillCooldown();
        } else if (dragonPhase == DragonPhase.END) {
            skillNumber = Main.random(1, 10);
            if (skillNumber == 6 && getBool("skill6Active")) {
                int random = Main.random(1, 2);
                if (random == 1) skillNumber = Main.random(1, 5);
                if (random == 2) skillNumber = Main.random(7, 10);
            }
            habilityCooldown = DragonPhase.END.getSkillCooldown();
        } else if (dragonPhase == DragonPhase.FINAL) {
            skillNumber = Main.random(1, 10);
            if (skillNumber == 6 && getBool("skill6Active")) {
                int random = Main.random(1, 2);
                if (random == 1) skillNumber = Main.random(1, 5);
                if (random == 2) skillNumber = Main.random(7, 10);
            }
            habilityCooldown = DragonPhase.FINAL.getSkillCooldown();
        }

        Main.debug("Skill number: " + skillNumber);

        switch (skillNumber) {
            case 1:
                if (!damager.getType().equals(EntityType.PLAYER)) {
                    resetHability();
                    Location dragonLocation = enderDragon.getLocation();
                    Location playerLocation = player.getLocation();
                    if (dragonLocation.getWorld().equals(playerLocation.getWorld())) {
                        Vector direction = VectorMath.directionVector(playerLocation, dragonLocation);
                        enderDragon.teleport(enderDragon.getLocation().setDirection(direction));
                        dragonLocation = enderDragon.getLocation();
                        Location finalDragonLocation = dragonLocation;
                        new DragonSkill1(main).Skill1(finalDragonLocation, player);
                    }
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
                    new DragonSkill4().Skill4(player);
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
                    new DragonSkill8().Skill8(player, enderDragon);
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
                && Objects.requireNonNull(e.getEntity().getCustomName())
                .contains(Formatter.FText(TnTDragon.dragonName, true))) {
            e.getDrops().clear();
            SortedSet<PlayerData> topList =
                    new TreeSet<>(Comparator.comparingDouble(toplist -> -toplist.playerDamage));
            for (String key : Objects.requireNonNull(getConfigurationSection("damage"))
                    .getKeys(false)) {
                if (getDouble("damage." + key) != 0) {
                    PlayerData playerData = new PlayerData();
                    playerData.playerName = key;
                    playerData.playerDamage = Main.round(getDouble("damage." + key), 2);
                    playerData.damagePercent = (playerData.playerDamage / getDouble("totalDamage")) * 100;
                    topList.add(playerData);
                }
            }
            int i = 1;
            Bukkit.broadcastMessage(Formatter.FText("&a-----------------&c&lTop Damge&a-----------------", true));
            for (PlayerData data : topList) {
                RoundingMode roundingMode = RoundingMode.HALF_DOWN;
                int scaleAmount = (int) Main.round((totalScaleAmount * (data.damagePercent / 100)), 0, roundingMode);
                int xpAmount = (int) Main.round((totalXPAmount * (data.damagePercent / 100)), 0, roundingMode);
                if (scaleAmount != 0) {
                    Bukkit.broadcastMessage(Formatter
                            .FText("&c" + i + ".&6" + data.playerName + " hizo &c" + data.playerDamage + "&6 de daño (" + Main
                                    .round(data.damagePercent, 2, roundingMode) + "%)", true, Bukkit
                                    .getPlayer(data.playerName))
                    );
                    Bukkit.broadcastMessage(Formatter
                            .FText("&c➢ &6Consiguio " + scaleAmount + " &5&lEscamas &6y " + xpAmount + " de XP", true, Bukkit
                                    .getPlayer(data.playerName))
                    );
                    i++;
                    ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                    String command = "mi MATERIAL DRAGON_SCALE " + data.playerName + " " + scaleAmount;
                    Main.debug(command);
                    Bukkit.dispatchCommand(console, command);
                    String command2 = "xp give " + data.playerName + " " + xpAmount;
                    Main.debug(command2);
                    Bukkit.dispatchCommand(console, command2);
                }
            }
            Bukkit.broadcastMessage(Formatter.FText("&a------------------------------------------", true));
            set("skill6Active", false);
            set("disableSkill5", true);
            new BukkitRunnable() {
                @Override
                public void run() {
                    rename("dragon-data-backup.yml");
                    deleteConfig();
                }
            }.runTaskLater(main, 20);
        }
    }

    private void resetHability() {
        canUseSkill = false;
        sethabilityCooldown = habilityCooldown;
    }

    private void setDragonPhase(EnderDragon enderDragon, double health) {
        if (health > maxHealth * (DragonPhase.INITIAL.getPercent() / 100)) {
            dragonPhase = DragonPhase.INITIAL;
        } else if (health < maxHealth * (DragonPhase.INITIAL.getPercent() / 100)
                && health > maxHealth * (DragonPhase.MID.getPercent() / 100)) {
            dragonPhase = DragonPhase.MID;
        } else if (health < maxHealth * (DragonPhase.MID.getPercent() / 100)
                && health > maxHealth * (DragonPhase.END.getPercent() / 100)) {
            dragonPhase = DragonPhase.END;
        } else if (health < maxHealth * (DragonPhase.FINAL.getPercent() / 100)) {
            dragonPhase = DragonPhase.FINAL;
        }
        Main.debug(health + " " + enderDragon.getHealth());
        enderDragon.setCustomName(Formatter
                .FText(dragonName + " - Fase " + dragonPhase
                        .getPhaseName() + dragonNameColor + " - " + (health <= 0 ? "0" : health) + "/" + maxHealth, true));
    }

    public enum DragonPhase {
        INITIAL("&a&lInicial", 75, 15), MID("&e&lMedio", 50, 10), END("&c&lFinal", 25, 5), FINAL("&4&lEnrabiado", 10, 0);

        String phaseName;
        double percent;
        int skillCooldown;

        DragonPhase(String phaseName, int percent, int skillCooldown) {
            this.phaseName = phaseName;
            this.percent = percent;
            this.skillCooldown = skillCooldown;
        }

        public String getPhaseName() {
            return phaseName;
        }

        public double getPercent() {
            return percent;
        }

        public int getSkillCooldown() {
            return skillCooldown;
        }
    }

    public static class PlayerData {
        public String playerName;
        public Double playerDamage;
        public double damagePercent;
    }
}
