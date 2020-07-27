package guerrero61.customtnt.mobs;

import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.MainUtils.Formatter;
import guerrero61.customtnt.mobs.dragonskills.*;
import guerrerocraft61.particleapi.VectorMath;

public class TnTDragon implements Listener {

	private final int maxHealth = 5000;
	private int habilityCooldown = 15;
	private int autoHabilityCooldown;
	private int sethabilityCooldown;
	private boolean canUseSkill;
	private final Main main;
	private boolean schedulerActivated = false;
	private boolean autoHabilitySchedulerActivated = false;

	private DragonPhase dragonPhase = DragonPhase.INITIAL;

	public TnTDragon(Main m) {
		main = m;
		sethabilityCooldown = habilityCooldown;
		autoHabilityCooldown = habilityCooldown;
		canUseSkill = false;
	}

	@EventHandler
	public void onSpawn(CreatureSpawnEvent event) {
		Entity entity = event.getEntity();
		if (!entity.getType().equals(EntityType.ENDER_DRAGON)) {
			return;
		}
		EnderDragon enderDragon = (EnderDragon) entity;
		enderDragon.setCustomName(Formatter.FText("&cTnT Dragon", true));
		enderDragon.setCustomNameVisible(true);
		enderDragon.setGlowing(true);
		Objects.requireNonNull(enderDragon.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(maxHealth);
		enderDragon.setHealth(maxHealth);
		enderDragon.setMaximumNoDamageTicks(0);
		enderDragon.setNoDamageTicks(0);
		if (!schedulerActivated) {
			new BukkitRunnable() {
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
				public void run() {
					Main.debug(Integer.toString(autoHabilityCooldown));
					if (autoHabilityCooldown > 0) {
						autoHabilityCooldown--;
					} else if (autoHabilityCooldown == 0) {
						Player[] playerList = Bukkit.getOnlinePlayers().toArray(new Player[0]);
						Player selectedTarget = playerList[(int) ((Math.random() * playerList.length) + 1)];
						Arrow arrow = (Arrow) selectedTarget.getWorld()
								.spawnEntity(new Location(selectedTarget.getWorld(), 0, 500, 0), EntityType.ARROW);
						arrow.setShooter(selectedTarget);
						onDamage(new EntityDamageByEntityEvent(arrow, enderDragon, EntityDamageEvent.DamageCause.CUSTOM,
								0));
						arrow.remove();
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
		Main.debug("EntityDamageByEntityEvent");
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

		EnderDragon enderDragon = (EnderDragon) entity;
		Main.debug("Vida del dragon: " + enderDragon.getHealth());
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
			habilityCooldown = DragonPhase.FINAL.getSkillCooldown();
		}

		Main.debug("Skill number: " + skillNumber);

		switch (skillNumber) {
		case 1:
			if (!damager.getType().equals(EntityType.PLAYER)) {
				sethabilityCooldown = habilityCooldown;
				Location dragonLocation = enderDragon.getLocation();
				assert player != null;
				Location playerLocation = player.getLocation();
				Vector direction = VectorMath.directionVector(playerLocation, dragonLocation);
				enderDragon.teleport(enderDragon.getLocation().setDirection(direction));
				dragonLocation = enderDragon.getLocation();
				Location finalDragonLocation = dragonLocation;
				new DragonSkill1(main).Skill1(finalDragonLocation, playerLocation);
			}
			return;
		case 2:
			if (canUseSkill) {
				canUseSkill = false;
				sethabilityCooldown = habilityCooldown;
				assert player != null;
				new DragonSkill2(main).Skill2(player.getLocation());
			}
			return;
		case 3:
			if (canUseSkill) {
				canUseSkill = false;
				sethabilityCooldown = habilityCooldown;
				new DragonSkill3(main).Skill3(enderDragon);
			}
			return;
		case 4:
			if (canUseSkill) {
				canUseSkill = false;
				sethabilityCooldown = habilityCooldown;
				new DragonSkill4(main).Skill4();
			}
			return;
		case 5:
			if (canUseSkill) {
				canUseSkill = false;
				sethabilityCooldown = habilityCooldown;
				new DragonSkill5(main).Skill5();
			}
			return;
		case 6:
			if (canUseSkill) {
				canUseSkill = false;
				sethabilityCooldown = habilityCooldown;
				new DragonSkill6(main).Skill6();
			}
			return;
		case 7:
			if (canUseSkill) {
				canUseSkill = false;
				sethabilityCooldown = habilityCooldown;
				new DragonSkill7(main).Skill7();
			}
			return;
		case 8:
			if (canUseSkill) {
				canUseSkill = false;
				sethabilityCooldown = habilityCooldown;
				new DragonSkill8(main).Skill8();
			}
			return;
		case 9:
			if (canUseSkill) {
				canUseSkill = false;
				sethabilityCooldown = habilityCooldown;
				new DragonSkill9(main).Skill9();
			}
			return;
		case 10:
			if (canUseSkill) {
				canUseSkill = false;
				sethabilityCooldown = habilityCooldown;
				new DragonSkill10(main).Skill10();
			}
			return;
		default:
			Main.consoleMsg(Formatter.FText("&cError al intentar usar habilidades del Dragon de TnT"));
		}
	}

	public enum DragonPhase {
		INITIAL(3500, 15), MID(2000, 10), END(1000, 5), FINAL(500, 0);

		int health;
		int skillCooldown;

		DragonPhase(int health, int skillCooldown) {
			this.health = health;
			this.skillCooldown = skillCooldown;
		}

		public int getHealth() {
			return health;
		}

		public int getSkillCooldown() {
			return skillCooldown;
		}
	}
}
