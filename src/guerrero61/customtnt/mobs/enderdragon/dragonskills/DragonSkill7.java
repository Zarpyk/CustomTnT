package guerrero61.customtnt.mobs.enderdragon.dragonskills;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.mobs.enderdragon.TnTDragon;
import guerrerocraft61.particleapi.Formatter;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vex;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Objects;

public class DragonSkill7 implements Listener {

    private final Main main;
    public static String skillName = "TnT Guardian";
    private BukkitTask task;

    private final String vexName = "&cTnT Vex";

    public DragonSkill7(Main m) {
        main = m;
    }

    public void Skill7(Player player) {
        Main.debug("Skill 7");
        player.sendMessage(guerrero61.customtnt.mainutils.Formatter
                .FText(TnTDragon.dragonName + " &4&lha usado la habilidad &2&l" + skillName + " &4&len ti."));
        Vex vex = (Vex) player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.VEX);
        Objects.requireNonNull(vex.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(65);
        vex.setHealth(65);
        vex.setCanPickupItems(false);
        vex.setCharging(true);
        vex.setCustomName(Formatter.FText(vexName, true, player));
        vex.setCustomNameVisible(true);
        vex.setGlowing(true);
        vex.setLootTable(null);
        vex.setRemoveWhenFarAway(false);

        ItemStack vexTNT = new ItemStack(Material.TNT);
        vexTNT.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2);
        Objects.requireNonNull(vex.getEquipment()).setItemInMainHand(vexTNT);
        Objects.requireNonNull(vex.getEquipment()).setItemInMainHandDropChance(0);
        Objects.requireNonNull(vex.getEquipment()).setItemInOffHand(vexTNT);
        Objects.requireNonNull(vex.getEquipment()).setItemInOffHandDropChance(0);
        Objects.requireNonNull(vex.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).setBaseValue(0);
        Objects.requireNonNull(vex.getAttribute(Attribute.GENERIC_ARMOR)).setBaseValue(10);
        Objects.requireNonNull(vex.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).setBaseValue(0.5);

        task = new BukkitRunnable() {
            @Override
            public void run() {
                vex.setTarget(player);
            }
        }.runTaskTimer(main, 0, 20L);
    }

    @EventHandler
    public void onEntityAttack(EntityDamageByEntityEvent event) {
        Entity entity = event.getDamager();
        if (entity.getType() == EntityType.VEX && Objects.equals(entity.getCustomName(), vexName)) {
            Entity attackedEntity = event.getEntity();
            entity.setInvulnerable(true);
            attackedEntity.getWorld().createExplosion(attackedEntity.getLocation(), 1f, false, false);
            entity.setInvulnerable(false);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (entity.getType() == EntityType.VEX && Objects.equals(entity.getCustomName(), vexName)) {
            if (event.getEntity().getKiller() == null) {
                for (Player player : entity.getLocation().getNearbyPlayers(10)) {
                    player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1.5F);
                }
                return;
            }
            Player killer = event.getEntity().getKiller();
            if (Objects.equals(entity.getCustomName(), Formatter.FText(vexName, true, killer))) {
                killer.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 20, 10));
                for (Player player : entity.getLocation().getNearbyPlayers(10)) {
                    player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1.5F);
                }
                if (task != null) {
                    task.cancel();
                }
            }
        }
    }
}
