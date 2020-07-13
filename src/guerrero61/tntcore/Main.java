package guerrero61.tntcore;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.security.auth.login.LoginException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import guerrero61.tntcore.commands.MainCommand;
import guerrero61.tntcore.commands.tabcompleter.MainCommandCompleter;
import guerrero61.tntcore.discord.commands.Help;
import guerrero61.tntcore.discord.commands.ReportSuggest;
import guerrero61.tntcore.discord.commands.ServerInfo;
import guerrero61.tntcore.discord.commands.Summon;
import guerrero61.tntcore.discord.events.DiscordReady;
import guerrero61.tntcore.events.Death;
import guerrero61.tntcore.events.Sleep;
import guerrero61.tntcore.events.Totem;
import guerrero61.tntcore.events.Weather;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

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

	private JDA api;
	public String token;

	public String stormTime;

	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage(startMessage);
		if (CheckDisablePlugin()) {
			registerConfig();
			registerEvents();
			registerCommands();
			registerDiscord();
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
				int segundosbrutos = (Objects.requireNonNull(Bukkit.getWorld("world")).getWeatherDuration() / 20);
				int hours = Math.toIntExact(segundosbrutos % 86400L / 3600L);
				int minutes = Math.toIntExact(segundosbrutos % 3600L / 60L);
				int seconds = Math.toIntExact(segundosbrutos % 60L);
				int days = Math.toIntExact(segundosbrutos / 86400L);
				stormTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
				if (days < 1L && Objects.requireNonNull(Bukkit.getWorld("world")).hasStorm()) {
					String Message = Main.this.getConfig().getString("Messages.Death.train-actionbar");
					Bukkit.getOnlinePlayers().forEach(player -> {
						assert Message != null;
						player.sendActionBar(FTextNPrefix(Message.replace("%time%", stormTime)));
					});
				}
			}, 0L, 20L);
		}
		Bukkit.getPlayer("");
	}

	public void onDisable() {
		if (api != null) {
			api.getEventManager().getRegisteredListeners()
					.forEach(listener -> api.getEventManager().unregister(listener));
			CompletableFuture<Void> shutdownTask = new CompletableFuture<>();
			api.addEventListener(new ListenerAdapter() {
				@Override
				public void onShutdown(@NotNull ShutdownEvent event) {
					shutdownTask.complete(null);
				}
			});
			api.shutdownNow();
			api = null;
			try {
				shutdownTask.get(5, TimeUnit.SECONDS);
			} catch (TimeoutException | InterruptedException | ExecutionException e) {
				getLogger().warning("JDA took too long to shut down, skipping");
			}
		}
		Bukkit.getConsoleSender().sendMessage(stopMessage);
	}

	private void registerCommands() {
		register("tnt");
	}

	private void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new Death(this), this);
		pm.registerEvents(new Sleep(this), this);
		pm.registerEvents(new Weather(), this);
		pm.registerEvents(new Totem(), this);
	}

	public void registerConfig() {
		File fConfig = new File(this.getDataFolder(), "config.yml");
		configPath = fConfig.getPath();
		if (!fConfig.exists()) {
			this.getConfig().options().copyDefaults(true);
			saveConfig();
		}
		config = Main.this.getConfig();
		prefix = config.getString("Prefix");
	}

	public void registerDiscord() {
		JDABuilder builder = JDABuilder.createDefault(Main.getString("Discord.token"));

		builder.setActivity(Activity.playing("/help para ayuda"));
		builder.setLargeThreshold(50);
		builder.setAutoReconnect(false);

		try {
			api = builder.build();
		} catch (LoginException e) {
			e.printStackTrace();
		}
		token = Main.getString("Discord.token");

		api.addEventListener(new DiscordReady());
		api.addEventListener(new Help());
		api.addEventListener(new ReportSuggest(api));
		api.addEventListener(new ServerInfo(api, this));
		api.addEventListener(new Summon(this));
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

	private void register(String command) {
		Objects.requireNonNull(this.getCommand(command)).setExecutor(new MainCommand(this, api));
		Objects.requireNonNull(this.getCommand(command)).setTabCompleter(new MainCommandCompleter());
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
