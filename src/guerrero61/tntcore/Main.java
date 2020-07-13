package guerrero61.tntcore;

import java.util.List;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import guerrero61.tntcore.MainUtils.DisableBot;
import guerrero61.tntcore.MainUtils.RegisterEvents;
import guerrero61.tntcore.MainUtils.StormActionBar;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

public class Main extends JavaPlugin {

	PluginDescriptionFile pInfo = getDescription();
	public String name = pInfo.getName();
	public String version = pInfo.getVersion();

	public String startMessage = ChatColor.GREEN + name + " " + version + ": Se ha activado.";
	public String stopMessage = ChatColor.RED + name + " " + version + ": Se ha desactivado.";

	public String configPath;
	public static FileConfiguration config;
	public static String prefix;

	public static String[] allowIP = new String[] { "***REMOVED***", "0.0.0.0" };

	public JDA api;

	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage(startMessage);
		if (CheckDisablePlugin()) {
			RegisterEvents.registerConfig(this);
			RegisterEvents.registerEvents(this);
			RegisterEvents.registerCommands(this);
			RegisterEvents.registerDiscord(this);
			StormActionBar.StormAB(this);
		}
	}

	public void onDisable() {
		DisableBot.Disable(this, api);
		Bukkit.getConsoleSender().sendMessage(stopMessage);
	}

	public static boolean CheckDisablePlugin() {
		String IP = Bukkit.getServer().getIp();
		for (String s : allowIP) {
			if (IP.equals(s)) {
				return true;
			}
		}
		return false;
	}

	public static String FText(String text) {
		return ChatColor.translateAlternateColorCodes('&', prefix + text);
	}

	public static String FTextNPrefix(String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}

	public static String getString(String configOption) {
		return config.getString(configOption);
	}

	public static List<String> getStringList(String configOption) {
		return config.getStringList(configOption);
	}

	public static Integer getInt(String configOption) {
		return config.getInt(configOption);
	}

	public static Float getFloat(String configOption) {
		return Float.parseFloat(Objects.requireNonNull(config.getString(configOption)));
	}

	public static Boolean getBool(String configOption) {
		return config.getBoolean(configOption);
	}

	public static Boolean checkCommand(String command, Message msg, MessageChannel mChannel) {
		if (!msg.getContentDisplay().equalsIgnoreCase("/" + command)) {
			return true;
		}

		List<String> channels = Main.getStringList("Discord.command-channel");
		for (String channel : channels) {
			if (mChannel.getId().equals(channel)) {
				return false;
			}
		}
		return true;
	}

	public static Boolean checkCommand(String command, String command2, Message msg, MessageChannel mChannel) {
		if (!msg.getContentDisplay().equalsIgnoreCase("/" + command)
				&& !msg.getContentDisplay().equalsIgnoreCase("/" + command2)) {
			return true;
		}

		List<String> channels = Main.getStringList("Discord.command-channel");
		for (String channel : channels) {
			if (mChannel.getId().equals(channel)) {
				return false;
			}
		}
		return true;
	}
}
