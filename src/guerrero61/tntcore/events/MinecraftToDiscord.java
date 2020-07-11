package guerrero61.tntcore.events;

import guerrero61.tntcore.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MinecraftToDiscord implements Listener {

    private final Main main;
    private final FileConfiguration config;

    public MinecraftToDiscord(Main m) {
        main = m;
        config = main.getConfig();
    }

    @EventHandler
    public void playerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();


    }

}
