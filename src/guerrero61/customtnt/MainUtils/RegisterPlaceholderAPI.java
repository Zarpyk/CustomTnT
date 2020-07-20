package guerrero61.customtnt.MainUtils;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.events.Death;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class RegisterPlaceholderAPI extends PlaceholderExpansion {

	private final String VERSION = getClass().getPackage().getImplementationVersion();
	private final Main main;

	public RegisterPlaceholderAPI(Main m) {
		main = m;
	}

	@Override
	public String getIdentifier() {
		return "customtnt";
	}

	@Override
	public String getAuthor() {
		return "GuerreroCraft61";
	}

	@Override
	public String getVersion() {
		return VERSION;
	}

	@Override
	public boolean canRegister() {
		return true;
	}

	@Override
	public boolean persist() {
		return true;
	}

	@Override
	public String onRequest(OfflinePlayer p, String params) {
		if (p != null && p.isOnline()) {
			return onPlaceholderRequest(p.getPlayer(), params);
		}
		return null;
	}

	@Override
	public String onPlaceholderRequest(Player player, String params) {
		Main.consoleMsg(params);
		switch (params.toLowerCase()) {
		case "storm_time":
			Main.consoleMsg("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA1");
			return StormActionBar.stormTime;
		case "add_storm_time":
			Main.consoleMsg("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA2");
			return Float.toString(Death.stormHours);
		default:
			Main.consoleMsg("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA3");
			if (player == null) {
				Main.consoleMsg("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA4");
				return null;
			}
			Main.consoleMsg("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA5");
			return null;
		}
	}
}
