package guerrero61.customtnt.mobs.enderdragon;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mainutils.config.Config;
import guerrero61.customtnt.mainutils.config.ConfigClass;
import guerrero61.customtnt.mobs.enderdragon.dragonskills.*;
import guerrerocraft61.particleapi.VectorMath;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
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

import java.awt.*;
import java.math.RoundingMode;
import java.util.List;
import java.util.*;

public class TnTDragon extends ConfigClass implements Listener {

    public static String fileName = "dragon-data";
    public static String dragonNameColor = "&c";
    public static String dragonName = dragonNameColor + "TnT Dragon";
    private final Main main;
    private final JDA api;
    private final int getAutoHabilityCooldown = 30;
    private final int perPlayerScales = 6;
    private final int perPlayerXP = 3097;
    private double maxHealth = 0;
    private int habilityCooldown = 15;
    private int autoHabilityCooldown;
    private int sethabilityCooldown;
    private boolean canUseSkill;
    private boolean schedulerActivated = false;
    private boolean autoHabilitySchedulerActivated = false;
    private DragonPhase dragonPhase = DragonPhase.INITIAL;
    private int totalScaleAmount = 0;
    private int totalXPAmount = 0;

    public TnTDragon(Main m, JDA a) {
        main = m;
        api = a;
        sethabilityCooldown = habilityCooldown;
        autoHabilityCooldown = getAutoHabilityCooldown;
        canUseSkill = false;
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        Entity entity = event.getEntity();
        if(!entity.getType().equals(EntityType.ENDER_DRAGON)) {
            return;
        }
        Main.debug(Boolean.toString(dataConfig == null));
        if(dataConfig == null || getDouble("dragonHealth") != null || getDouble("maxHealth") != null ||
           getConfigurationSection("paticipate") != null) {
            dataConfig = null;
            deleteConfig();
            protectedFileName = fileName;
            dataConfig = CreateConfig(fileName);
        }
        maxHealth = 0;
        totalScaleAmount = 0;
        totalXPAmount = 0;
        EnderDragon enderDragon = (EnderDragon) entity;
        if(Config.getBool(Config.Options.CustomDragonFirstTime)) {
            maxHealth = Config.getDouble(Config.Options.CustomDragonFirstTimeHealth);
            totalScaleAmount = 64;
            totalXPAmount = 30970;
            for (Player player : Bukkit.getOnlinePlayers()) {
                set("participate." + player.getName(), true);
            }
            Config.set(Config.Options.CustomDragonFirstTime, false);
        } else if(!Config.getBool(Config.Options.CustomDragonPerPlayerDificultyScalingMode)) {
            maxHealth = Config.getDouble(Config.Options.CustomDragonStaticHealth);
            totalScaleAmount = 64;
            totalXPAmount = 30970;
            for (Player player : Bukkit.getOnlinePlayers()) {
                if(player.getWorld().equals(enderDragon.getWorld()) &&
                   player.getLocation().distance(enderDragon.getLocation()) < 300) {
                    set("participate." + player.getName(), true);
                }
            }
        } else if(Config.getBool(Config.Options.CustomDragonPerPlayerDificultyScalingMode)) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if(player.getWorld().equals(enderDragon.getWorld()) &&
                   player.getLocation().distance(enderDragon.getLocation()) < 300) {
                    maxHealth += Config.getDouble(Config.Options.CustomDragonPerPlayerHealth);
                    totalScaleAmount += perPlayerScales;
                    totalXPAmount += perPlayerXP;
                    set("participate." + player.getName(), true);
                }
            }
        }

        if(getDouble("maxHealth") == null || getDouble("maxHealth") == 0) {
            set("maxHealth", maxHealth);
        }
        if(getDouble("totalScaleAmount") == null || getDouble("totalScaleAmount") == 0) {
            set("totalScaleAmount", totalScaleAmount);
        }
        if(getDouble("totalXPAmount") == null || getDouble("totalXPAmount") == 0) {
            set("totalXPAmount", totalXPAmount);
        }

        Objects.requireNonNull(enderDragon.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(maxHealth);
        if(maxHealth < 0) {
            Bukkit.broadcastMessage(Formatter.FText("&c&lERROR AL GENERAR EL DRAGON"));
        }
        double realMaxHealth = 0;
        double realScaleAmount = 0;
        double realXPAmount = 0;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if(player.getWorld().equals(enderDragon.getWorld()) &&
               player.getLocation().distance(enderDragon.getLocation()) < 300) {
                realMaxHealth += Config.getDouble(Config.Options.CustomDragonPerPlayerHealth);
                realScaleAmount += perPlayerScales;
                realXPAmount += perPlayerXP;
            }
        }
        if(maxHealth != realMaxHealth || totalScaleAmount != realScaleAmount || totalXPAmount != realXPAmount) {
            Bukkit.broadcastMessage(Formatter.FText("&c&lERROR AL GENERAR EL DRAGON"));
            Bukkit.broadcastMessage(Formatter.FText("&c&lERROR AL GENERAR EL DRAGON"));
            Bukkit.broadcastMessage(Formatter.FText("&c&lERROR AL GENERAR EL DRAGON"));
            enderDragon.remove();
        }

        enderDragon.setHealth(maxHealth);
        setDragonPhase(enderDragon, Main.round(enderDragon.getHealth(), 1));
        enderDragon.setCustomNameVisible(true);
        enderDragon.setGlowing(true);
        enderDragon.setMaximumNoDamageTicks(0);
        enderDragon.setNoDamageTicks(0);
        enderDragon.clearLootTable();
        if(!schedulerActivated) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Main.debug(Integer.toString(sethabilityCooldown));
                    if(sethabilityCooldown > 0 && !canUseSkill) {
                        sethabilityCooldown--;
                    } else if(sethabilityCooldown == 0) {
                        canUseSkill = true;
                    }

                    if(enderDragon.getHealth() <= 0 || !enderDragon.isValid()) {
                        cancel();
                    }
                }
            }.runTaskTimer(main, 0, 20L);
            schedulerActivated = true;
        }
        if(!autoHabilitySchedulerActivated) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Main.debug(Integer.toString(autoHabilityCooldown));
                    if(autoHabilityCooldown > 0) {
                        autoHabilityCooldown--;
                    } else {
                        List<Player> playerList = new ArrayList<>();
                        for (String key : Objects.requireNonNull(getConfigurationSection("participate")).getKeys(
                                false)) {
                            Player player = Bukkit.getPlayer(key);
                            if(player != null) {
                                if(player.getLocation().getWorld().equals(enderDragon.getWorld())) {
                                    playerList.add(player);
                                }
                            }
                        }
                        if(playerList.size() > 0) {
                            Player selectedTarget = playerList.get(
                                    playerList.size() == 1 ? 0 : Main.random(0, playerList.size() - 1));
                            onDamage(new EntityDamageByEntityEvent(selectedTarget, enderDragon,
                                    EntityDamageEvent.DamageCause.ENTITY_ATTACK, 0));
                            autoHabilityCooldown = getAutoHabilityCooldown;
                        } else {
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                set("participate." + player.getName(), true);
                            }
                        }
                    }
                    if(enderDragon.getHealth() <= 0 || !enderDragon.isValid()) {
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
        if(!entity.getType().equals(EntityType.ENDER_DRAGON)) {
            return;
        }
        if(!Main.contains(entity.getCustomName(), dragonName)) {
            return;
        }
        if(event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) ||
           event.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)) {
            event.setDamage(0);
            event.setCancelled(true);
        }
        if(dataConfig == null) {
            protectedFileName = fileName;
            dataConfig = CreateConfig(fileName);
        }
        if(getDouble("maxHealth") != null || getDouble("maxHealth") != 0) {
            maxHealth = getDouble("maxHealth");
        }
        if(getDouble("totalScaleAmount") != null || getInt("totalScaleAmount") != 0) {
            totalScaleAmount = getInt("totalScaleAmount");
        }
        if(getDouble("totalXPAmount") != null || getInt("totalXPAmount") != 0) {
            totalXPAmount = getInt("totalXPAmount");
        }
        if(!schedulerActivated) {
            new BukkitRunnable() {
                public void run() {
                    Main.debug(Integer.toString(sethabilityCooldown));
                    if(sethabilityCooldown > 0 && !canUseSkill) {
                        sethabilityCooldown--;
                    } else if(sethabilityCooldown == 0) {
                        canUseSkill = true;
                    }

                    if(((EnderDragon) entity).getHealth() <= 0 || !entity.isValid()) {
                        cancel();
                    }
                }
            }.runTaskTimer(main, 0, 20L);
            schedulerActivated = true;
        }
        EnderDragon enderDragon = (EnderDragon) entity;
        if(!autoHabilitySchedulerActivated) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Main.debug(Integer.toString(autoHabilityCooldown));
                    if(autoHabilityCooldown > 0) {
                        autoHabilityCooldown--;
                    } else {
                        List<Player> playerList = new ArrayList<>();
                        for (String key : Objects.requireNonNull(getConfigurationSection("participate")).getKeys(
                                false)) {
                            Player player = Bukkit.getPlayer(key);
                            if(player != null) {
                                if(player.getLocation().getWorld().equals(enderDragon.getWorld())) {
                                    playerList.add(player);
                                }
                            }
                        }
                        if(playerList.size() > 0) {
                            Player selectedTarget = playerList.get(
                                    playerList.size() == 1 ? 0 : Main.random(0, playerList.size() - 1));
                            onDamage(new EntityDamageByEntityEvent(selectedTarget, enderDragon,
                                    EntityDamageEvent.DamageCause.ENTITY_ATTACK, 0));
                            autoHabilityCooldown = getAutoHabilityCooldown;
                        } else {
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                set("participate." + player.getName(), true);
                            }
                        }
                    }
                    if(enderDragon.getHealth() <= 0 || !enderDragon.isValid()) {
                        cancel();
                    }
                }

            }.runTaskTimer(main, 0, 20L);
            autoHabilitySchedulerActivated = true;
        }
        Entity damager = event.getDamager();
        if(!damager.getType().equals(EntityType.PLAYER) && !damager.getType().equals(EntityType.ARROW) &&
           !damager.getType().equals(EntityType.SPECTRAL_ARROW) && !damager.getType().equals(EntityType.TRIDENT)) {
            return;
        }
        Player player;
        if(damager.getType().equals(EntityType.ARROW)) {
            player = (Player) ((Arrow) damager).getShooter();
        } else if(damager.getType().equals(EntityType.SPECTRAL_ARROW)) {
            player = (Player) ((SpectralArrow) damager).getShooter();
        } else if(damager.getType().equals(EntityType.TRIDENT)) {
            player = (Player) ((Trident) damager).getShooter();
        } else {
            player = (Player) damager;
        }
        assert player != null;
        if(!getBool("participate." + player.getName())) {
            event.setDamage(0);
            event.setCancelled(true);
            return;
        }
        Main.debug("EntityDamageByEntityEvent can use skill");

        set("dragonHealth", Main.round(enderDragon.getHealth() - event.getDamage(), 1));
        set("damage." + player.getName(), Main.round(getDouble("damage." + player.getName()) + event.getDamage(), 2));
        set("totalDamage", Main.round(getDouble("totalDamage") + event.getDamage(), 2));

        Main.debug(event.getDamage() + "");
        setDragonPhase(enderDragon, Main.round(enderDragon.getHealth() - event.getDamage(), 1));
        if(canUseSkill) {
            int skillNumber = 0;

            if(dragonPhase == DragonPhase.INITIAL || (dragonPhase == DragonPhase.MID && maxHealth < 1000) ||
               (dragonPhase == DragonPhase.END && maxHealth < 1000) ||
               (dragonPhase == DragonPhase.FINAL && maxHealth < 1000)) {
                skillNumber = Main.random(1, 4);
                habilityCooldown = dragonPhase.getSkillCooldown();
            } else if((dragonPhase == DragonPhase.MID && maxHealth >= 1000) ||
                      (dragonPhase == DragonPhase.END && maxHealth >= 1000 && maxHealth < 1500) ||
                      (dragonPhase == DragonPhase.FINAL && maxHealth >= 1000 && maxHealth < 1500)) {
                skillNumber = Main.random(1, 7);
                if(skillNumber == 6 && getBool("skill8Active")) {
                    int random = Main.random(1, 2);
                    if(random == 1) skillNumber = Main.random(1, 5);
                    if(random == 2) skillNumber = 7;
                }
                habilityCooldown = dragonPhase.getSkillCooldown();
            } else if(dragonPhase == DragonPhase.END && maxHealth >= 1500 ||
                      (dragonPhase == DragonPhase.FINAL && maxHealth >= 1500 && maxHealth < 2000)) {
                skillNumber = Main.random(1, 10);
                if(skillNumber == 6 && getBool("skill8Active")) {
                    int random = Main.random(1, 2);
                    if(random == 1) skillNumber = Main.random(1, 5);
                    if(random == 2) skillNumber = Main.random(7, 10);
                }
                habilityCooldown = dragonPhase.getSkillCooldown();
            } else if(dragonPhase == DragonPhase.FINAL && maxHealth >= 2000) {
                skillNumber = Main.random(1, 10);
                if(skillNumber == 6 && getBool("skill8Active")) {
                    int random = Main.random(1, 2);
                    if(random == 1) skillNumber = Main.random(1, 5);
                    if(random == 2) skillNumber = Main.random(7, 10);
                }
                habilityCooldown = dragonPhase.getSkillCooldown();
            }

            Main.debug("Skill number: " + skillNumber);

            switch (skillNumber) {
                case 1:
                    if(!damager.getType().equals(EntityType.PLAYER)) {
                        resetHability();
                        Location dragonLocation = enderDragon.getLocation();
                        Location playerLocation = player.getLocation();
                        if(dragonLocation.getWorld().equals(playerLocation.getWorld())) {
                            Vector direction = VectorMath.directionVector(playerLocation, dragonLocation);
                            enderDragon.teleport(enderDragon.getLocation().setDirection(direction));
                            dragonLocation = enderDragon.getLocation();
                            Location finalDragonLocation = dragonLocation;
                            new DragonSkill1(main).Skill1(finalDragonLocation, player);
                        }
                    }
                    return;
                case 2:
                    if(canUseSkill) {
                        resetHability();
                        new DragonSkill2(main).Skill2(player);
                    }
                    return;
                case 3:
                    if(canUseSkill) {
                        resetHability();
                        new DragonSkill3(main).Skill3(player);
                    }
                    return;
                case 4:
                    if(canUseSkill) {
                        resetHability();
                        new DragonSkill4().Skill4(player);
                    }
                    return;
                case 5:
                    if(canUseSkill) {
                        resetHability();
                        new DragonSkill5(main).Skill5(player);
                    }
                    return;
                case 6:
                    if(canUseSkill) {
                        resetHability();
                        new DragonSkill6().Skill6(player, enderDragon);
                    }
                    return;
                case 7:
                    if(canUseSkill) {
                        resetHability();
                        new DragonSkill7(main).Skill7(player);
                    }
                    return;
                case 8:
                    if(canUseSkill) {
                        resetHability();
                        new DragonSkill8(main).Skill8(enderDragon);
                    }
                    return;
                case 9:
                    if(canUseSkill) {
                        resetHability();
                        new DragonSkill9(main).Skill9(enderDragon);
                    }
                    return;
                case 10:
                    if(canUseSkill) {
                        resetHability();
                        new DragonSkill10(main).Skill10(enderDragon, player);
                    }
                    return;
                default:
                    Bukkit.broadcastMessage(Formatter.FText("&cError al intentar usar habilidades del Dragon de TnT"));
            }
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if(e.getEntity().getType() == EntityType.ENDER_DRAGON &&
           Objects.requireNonNull(e.getEntity().getCustomName()).contains(
                   Formatter.FText(TnTDragon.dragonName, true))) {
            e.getDrops().clear();
            SortedSet<PlayerData> topList = new TreeSet<>(Comparator.comparingDouble(toplist -> -toplist.playerDamage));
            double totalDamagePercent = 0;
            RoundingMode roundingMode = RoundingMode.HALF_DOWN;
            for (String key : Objects.requireNonNull(getConfigurationSection("damage")).getKeys(false)) {
                if(getDouble("damage." + key) != 0) {
                    PlayerData playerData = new PlayerData();
                    playerData.playerName = key;
                    playerData.playerDamage = Main.round(getDouble("damage." + key), 2);
                    playerData.damagePercent = (playerData.playerDamage / getDouble("totalDamage")) * 100;
                    playerData.scaleAmount = (int) Main.round((totalScaleAmount * (playerData.damagePercent / 100)), 0,
                            roundingMode);
                    playerData.XPAmount = (int) Main.round((totalXPAmount * (playerData.damagePercent / 100)), 0,
                            roundingMode);
                    totalDamagePercent += Main.round(playerData.damagePercent, 2, roundingMode);
                    topList.add(playerData);
                }
            }
            if(totalDamagePercent < 95 && totalDamagePercent > 105) {
                Bukkit.broadcastMessage(Formatter.FText("&c&lERROR, NO HAY EL 100%±5 DEL DAÑO", true));
                Bukkit.broadcastMessage(Formatter.FText("&c&lERROR, NO HAY EL 100%±5 DEL DAÑO", true));
                Bukkit.broadcastMessage(Formatter.FText("&c&lERROR, NO HAY EL 100%±5 DEL DAÑO", true));
            } else if(totalDamagePercent >= 95 && totalDamagePercent <= 105) {
                int i = 1;
                EmbedBuilder embed = new EmbedBuilder();
                embed.setAuthor("Top Damage", null,
                        "https://vignette.wikia.nocookie.net/minecraft/images/d/d6/DragonHead.gif");
                embed.setColor(new Color(125, 255, 100));
                Bukkit.broadcastMessage(Formatter.FText("&a-----------------&c&lTop Damge&a-----------------", true));
                for (PlayerData data : topList) {
                    if(data.scaleAmount != 0) {
                        double damagePercent = Main.round(data.damagePercent, 2, roundingMode);
                        Bukkit.broadcastMessage(Formatter.FText(
                                "&c" + i + ".&6" + data.playerName + " hizo &c" + data.playerDamage + "&6 de daño (" +
                                damagePercent + "%)", true, Bukkit.getPlayer(data.playerName)));
                        embed.addField("Top " + i, data.playerName, true);
                        embed.addField("Daño", data.playerDamage + " de daño (" + damagePercent + "%)", true);
                        Bukkit.broadcastMessage(Formatter.FText(
                                "&c➢ &6Consiguio " + data.scaleAmount + " Escamas y " + data.XPAmount + " de XP", true,
                                Bukkit.getPlayer(data.playerName)));
                        if(i == 1) {
                            int random = Main.random(0, 100);
                            if(random > 75) {
                                Bukkit.broadcastMessage(Formatter.FText("&c➢ &6Consiguio 1 &6&lCorazon de Dragon", true,
                                        Bukkit.getPlayer(data.playerName)));
                                embed.addField("Recompensa", data.scaleAmount + " Escamas y " + data.XPAmount +
                                                             " de XP y 1 Corazon de Dragon", true);
                                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                                String command = "mi give MATERIAL DRAGON_HEART " + data.playerName + " 1";
                                Bukkit.dispatchCommand(console, command);
                            } else {
                                embed.addField("Recompensa",
                                        data.scaleAmount + " Escamas y " + data.XPAmount + " de XP", true);
                            }
                        } else {
                            embed.addField("Recompensa", data.scaleAmount + " Escamas y " + data.XPAmount + " de XP",
                                    true);
                        }
                        i++;

                        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                        String command = "mi give MATERIAL DRAGON_SCALE " + data.playerName + " " + data.scaleAmount;
                        Main.debug(command);
                        Bukkit.dispatchCommand(console, command);
                        String command2 = "xp give " + data.playerName + " " + data.XPAmount;
                        Main.debug(command2);
                        Bukkit.dispatchCommand(console, command2);
                    }
                }
                Bukkit.broadcastMessage(Formatter.FText("&a--------------------------------------------", true));
                for (String channelID : Config.getStringList(Config.Options.ChannelsSendMsg)) {
                    TextChannel textChannel = api.getTextChannelById(channelID);
                    if(textChannel != null) {
                        textChannel.sendMessage(embed.build()).queue();
                    }
                }
            }
            Main.debug("skill8 false");
            set("skill8Active", false);
            set("disableSkill5", true);
            new BukkitRunnable() {
                @Override
                public void run() {
                    maxHealth = 0;
                    totalScaleAmount = 0;
                    totalXPAmount = 0;
                    rename("dragon-data-backup");
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
        if(health > maxHealth * (DragonPhase.INITIAL.getPercent() / 100)) {
            dragonPhase = DragonPhase.INITIAL;
        } else if(health < maxHealth * (DragonPhase.INITIAL.getPercent() / 100) &&
                  health > maxHealth * (DragonPhase.MID.getPercent() / 100)) {
            dragonPhase = DragonPhase.MID;
        } else if(health < maxHealth * (DragonPhase.MID.getPercent() / 100) &&
                  health > maxHealth * (DragonPhase.END.getPercent() / 100)) {
            dragonPhase = DragonPhase.END;
        } else if(health < maxHealth * (DragonPhase.FINAL.getPercent() / 100)) {
            dragonPhase = DragonPhase.FINAL;
        }
        Main.debug(health + " " + enderDragon.getHealth());
        enderDragon.setCustomName(Formatter.FText(
                dragonName + " - Fase " + dragonPhase.getPhaseName() + dragonNameColor + " - " +
                (health <= 0 ? "0" : health) + "/" + maxHealth, true));
    }

    public enum DragonPhase {
        INITIAL("&a&lInicial", 75, 15), MID("&e&lMedio", 50, 10), END("&c&lFinal", 25, 5), FINAL("&4&lEnrabiado", 10,
                0);

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
        public int scaleAmount;
        public int XPAmount;
    }
}
