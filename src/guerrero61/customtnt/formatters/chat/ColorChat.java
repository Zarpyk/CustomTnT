package guerrero61.customtnt.formatters.chat;

import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mainutils.config.Config;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ColorChat implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerChat(AsyncPlayerChatEvent event) {
        //https://minecraft.tools/en/json_text.php?json=Welcome%20to%20Minecraft%20Tools
        //https://www.spigotmc.org/threads/issue-with-1-16-1-and-packets.447432/#post-3906291
        //https://www.spigotmc.org/threads/issue-with-1-16-1-and-packets.447432/#post-3852712
        String message = event.getMessage();
        event.setFormat(Config.getString(Config.Options.ChatFormat));
        event.setMessage(Formatter.FText(message, true));
    }
}
