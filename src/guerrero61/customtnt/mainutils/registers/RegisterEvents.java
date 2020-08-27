package guerrero61.customtnt.mainutils.registers;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.death.Death;
import guerrero61.customtnt.death.Sleep;
import guerrero61.customtnt.death.Totem;
import guerrero61.customtnt.death.Weather;
import guerrero61.customtnt.discord.minecraft.MinecraftToDiscord;
import guerrero61.customtnt.extensions.mmoitems.DisableCustomRepair;
import guerrero61.customtnt.formatters.tablist.TabList;
import guerrero61.customtnt.items.disable.DisableTrident;
import guerrero61.customtnt.mainutils.config.Config;
import guerrero61.customtnt.mobs.enderdragon.TnTDragon;
import guerrero61.customtnt.mobs.enderdragon.dragonskills.DragonSkill4;
import guerrero61.customtnt.mobs.enderdragon.dragonskills.DragonSkill5;
import guerrero61.customtnt.mobs.enderdragon.dragonskills.DragonSkill7;
import guerrero61.customtnt.mobs.enderdragon.dragonskills.DragonSkill8;
import org.bukkit.plugin.PluginManager;

public class RegisterEvents {

    public RegisterEvents(Main m) {
        PluginManager pm = m.getServer().getPluginManager();
        //Death
        if(Config.getBool(Config.Options.DeathEnable)) {
            pm.registerEvents(new Death(m), m);
            pm.registerEvents(new Sleep(m), m);
            pm.registerEvents(new Weather(m.api), m);
            pm.registerEvents(new Totem(m.api), m);
        }
        //Disable
        if(Config.getBool(Config.Options.DisableTrident)) {
            pm.registerEvents(new DisableTrident(), m);
        }
        //Discord
        if(Config.getBool(Config.Options.DiscordEnable)) {
            pm.registerEvents(new MinecraftToDiscord(m, m.api), m);
        }
        //Formatter
        if(Config.getBool(Config.Options.FormatterEnable)) {
            if(Config.getBool(Config.Options.FormatterTabList)) {
                pm.registerEvents(new TabList(m), m);
            }
            if(Config.getBool(Config.Options.FormatterCustomChat)) {
                //pm.registerEvents(new ColorChat(), m);
            }
        }
        //CustomDragon
        if(Config.getBool(Config.Options.CustomDragonEnable)) {
            pm.registerEvents(new TnTDragon(m), m);
            pm.registerEvents(new DragonSkill4(), m);
            pm.registerEvents(new DragonSkill5(m), m);
            pm.registerEvents(new DragonSkill8(m), m);
            pm.registerEvents(new DragonSkill7(m), m);
        }
        //MMOItems
        if(Config.getBool(Config.Options.MMOItemsEnable)) {
            pm.registerEvents(new DisableCustomRepair(), m);
        }
    }
}
