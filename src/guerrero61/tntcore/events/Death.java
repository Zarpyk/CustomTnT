package guerrero61.tntcore.events;

import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import guerrero61.tntcore.Main;

public class Death implements Listener{
	
	private Main main;
	private FileConfiguration config;
	private long addStormSeconds;
	private long stormHours;
	private String world;
	
	public Death(Main m) {
		main = m;
		config = main.getConfig();
		addStormSeconds = Long.parseLong(config.getString("Storm.AddSeconds"));
		stormHours = addStormSeconds / 60L / 60L;
		world = config.getString("MainWorld");
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		//Codigo modificado de PermadeathCore por vo1d & SebazCRC
	    boolean weather = Bukkit.getWorld(Objects.<String>requireNonNull(world)).hasStorm();
	    String victim = e.getEntity().getPlayer().getName();
	    Player killer = e.getEntity().getKiller();
	    String DeathChatMessage = config.getString("Messages.Death.msg");
	    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', DeathChatMessage.replace("%player%", victim)));
	    if (config.getBoolean("Messages.Death.coords-enable")) {
	    	String CoordsMessage = config.getString("Messages.Death.coords-msg");
		    String Dx = Integer.toString(e.getEntity().getPlayer().getLocation().getBlockX());
		    String Dy = Integer.toString(e.getEntity().getPlayer().getLocation().getBlockY());
		    String Dz = Integer.toString(e.getEntity().getPlayer().getLocation().getBlockZ());
	    	Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',CoordsMessage.replace("%x%", Dx).replace("%y%", Dy).replace("%z%", Dz))); 
	    }
	    int stormDuration = Bukkit.getWorld(Objects.<String>requireNonNull(world)).getWeatherDuration();
	    int stormTicksToSeconds = stormDuration / 20;
	    long stormIncrement = stormTicksToSeconds + this.addStormSeconds;
	    int intsTicks = (int)this.addStormSeconds;
	    int inc = (int)stormIncrement;
	    if (weather) {
	      Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), "minecraft:weather thunder");
	      Bukkit.getWorld(Objects.<String>requireNonNull(world)).setWeatherDuration(inc * 20);
	    } else {
	      Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), "minecraft:weather thunder");
	      Bukkit.getWorld(Objects.<String>requireNonNull(world)).setWeatherDuration(intsTicks * 20);
	    } 
	    BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	    scheduler.scheduleSyncDelayedTask((Plugin)main, new Runnable() {
	          public void run() {
	            String DeathTrainMessage = config.getString("Messages.Death.train");
	            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', DeathTrainMessage.replace("%time%", Float.toString(stormHours))));
	            for (Player player : Bukkit.getOnlinePlayers())
	              player.playSound(player.getLocation(), Sound.ENTITY_SKELETON_HORSE_DEATH, 10.0F, 1.0F); 
	          }
	        },75L);
	    for (Player player : Bukkit.getOnlinePlayers()) {
	      String ServerMessageTitle = config.getString("Messages.Death.title");
	      String ServerMessageSubtitle = config.getString("Messages.Death.subtitle");
	      player.sendTitle(ChatColor.translateAlternateColorCodes('&', ServerMessageTitle), ChatColor.translateAlternateColorCodes('&', ServerMessageSubtitle.replace("%player%", victim)), 10, 70, 20);
	      player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_DEATH, 10.0F, -5.0F);
	    } 
	}
}
