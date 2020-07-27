package guerrero61.customtnt.mainutils.registers;

import org.bukkit.plugin.PluginManager;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.discord.minecraft.MinecraftToDiscord;
import guerrero61.customtnt.events.*;
import guerrero61.customtnt.events.formats.TabList;
import guerrero61.customtnt.mobs.TnTDragon;

public class RegisterEvents {

	public RegisterEvents(Main m) {
		PluginManager pm = m.getServer().getPluginManager();
		pm.registerEvents(new Death(m), m);
		pm.registerEvents(new Sleep(m), m);
		pm.registerEvents(new Weather(m.api), m);
		pm.registerEvents(new Totem(m.api), m);
		pm.registerEvents(new DisableCustomRepair(), m);
		pm.registerEvents(new DisableTrident(), m);
		pm.registerEvents(new MinecraftToDiscord(m, m.api), m);
		pm.registerEvents(new TnTDragon(m), m);
		pm.registerEvents(new TabList(m), m);
		//pm.registerEvents(new ColorChat(), m);
	}
}
