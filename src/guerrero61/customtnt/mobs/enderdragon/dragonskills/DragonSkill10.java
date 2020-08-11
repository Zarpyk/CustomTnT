package guerrero61.customtnt.mobs.enderdragon.dragonskills;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mobs.enderdragon.TnTDragon;
import org.bukkit.Location;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class DragonSkill10 {

    private final Main main;
    public static String skillName = "Dragon Ultimate";

    public DragonSkill10(Main m) {
        main = m;
    }

    public void Skill10(EnderDragon enderDragon, Player player) {
        player.sendMessage(Formatter
                .FText(TnTDragon.dragonName + " &4&lha usado la habilidad &2&l" + skillName));
        int random = Main.random(1, 3);
        for (int i = 0; i < random; i++) {
            int randomHability = Main.random(1, 10);
            RandomHability(randomHability, enderDragon, player);
        }
    }

    public void Skill10(Location location, Player player) {
        int random = Main.random(1, 3);
        for (int i = 0; i < random; i++) {
            int randomHability = Main.random(1, 10);
            RandomHability(randomHability, location, player);
        }
    }

    public void RandomHability(int habilityNumber, EnderDragon enderDragon, Player player) {
        switch (habilityNumber) {
            case 1:
                new DragonSkill1(main).Skill1(enderDragon.getLocation(), player);
                break;
            case 2:
                new DragonSkill2(main).Skill2(player);
                break;
            case 3:
                new DragonSkill3(main).Skill3(player);
                break;
            case 4:
                new DragonSkill4(main).Skill4(player);
                break;
            case 5:
                new DragonSkill5(main).Skill5(player);
                break;
            case 6:
                new DragonSkill6(main).Skill6(enderDragon);
                break;
            case 7:
                new DragonSkill7(main).Skill7(player);
                break;
            case 8:
                new DragonSkill8(main).Skill8(player, enderDragon);
                break;
            case 9:
                new DragonSkill9(main).Skill9(enderDragon);
                break;
            case 10:
                new DragonSkill10(main).Skill10(enderDragon, player);
                break;
            default:
                Main.consoleMsg(Formatter.FText("&cEsta habilidad no existe"));
                break;
        }
    }

    public void RandomHability(int habilityNumber, Location location, Player player) {
        EnderDragon enderDragon;
        enderDragon = (EnderDragon) location.getWorld().spawnEntity(location,
                EntityType.ENDER_DRAGON);
        enderDragon.setAI(false);
        RandomHability(habilityNumber, enderDragon, player);
    }
}
