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

    private final Main main;
    private final ConfigClass configClass;
    private final String fileName = "events";

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
            switch (event.eventType) {
                case Mobs: {
                    /*if (entityType == null || mobMultiply < 0 || mobLootMultiply < 0) {
                        Bukkit.broadcastMessage(Formatter.FText("&c&lError al crear evento"));
                    }
                    //MobEvent mobEvent = new MobEvent(entityType, mobMultiply, mobLootMultiply);
                    //new EventTimer(this, configClass, mobEvent, eventName).runTaskTimer(main, 0, 20);*/
                }
                case Block: {

                }
            }
            BossBar bossBar = Bukkit.createBossBar(new NamespacedKey(main, eventName.replace(" ", "_")), Formatter
                    .FText(eventName, true), color, style);
            for (Player player : Bukkit.getOnlinePlayers()) {
                bossBar.addPlayer(player);
            }
            configClass.set("events." + eventName + ".time", timeInSeconds);
        }
    }

    protected void deleteEvent(String eventName) {
        BossBar bossBar = Bukkit.getBossBar(new NamespacedKey(main, eventName.replace(" ", "_")));
        assert bossBar != null;
        for (Player player : bossBar.getPlayers()) {
            bossBar.removePlayer(player);
        }
        bossBar.setVisible(false);
        Bukkit.removeBossBar(new NamespacedKey(main, eventName.replace(" ", "_")));
        configClass.set("events." + eventName + ".time", (String) null);
    }


    public static class Event {
        public EventType eventType;

        public EntityType entityType;
        public int multiply;
        public int lootMultiply;

        public Material blockType;

        public boolean cancel;

        public Event(EntityType entityType) {
            this.eventType = EventType.Mobs;
            this.entityType = entityType;
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
}
