package guerrero61.customtnt.events.mobs;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.events.EventTimer;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntitySpawnEvent;

public class WitherSkeleton extends MobEvent {


    public WitherSkeleton(Main main) {
        super(main);
    }

    @Override
    public void mobSpawnEvent(EntitySpawnEvent event) {
        super.mobSpawnEvent(event);
        if (event.getEntity().getType().equals(entityType)) {
            if (currentEntityCount != oldEntityCount) {
                for (Entity witherSkeleton : spawnedMob) {
                    ((org.bukkit.entity.WitherSkeleton) witherSkeleton).setRemoveWhenFarAway(true);
                }
                oldEntityCount = currentEntityCount;
            }
        }
    }

    @Override
    public void createEvent(String eventName, BarColor color, BarStyle style, Event event, int timeInSeconds) {
        super.createEvent(eventName, color, style, event, timeInSeconds);
        this.entityType = event.entityType;
        this.multiply = event.multiply;
        this.lootMultiply = event.lootMultiply;
        EventTimer eventTimer = new EventTimer(this, configClass, this, eventName);
        Main.getPlugin().getServer().getPluginManager().registerEvents(eventTimer, Main.getPlugin());
        eventTimer.runTaskTimer(main, 0, 20);
    }
}
