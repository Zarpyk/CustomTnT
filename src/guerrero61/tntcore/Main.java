package guerrero61.tntcore;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import guerrero61.tntcore.commands.MainCommand;
import guerrero61.tntcore.commands.discord.Help;
import guerrero61.tntcore.commands.discord.ReportSuggest;
import guerrero61.tntcore.commands.discord.ServerInfo;
import guerrero61.tntcore.commands.discord.Summon;
import guerrero61.tntcore.commands.tabcompleter.MainCommandCompleter;
import guerrero61.tntcore.events.Death;
import guerrero61.tntcore.events.Sleep;
import guerrero61.tntcore.events.Totem;
import guerrero61.tntcore.events.Weather;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Main extends JavaPlugin {
	PluginDescriptionFile pInfo = getDescription();
	public String name = pInfo.getName();
	public String version = pInfo.getVersion();

	public String startMessage = ChatColor.GREEN + name + " " + version + ": Se ha activado.";
	public String stopMessage = ChatColor.RED + name + " " + version + ": Se ha desactivado.";

	public String configPath;
	private static FileConfiguration config;
	private static String prefix;

	public static String[] allowIP = new String[] { "***REMOVED***", "0.0.0.0" };
	
	private DiscordApi api;
	
	public String stormTime;
	
	
	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage(startMessage);
		if (CheckDisablePlugin(getPluginLoader(), (Plugin) this)) {
			registerConfig();
			registerEvents();
			registerCommands();
			registerDiscord();
			Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin) this, new Runnable() {
				public void run() {
					long segundosbrutos = (Bukkit.getWorld("world").getWeatherDuration() / 20);
					long hours = segundosbrutos % 86400L / 3600L;
					long minutes = segundosbrutos % 3600L / 60L;
					long seconds = segundosbrutos % 60L;
					long days = segundosbrutos / 86400L;
					stormTime = String.format("%02d:%02d:%02d",
							new Object[] { Long.valueOf(hours), Long.valueOf(minutes), Long.valueOf(seconds) });
					if (days < 1L && Bukkit.getWorld("world").hasStorm()) {
						String Message = Main.this.getConfig().getString("Messages.Death.train-actionbar");
						Bukkit.getOnlinePlayers().forEach(player -> player.spigot()
								.sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
										ChatColor.translateAlternateColorCodes('&', Message.replace("%time%", stormTime)))));
					}
				}
			}, 0L, 20L);
		}
	}

	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage(stopMessage);
		
        if (api != null) {
            api.disconnect();
            api = null;
        }
	}

	private void registerCommands() {
		register("tnt", new MainCommand(this), new MainCommandCompleter());
	}

	private void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new Death(this), this);
		pm.registerEvents(new Sleep(this), this);
		pm.registerEvents(new Weather(), this);
		pm.registerEvents(new Totem(), this);
	}

	private void registerConfig() {
		File fConfig = new File(this.getDataFolder(), "config.yml");
		configPath = fConfig.getPath();
		if (!fConfig.exists()) {
			this.getConfig().options().copyDefaults(true);
			saveConfig();
		}
		config = Main.this.getConfig();
		prefix = config.getString("Prefix");
	}
	
	private void registerDiscord() {
		new DiscordApiBuilder()
        .setToken("***REMOVED***") // Set the token of the bot here
        .login() // Log the bot in
        .thenAccept(this::onConnectToDiscord) // Call #onConnectToDiscord(...) after a successful login
        .exceptionally(error -> {
            // Log a warning when the login to Discord failed (wrong token?)
            getLogger().warning("Failed to connect to Discord! Disabling plugin!");
            getPluginLoader().disablePlugin(this);
            return null;
        });
	}
	
    private void onConnectToDiscord(DiscordApi api) {
        this.api = api;

        getLogger().info("Se ha conectado con el bot de discord:" + api.getYourself().getDiscriminatedName());
        api.addListener(new ServerInfo(api, this));
        api.addListener(new ReportSuggest(api));
        api.addListener(new Help());
        //api.addListener(new Summon(this));
    }

	public static boolean CheckDisablePlugin(PluginLoader pl, Plugin plugin) {
		String IP = Bukkit.getServer().getIp();
		for (int i = 0; i < allowIP.length; i++) {
			if (IP.equals(allowIP[i])) {
				return true;
			}
		}
		return false;
	}
	
	private void register(String command, CommandExecutor ce, TabCompleter tc) {
		this.getCommand(command).setExecutor(ce);
		this.getCommand(command).setTabCompleter(tc);
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
	
	public static Integer getInt(String configOption) {
		return config.getInt(configOption);
	}
	
	public static Float getFloat(String configOption) {
		return Float.parseFloat(config.getString(configOption));
	}
	
	public static Boolean getBool(String configOption) {
		return config.getBoolean(configOption);
	}
}
