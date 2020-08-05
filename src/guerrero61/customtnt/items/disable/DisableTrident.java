package guerrero61.customtnt.items.disable;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class DisableTrident implements Listener {

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if (e.getEntity().getType() == EntityType.DROWNED && e.getEntity().getCustomName() == null) {
            e.getDrops().removeIf(drop -> drop.equals(new ItemStack(Material.TRIDENT)));
        }
    }

}
