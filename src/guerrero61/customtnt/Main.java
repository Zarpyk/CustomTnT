package guerrero61.customtnt;

import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SplittableRandom;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import guerrero61.customtnt.MainUtils.*;
import guerrero61.customtnt.MainUtils.Config.Config;
import guerrero61.customtnt.MainUtils.Config.DiscordConfig;
import guerrero61.customtnt.MainUtils.Config.MessagesConfig;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.luckperms.api.LuckPerms;
import net.md_5.bungee.api.ChatColor;

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

	private static final String[] allowIP = new String[] { "***REMOVED***", "0.0.0.0", "192.168.0.173",
			"***REMOVED***" };

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
			if (api == null) {
				registerEvents.registerDiscord(this);
			}
			registerEvents.registerDependencies(this);
			registerEvents.registerEvents(this);
			registerEvents.registerCommands(this);

			if (Config.getBool(Config.Options.StormActionBarEnable)) {
				new StormActionBar().StormAB(this);
			}
			Scheduler scheduler = new Scheduler();
			scheduler.startMessageDelayScheduler(this);
			scheduler.reloadStatusScheduler(this);
			//scheduler.registerDependencies(this);
		} else {
			Bukkit.getPluginManager().disablePlugin(this);
		}
		Main.prefix = Config.getString(Config.Options.Prefix);
	}

	public void onDisable() {
		ReloadStatus.startStopToDiscord("https://imgur.com/Ilu3YmV.png", api,
				Config.getString(Config.Options.MessagesStop), new Color(255, 10, 10), "offline");
		try {
			DisableBot.Disable(this);
		} catch (NoClassDefFoundError e) {
			e.printStackTrace();
		}
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

	public static void consoleMsg(String text) {
		Bukkit.getConsoleSender().sendMessage(text);
	}

	public static void debug(String text) {
		if (Config.getBool(Config.Options.DebugMode)) {
			consoleMsg(Formatter.FText("&5[&bDEBUG&5] &e" + text));
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
		if (!msg.getContentDisplay().toLowerCase().startsWith("/" + command.toLowerCase())) {
			return true;
		}

		List<String> channels = Config.getStringList(Config.Options.ChannelsCommands);
		for (String channel : channels) {
			if (mChannel.getId().equals(channel)) {
				return false;
			}
		}
		return true;
	}

	public static Boolean checkCommand(String command, String command2, Message msg, MessageChannel mChannel) {
		if (!msg.getContentDisplay().toLowerCase().startsWith("/" + command.toLowerCase())
				&& !msg.getContentDisplay().toLowerCase().startsWith("/" + command2.toLowerCase())) {
			return true;
		}

		List<String> channels = Config.getStringList(Config.Options.ChannelsCommands);
		for (String channel : channels) {
			if (mChannel.getId().equals(channel)) {
				return false;
			}
		}
		return true;
	}

	public static int random(int a, int b) {
		return new SplittableRandom().nextInt(a, b + 1);
	}
}
