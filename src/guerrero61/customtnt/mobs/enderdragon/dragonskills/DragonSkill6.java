package guerrero61.customtnt.mobs.enderdragon.dragonskills;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mobs.enderdragon.TnTDragon;
import net.minecraft.world.damagesource.DamageSource;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;

public class DragonSkill6 {

    public static String skillName = "Chupa Sangre";
    public static float percentage = 25;
    public static double multiply = 3;
    private float damage;

    public void Skill6(Player player) {
        Main.debug("Skill 8");
        player.sendMessage(
                Formatter.FText(TnTDragon.dragonName + " &6&lha usado la habilidad &c&l" + skillName + " &6&len ti."));
        damage = (float) (player.getHealth() * (percentage / 100));
        Main.debug(Float.toString(damage));
        player.setHealth(player.getHealth() - damage);
        ((CraftPlayer) player).getHandle().damageEntity(DamageSource.a, 0); //TODO ni idea de que es esto
    }

    public void Skill6(Player player, EnderDragon enderDragon) {
        Skill6(player);
        enderDragon.setHealth(enderDragon.getHealth() + (damage * multiply));
    }
}
