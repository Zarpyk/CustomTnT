package guerrero61.customtnt.events.mobs;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.events.EventClass;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;

public class MobEvent extends EventClass implements Listener {

    protected EntityType entityType;
    protected int multiply;
    protected int lootMultiply;

    public MobEvent(Main main) {
        super(main);
    }

    @EventHandler
    public void mobSpawnEvent(EntitySpawnEvent event) {
        if (event.getEntity().getType().equals(entityType)) {
            if (multiply > 0) {
                for (int i = 0; i < (multiply - 1); i++) {
                    int x = Main.random(0, 2);
                    int y = 0;
                    int z = Main.random(0, 2);
                    event.getEntity().getLocation().getWorld()
                            .spawnEntity(event.getEntity().getLocation().add(x, y, z), entityType);
                }
            } else {
                event.getEntity().remove();
            }
        }
    }

    @EventHandler
    public void mobDeadEvent(EntityDeathEvent event) {
        if (event.getEntity().getType().equals(entityType)) {
            for (ItemStack itemStack : event.getDrops()) {
                itemStack.setAmount(itemStack.getAmount() * lootMultiply);
            }
        }
    }

    public void setMobMultiply(int mobMultiply) {
        this.multiply = mobMultiply;
    }

    public void setMobLootMultiply(int mobLootMultiply) {
        this.lootMultiply = mobLootMultiply;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

}
