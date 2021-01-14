package guerrero61.customtnt;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
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

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import guerrero61.customtnt.discord.events.DisableBot;
import guerrero61.customtnt.discord.events.ReloadStatus;
import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mainutils.StormActionBar;
import guerrero61.customtnt.mainutils.config.Config;
import guerrero61.customtnt.mainutils.registers.Registers;
import guerrero61.customtnt.mainutils.registers.Scheduler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.luckperms.api.LuckPerms;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.permission.Permission;

public class Main extends JavaPlugin {

	PluginDescriptionFile pInfo = getDescription();
	public String name = pInfo.getName();
	public String version = pInfo.getVersion();
	private static Main plugin = null;

	public String startMessage = ChatColor.GREEN + name + " " + version + ": Se ha activado.";
	public String stopMessage = ChatColor.RED + name + " " + version + ": Se ha desactivado.";

	public String configPath;
	public static FileConfiguration config;
	public static FileConfiguration messagesConfig;
	public static FileConfiguration discordConfig;
	public File messagesConfigFile;
	public File discordConfigFile;
	public static Map<Config.CONFIG, FileConfiguration> configMap;

	public static String prefix;

	private static final String[] allowIP = new String[] { "***REMOVED***", "0.0.0.0", "***REMOVED***" };

	public JDA api;
	public LuckPerms lpApi;
	public Permission perms = null;
	private Connection skinRestorerConnection;

	@Override
	public void onLoad() {
		plugin = this;
	}

	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage(startMessage);
		configMap = new HashMap<>();
		Registers registers = new Registers();
		registers.registerConfig(this);
		if (api == null && Config.getBool(Config.Options.DiscordEnable)) {
			registers.registerDiscord(this);
		}
		registers.registerDependencies(this);
		registers.registerEvents(this);
		registers.registerCommands(this);
		new StormActionBar().StormAB(this);
		Scheduler scheduler = new Scheduler();
		if (Config.getBool(Config.Options.DiscordEnable)) {
			scheduler.startMessageDelayScheduler(this);
			scheduler.reloadStatusScheduler(this);
		}
		scheduler.registerScoreboard(this);
		//scheduler.registerDependencies(this);
		Main.prefix = Config.getString(Config.Options.Prefix);
		if (Config.getBool(Config.Options.SkinsRestorerMySQLEnable)) {
			skinsRestorerMySqlSetup();
		}
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

	public static Main getPlugin() {
		return plugin;
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
		if (Config.getBool(Config.Options.CustomIPEnable)) {
			return Config.getString(Config.Options.CustomIPIP);
		} else {
			return Config.getString(Config.Options.CustomIPRealIP);
		}
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

	public void skinsRestorerMySqlSetup() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			MysqlDataSource dataSource = new MysqlDataSource();
			dataSource.setServerName(Config.getString(Config.Options.SkinsRestorerMySQLHost));
			dataSource.setPort(Config.getInt(Config.Options.SkinsRestorerMySQLPort));
			dataSource.setDatabaseName(Config.getString(Config.Options.SkinsRestorerMySQLDatabase));
			dataSource.setUser(Config.getString(Config.Options.SkinsRestorerMySQLUsername));
			dataSource.setPassword(Config.getString(Config.Options.SkinsRestorerMySQLPassword));
			skinRestorerConnection = dataSource.getConnection();
			Main.consoleMsg(Formatter.FText("&a&lSkinsRestorer MySQL Connected"));
		} catch (Exception e) {
			Main.consoleMsg(Formatter.FText("&c&lSkinsRestorer MySQL Error"));
			Main.consoleMsg(Formatter.FText("&c&lDisabling plugin..."));
			Bukkit.getPluginManager().disablePlugin(this);
		}
	}

	public Connection getSkinRestorerConnection() {
		try {
			if (skinRestorerConnection == null || skinRestorerConnection.isClosed()) {
				Main.consoleMsg(Formatter.FText("&c&lSkinsRestorer MySQL Error"));
				Main.consoleMsg(Formatter.FText("&c&lDisabling plugin..."));
				Bukkit.getPluginManager().disablePlugin(this);
			}
			return skinRestorerConnection;
		} catch (SQLException throwables) {
			Main.consoleMsg(Formatter.FText("&c&lSkinsRestorer MySQL Error"));
			Main.consoleMsg(Formatter.FText("&c&lDisabling plugin..."));
			Bukkit.getPluginManager().disablePlugin(this);
			return skinRestorerConnection;
		}
	}

	public static int random(int a, int b) {
		return new SplittableRandom().nextInt(a, b + 1);
	}

	public static double round(double value, int places) {
		return round(value, places, RoundingMode.HALF_UP);
	}

	public static double round(double value, int places, RoundingMode roundingMode) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(Double.toString(value));
		bd = bd.setScale(places, roundingMode);
		return bd.doubleValue();
	}

	public static boolean isNumeric(String string) {
		if (string == null) {
			return false;
		}
		try {
			double d = Double.parseDouble(string);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public static boolean isBool(String string) {
		if (string == null) {
			return false;
		}
		return string.equalsIgnoreCase("true") || string.equalsIgnoreCase("false");
	}

	public static boolean contains(String string1, String string2) {
		if (string1 == null && string2 != null) {
			return false;
		} else if (string2 == null && string1 != null) {
			return false;
		} else if (string1 == null) { //string2 == null
			Main.debug("Contains have Null");
			return true;
		} else {
			return Formatter.RemoveFormat(string1).contains(Formatter.RemoveFormat(string2));
		}
	}

	public static BufferedImage scale(BufferedImage imageToScale, int dWidth, int dHeight) {
		BufferedImage scaledImage = null;
		if (imageToScale != null) {
			scaledImage = new BufferedImage(dWidth, dHeight, imageToScale.getType());
			Graphics2D graphics2D = scaledImage.createGraphics();
			graphics2D.drawImage(imageToScale, 0, 0, dWidth, dHeight, null);
			graphics2D.dispose();
		}
		return scaledImage;
	}
}
