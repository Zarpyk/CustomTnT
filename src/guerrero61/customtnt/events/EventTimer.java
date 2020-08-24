package guerrero61.customtnt.events;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.mainutils.config.ConfigClass;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class EventTimer extends BukkitRunnable implements Listener {

    private final EventClass eventClass;
    private final ConfigClass configClass;
    private final Listener listener;

    private double timer;
    private final String eventName;
    private double seconds;

    public EventTimer(EventClass eventClass, ConfigClass configClass, Listener listener, String eventName) {
        this.eventClass = eventClass;
        this.configClass = configClass;
        this.listener = listener;
        this.eventName = eventName;
        Main.getPlugin().getServer().getPluginManager().registerEvents(listener, Main.getPlugin());
        Main.getPlugin().getServer().getPluginManager().registerEvents(this, Main.getPlugin());
    }

    @Override
    public void run() {
        seconds = configClass.getDouble(eventName + ".time");
        if (timer >= seconds) {
            eventClass.deleteEvent(eventName);
            HandlerList.unregisterAll(this);
            HandlerList.unregisterAll(listener);
        }
        Objects.requireNonNull(Bukkit.getBossBar(new NamespacedKey(Main.getPlugin(), eventName.replace(" ", "_"))))
                .setProgress((seconds - timer) / seconds);
        timer++;
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        configClass.getConfigurationSection("");
    }
}
