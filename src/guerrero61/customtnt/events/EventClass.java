package guerrero61.customtnt.events;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mainutils.config.ConfigClass;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class EventClass {

    protected final Main main;
    protected final ConfigClass configClass;
    protected final String fileName = "events";

    protected EventClass(Main main) {
        configClass = new ConfigClass(fileName);
        this.main = main;
    }

    protected void createEvent(String eventName, BarColor color, BarStyle style, Event event, int timeInHours, int timeInMinutes, int timeInSeconds) {
        createEvent(eventName, color, style, event, (timeInHours * 60) + timeInMinutes, timeInSeconds);
    }

    protected void createEvent(String eventName, BarColor color, BarStyle style, Event event, int timeInMinutes, int timeInSeconds) {
        createEvent(eventName, color, style, event, (timeInMinutes * 60) + timeInSeconds);
    }

    protected void createEvent(String eventName, BarColor color, BarStyle style, Event event, int timeInSeconds) {
        if (!event.cancel) {
            BossBar bossBar = Bukkit.createBossBar(new NamespacedKey(main, eventNameFormatter(eventName)), Formatter
                    .FText(eventName, true), color, style);
            for (Player player : Bukkit.getOnlinePlayers()) {
                bossBar.addPlayer(player);
            }
            configClass.set("event." + eventNameFormatter(eventName) + ".time", timeInSeconds);
        }
    }

    protected void deleteEvent(String eventName) {
        BossBar bossBar = Bukkit
                .getBossBar(new NamespacedKey(main, eventNameFormatter(eventName)));
        assert bossBar != null;
        for (Player player : bossBar.getPlayers()) {
            bossBar.removePlayer(player);
        }
        bossBar.setVisible(false);
        Bukkit.removeBossBar(new NamespacedKey(main, eventNameFormatter(eventName)));
        configClass.set("event." + eventNameFormatter(eventName) + ".time", (String) null);
    }


    public static class Event {
        public EventType eventType;

        public EntityType entityType;
        public int multiply;
        public int lootMultiply;

        public Material blockType;

        public boolean cancel;

        public Event(EntityType entityType, int multiply, int lootMultiply) {
            this.eventType = EventType.Mobs;
            this.entityType = entityType;
            this.multiply = multiply;
            this.lootMultiply = lootMultiply;
        }

        public Event(Material material) {
            if (!material.isBlock()) {
                Main.consoleMsg(Formatter.FText("&c&lERROR, El material indicado no es un bloque"));
                cancel = true;
            } else {
                this.eventType = EventType.Block;
                this.blockType = material;
            }
        }
    }

    public enum EventType {
        Mobs, Block
    }

    public static String eventNameFormatter(String eventName) {
        String pattern = "(?![a-z0-9/._-]).";
        return Formatter.RemoveFormat(eventName.toLowerCase()).replace(" ", "_").replaceAll(pattern, "");
    }
}
