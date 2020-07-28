package guerrero61.customtnt.extensions.mmoitems;

import java.util.Objects;

import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import guerrero61.customtnt.mainutils.config.Config;
import net.Indyuce.mmoitems.stat.type.ItemStat;
import net.mmogroup.mmolib.api.item.NBTItem;

public class DisableCustomRepair implements Listener {

	@EventHandler
	public void getResult(InventoryClickEvent event) {
		if (Config.getBool(Config.Options.MMOItemsDisableCutomsRepair))
			if (event.getInventory().getType().equals(InventoryType.ANVIL) && event.getSlot() == 2) {
				ItemStack item1 = event.getInventory().getItem(0);
				ItemStack item2 = event.getInventory().getItem(1);
				ItemStack result = event.getInventory().getItem(2);
				if (item1 == null || result == null)
					return;
				if (NBTItem.get(item1).hasType() && ((Objects.requireNonNull(item2).getType() != Material.ENCHANTED_BOOK
						|| NBTItem.get(item1).getStat(ItemStat.DISABLE_ENCHANTING) == 1.0D))) {
					event.setResult(Event.Result.DENY);
				}
			}

		/*if (event.getInventory().getType().equals(InventoryType.ANVIL) && event.getSlot() == 2) {
			ItemStack item1 = event.getInventory().getItem(0);
			ItemStack item2 = event.getInventory().getItem(1);
			ItemStack result = event.getInventory().getItem(2);
			if (item1 == null || result == null)
				return;
		
			boolean item2Condition;
			boolean disableEnchantCondition;
			boolean disableRepairCondition;
			if (item2 != null) {
				item2Condition = item2.getType() != Material.ENCHANTED_BOOK;
				if (NBTItem.get(item2).hasType()) {
					item2Condition = item2Condition && NBTItem.get(item1) != NBTItem.get(item2);
				}
				disableEnchantCondition = NBTItem.get(item1).getStat(ItemStat.DISABLE_ENCHANTING) == 1.0;
				disableRepairCondition = NBTItem.get(item1).getStat(ItemStat.DISABLE_REPAIRING) == 1.0;
			} else {
				item2Condition = false;
				disableEnchantCondition = false;
				disableRepairCondition = false;
			}
			if (NBTItem.get(item1).hasType()
					&& ((item2Condition && disableRepairCondition) || disableEnchantCondition)) {
				event.setCancelled(true);
			}
		}*/
	}
}
