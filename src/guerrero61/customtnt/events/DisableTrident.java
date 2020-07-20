package guerrero61.customtnt.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class DisableTrident implements Listener {

	@EventHandler
	public void onDeath(EntityDeathEvent e) {
		for (ItemStack drop : e.getDrops()) {
			if (drop.equals(new ItemStack(Material.TRIDENT))) {
				e.setCancelled(true);
			}
		}
	}

}
