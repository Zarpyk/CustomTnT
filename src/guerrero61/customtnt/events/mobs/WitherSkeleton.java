package guerrero61.customtnt.events.mobs;

import guerrero61.customtnt.Main;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntitySpawnEvent;

public class WitherSkeleton extends MobEvent {

    public WitherSkeleton(Main main, EntityType entityType, int multiply, int lootMultiply) {
        super(main);
        this.entityType = entityType;
        this.multiply = multiply;
        this.lootMultiply = lootMultiply;
    }

    @Override
    public void mobSpawnEvent(EntitySpawnEvent event) {
        super.mobSpawnEvent(event);
    }
}
