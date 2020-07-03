package guerrero61.tntcore.events;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;

import guerrero61.tntcore.Main;

public class Totem implements Listener {

	private Main main;
	private FileConfiguration config;
	private String prefix;

	public Totem(Main m) {
		main = m;
		config = main.getConfig();
		prefix = config.getString("Prefix");
	}

	@EventHandler
	public void totemNerf(EntityResurrectEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		if (((Player) event.getEntity()).getInventory().getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING
				|| ((Player) event.getEntity()).getInventory().getItemInOffHand()
						.getType() == Material.TOTEM_OF_UNDYING) {
			if (!config.getBoolean("Totem.fail-enable"))
				return;
			Player p = (Player) event.getEntity();
			String player = p.getName();
			int failProb = Integer.parseInt(config.getString("Totem.probability"));
			String totemFail = Objects.<String>requireNonNull(config.getString("Totem.msg-fail"));
			String totemMessage = Objects.<String>requireNonNull(config.getString("Totem.msg-used-totem"));
			if (failProb >= 101)
				failProb = 100;
			if (failProb < 0)
				failProb = 1;
			if (failProb == 100) {
				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
						prefix + totemMessage.replace("%player%", player).replace("%porcent%", "=")
								.replace("%totem_fail%", String.valueOf(100))
								.replace("%number%", String.valueOf(failProb))));
				Bukkit.broadcastMessage(
						ChatColor.translateAlternateColorCodes('&', prefix + totemFail.replace("%player%", player)));
				event.setCancelled(true);
			} else {
				int random = (new Random()).nextInt(99);
				random++;
				int resta = 100 - failProb;
				if (random > resta) {
					Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
							prefix + totemMessage.replace("%player%", player).replace("%porcent%", "=>")
									.replace("%totem_fail%", String.valueOf(random))
									.replace("%number%", String.valueOf(resta))));
					Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
							prefix + totemFail.replace("%player%", player)));
					event.setCancelled(true);
				} else {
					Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
							prefix + totemMessage.replace("%player%", player).replace("%porcent%", "!=")
									.replace("%totem_fail%", String.valueOf(random))
									.replace("%number%", String.valueOf(resta))));
				}
			}
		}
	}
}
