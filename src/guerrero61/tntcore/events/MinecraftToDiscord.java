package guerrero61.tntcore.events;

import java.util.ArrayList;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import guerrero61.tntcore.Main;

public class MinecraftToDiscord implements Listener {

	private Main main;
	private FileConfiguration config;
	private String prefix;
	ArrayList<Player> sleeping = new ArrayList<>();
	ArrayList<Player> globalSleeping = new ArrayList<>();
	private Boolean executed = false;
	private String world;

	public MinecraftToDiscord(Main m) {
		main = m;
		config = main.getConfig();
		prefix = config.getString("Prefix");
		world = config.getString("MainWorld");
	}
}
