package guerrero61.customtnt.events.mobs;

import org.bukkit.entity.EntityType;

public class Mobs {

    public enum MobType {
        WITHER_SKELETON(EntityType.WITHER_SKELETON), GHAST(EntityType.GHAST);

        public EntityType entityType;

        MobType(EntityType entityType) {
            this.entityType = entityType;
        }

        public EntityType getEntityType() {
            return entityType;
        }
    }
}
