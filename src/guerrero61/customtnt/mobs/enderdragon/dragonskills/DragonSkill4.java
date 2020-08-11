package guerrero61.customtnt.mobs.enderdragon.dragonskills;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mobs.enderdragon.TnTDragon;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DragonSkill4 {

    private final Main main;
    public static String skillName = "Mob Random";
    public static String[] mobNames = new String[]{"&6Esbirro del Dragon", "&6Soldado del Dragon", "&6Esclavo del Dragon", "&6Guerrero del Dragon", "&6Arma del Dragon", "&6Hijo del Dragon"};
    public static int minMob = 3;
    public static int maxMob = 10;

    public DragonSkill4(Main m) {
        main = m;
    }

    public void Skill4(Player player) {
        Main.debug("Skill 4");
        player.sendMessage(Formatter
                .FText(TnTDragon.dragonName + " &4&lha usado la habilidad &2&l" + skillName + " &4&len ti."));
        int random = Main.random(minMob, maxMob);
        for (int i = 0; i < random; i++) {
            try {
                int x = Main.random(0, 3);
                int y = Main.random(0, 3);
                int z = Main.random(0, 3);
                Entity entity = player.getLocation().getWorld()
                        .spawnEntity(player.getLocation().add(x, y, z), EntityType.valueOf(randomMob()));
                entity.setGlowing(true);
                int randomName = Main.random(0, mobNames.length - 1);
                entity.setCustomName(Formatter.FText(mobNames[randomName], true, player));
                entity.setCustomNameVisible(true);
            } catch (Exception e) {
                Bukkit.broadcastMessage(Formatter.FText("&cHubo un error al invocar a: " + randomMob()));
            }
        }
    }

    private String randomMob() {
        List<String> mobList = new ArrayList<>(List
                .of("BLAZE",
                        "CAVE_SPIDER",
                        "CREEPER",
                        "DROWNED",
                        "ENDERMAN",
                        "ENDERMITE",
                        "EVOKER",
                        "GHAST",
                        "HOGLIN",
                        "HUSK",
                        "MAGMA_CUBE",
                        "PHANTOM",
                        "PIGLIN",
                        "PILLAGER",
                        "RAVAGER",
                        "SILVERFISH",
                        "SKELETON",
                        "SLIME",
                        "SPIDER",
                        "STRAY",
                        "VEX",
                        "VINDICATOR",
                        "WITCH",
                        "WITHER_SKELETON",
                        "ZOMBIE",
                        "ZOGLIN",
                        "ZOMBIFIED_PIGLIN"));
        int random = Main.random(0, mobList.size() - 1);
        return mobList.get(random);
    }
}
