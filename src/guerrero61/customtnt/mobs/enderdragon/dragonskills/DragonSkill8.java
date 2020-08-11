package guerrero61.customtnt.mobs.enderdragon.dragonskills;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mobs.enderdragon.TnTDragon;
import net.minecraft.server.v1_16_R1.DamageSource;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;

public class DragonSkill8 {

    private final Main main;
    public static String skillName = "Chupa Sangre";
    public static float percentage = 25;
    public static double multiply = 3;
    private float damage;

    public DragonSkill8(Main m) {
        main = m;
    }

    public void Skill8(Player player) {
        Main.debug("Skill 8");
        player.sendMessage(Formatter
                .FText(TnTDragon.dragonName + " &4&lha usado la habilidad &2&l" + skillName + " &4&len ti."));
        damage = (float) (player.getHealth() * (percentage / 100));
        Main.debug(Float.toString(damage));
        player.setHealth(player.getHealth() - damage);
        ((CraftPlayer) player).getHandle().damageEntity(DamageSource.MAGIC, 0);
    }

    public void Skill8(Player player, EnderDragon enderDragon) {
        Skill8(player);
        enderDragon.setHealth(enderDragon.getHealth() + (damage * multiply));
    }
}
