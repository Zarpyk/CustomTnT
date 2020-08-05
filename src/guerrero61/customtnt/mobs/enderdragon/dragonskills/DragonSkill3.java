package guerrero61.customtnt.mobs.enderdragon.dragonskills;

import guerrero61.customtnt.Main;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class DragonSkill3 {

    private final Main main;
    public static String skillName = "La LoCuRa";
    public static int effectDuration = 200;
    public static int soundCount = 35;
    public static int soundExtraTime = 600;

    public DragonSkill3(Main m) {
        main = m;
    }

    public void Skill3(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, effectDuration, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, effectDuration, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, effectDuration, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, effectDuration, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, effectDuration, 3));
        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, effectDuration, 2));
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                player.playSound(player.getLocation(), Sound.valueOf(randomSound()), 1, 1);
            }
        }.runTaskTimer(main, 0, effectDuration / soundCount);
        new BukkitRunnable() {
            @Override
            public void run() {
                task.cancel();
                for (Sound sound : soundList()) {
                    player.stopSound(sound);
                }
            }
        }.runTaskLater(main, effectDuration + soundExtraTime);
        Main.debug("Skill 3");
    }

    private String randomSound() {
        List<String> stringList = new ArrayList<>();
        for (Object object : Sound.class.getEnumConstants()) {
            stringList.add(object.toString());
        }
        int random = Main.random(0, stringList.size() - 1);
        return stringList.get(random);
    }

    private List<Sound> soundList() {
        List<Sound> soundList = new ArrayList<>();
        for (Object object : Sound.class.getEnumConstants()) {
            soundList.add(Sound.valueOf(object.toString()));
        }
        return soundList;
    }
}
