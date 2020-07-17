package guerrero61.customtnt;

import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import guerrero61.customtnt.MainUtils.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.luckperms.api.LuckPerms;

public class Main extends JavaPlugin {

	PluginDescriptionFile pInfo = getDescription();
	public String name = pInfo.getName();
	public String version = pInfo.getVersion();

	public String startMessage = ChatColor.GREEN + name + " " + version + ": Se ha activado.";
	public String stopMessage = ChatColor.RED + name + " " + version + ": Se ha desactivado.";

	public String configPath;
	public static FileConfiguration config;
	public FileConfiguration messagesConfig;
	public FileConfiguration discordConfig;
	public File messagesConfigFile;
	public File discordConfigFile;
	public static Map<Config.CONFIG, FileConfiguration> configMap;

	public static String prefix;

	private static final String[] allowIP = new String[] { "***REMOVED***", "0.0.0.0" };

	public JDA api;
	public LuckPerms lpApi;

	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage(startMessage);
		if (CheckDisablePlugin()) {
			configMap = new HashMap<>();
			RegisterEvents registerEvents = new RegisterEvents();
			registerEvents.registerConfig(this);
			configMap.put(Config.CONFIG.Main, config);
			registerEvents.registerMessagesConfig(this);
			configMap.put(Config.CONFIG.Messages,
					messagesConfig == null ? MessagesConfig.getMessagesConfig(this) : messagesConfig);
			registerEvents.registerDiscordConfig(this);
			configMap.put(Config.CONFIG.Discord,
					discordConfig == null ? DiscordConfig.getDiscordConfig(this) : discordConfig);
			consoleMsg(discordConfig.toString());
			registerEvents.registerDiscord(this);
			lpApi = registerEvents.registerLuckPerms();
			registerEvents.registerEvents(this);
			registerEvents.registerCommands(this);

			new StormActionBar().StormAB(this);
			Scheduler scheduler = new Scheduler();
			scheduler.startMessageDelayScheduler(this);
			scheduler.reloadStatusScheduler(this);
		} else {
			Bukkit.getPluginManager().disablePlugin(this);
		}
		Main.prefix = Config.getString("Prefix", Config.CONFIG.Messages);
	}

	public void onDisable() {
		ReloadStatus.startStopToDiscord("https://imgur.com/Ilu3YmV.png", api,
				Config.getString("Messages.stop-msg", Config.CONFIG.Discord), new Color(255, 10, 10), "offline");
		DisableBot.Disable(this, api);
		api = null;
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

	public static String capitalize(String str) {
		if (str == null || str.isEmpty()) {
			return str;
		}
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	public static String removeFormatter(String text) {
		String t = text.replace("§", "&");
		return t.replace("&a", "").replace("&b", "").replace("&c", "").replace("&d", "").replace("&e", "")
				.replace("&f", "").replace("&1", "").replace("&2", "").replace("&3", "").replace("&4", "")
				.replace("&5", "").replace("&6", "").replace("&7", "").replace("&8", "").replace("&9", "")
				.replace("&0", "").replace("&k", "").replace("&l", "").replace("&m", "").replace("&n", "")
				.replace("&o", "").replace("&r", "");
	}

	public static void consoleMsg(String text) {
		Bukkit.getConsoleSender().sendMessage(FText(text));
	}

	public static void consoleMsg(String text, Boolean nPrefix) {
		if (nPrefix) {
			Bukkit.getConsoleSender().sendMessage(FTextNPrefix(text));
		} else {
			consoleMsg(text);
		}
	}

	public static String getIp() {
		return Bukkit.getServer().getIp()
				+ (Bukkit.getServer().getPort() == 25565 ? "" : ":" + Bukkit.getServer().getPort());
	}

	public static String getPlayerCount() {
		return "Jugadores " + Bukkit.getServer().getOnlinePlayers().size() + "/" + Bukkit.getServer().getMaxPlayers();
	}

	public static boolean isVanish(Player player) {
		for (MetadataValue meta : player.getMetadata("vanished")) {
			if (meta.asBoolean())
				return true;
		}
		return false;
	}

	public static Boolean checkCommand(String command, Message msg, MessageChannel mChannel) {
		if (!msg.getContentDisplay().equalsIgnoreCase("/" + command)) {
			return true;
		}

		List<String> channels = Config.getStringList("Channels.command-channel", Config.CONFIG.Discord);
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

		List<String> channels = Config.getStringList("Channels.command-channel", Config.CONFIG.Discord);
		for (String channel : channels) {
			if (mChannel.getId().equals(channel)) {
				return false;
			}
		}
		return true;
	}
}
