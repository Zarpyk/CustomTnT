package guerrero61.customtnt.mainutils.registers;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.death.Death;
import guerrero61.customtnt.mainutils.StormActionBar;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

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
        if(p != null && p.isOnline()) {
            return onPlaceholderRequest(p.getPlayer(), params);
        }
        return null;
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        Main.consoleMsg(params);
        switch (params.toLowerCase()) {
            case "storm_time":
                return StormActionBar.stormTime;
            case "add_storm_time":
                return Float.toString(Death.stormHours);
            default:
                if(player == null) {
                    return null;
                }
                return null;
        }
    }
}
