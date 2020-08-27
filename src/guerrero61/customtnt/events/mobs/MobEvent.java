package guerrero61.customtnt.events.mobs;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.events.EventClass;
import guerrero61.customtnt.mainutils.Formatter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MobEvent extends EventClass implements Listener {

    protected EntityType entityType;
    protected int multiply;
    protected int lootMultiply;

    protected List<Entity> spawnedMob;
    protected int currentEntityCount;
    protected int oldEntityCount;

    public MobEvent(Main main) {
        super(main);
    }

    @EventHandler
    public void mobSpawnEvent(EntitySpawnEvent event) {
        if(event.getEntity().getType().equals(entityType)) {
            if(multiply > 0) {
                for (int i = 0; i < (multiply - 1); i++) {
                    int x = Main.random(0, 2);
                    int y = 0;
                    int z = Main.random(0, 2);
                    Entity entity = event.getEntity().getLocation().getWorld().spawnEntity(
                            event.getEntity().getLocation().add(x, y, z), entityType);
                    entity.setCustomName(Formatter.FText("&6&lMob Extra"));
                    entity.setCustomNameVisible(true);
                    spawnedMob.add(entity);
                }
                currentEntityCount = 0;
                oldEntityCount = spawnedMob.size();
            } else {
                event.getEntity().remove();
            }
        }
    }

    @EventHandler
    public void mobDeadEvent(EntityDeathEvent event) {
        if(event.getEntity().getType().equals(entityType)) {
            for (ItemStack itemStack : event.getDrops()) {
                if(itemStack.getMaxStackSize() >= itemStack.getAmount() * lootMultiply) {
                    itemStack.setAmount(itemStack.getAmount() * lootMultiply);
                }
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
