package guerrero61.tntcore.MainUtils;

import java.io.File;
import java.util.Objects;

import javax.security.auth.login.LoginException;

import org.bukkit.plugin.PluginManager;

import guerrero61.tntcore.Main;
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
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

public class RegisterEvents {

	public static void registerConfig(Main m) {
		File fConfig = new File(m.getDataFolder(), "config.yml");
		m.configPath = fConfig.getPath();
		if (!fConfig.exists()) {
			m.getConfig().options().copyDefaults(true);
			m.saveConfig();
		}
		Main.config = m.getConfig();
		Main.prefix = Main.getString("Prefix");
	}

	public static void registerEvents(Main m) {
		PluginManager pm = m.getServer().getPluginManager();
		pm.registerEvents(new Death(m), m);
		pm.registerEvents(new Sleep(m), m);
		pm.registerEvents(new Weather(), m);
		pm.registerEvents(new Totem(), m);
	}

	public static void registerCommands(Main m) {
		register("tnt", m);
	}

	private static void register(String command, Main m) {
		Objects.requireNonNull(m.getCommand(command)).setExecutor(new MainCommand(m, m.api));
		Objects.requireNonNull(m.getCommand(command)).setTabCompleter(new MainCommandCompleter());
	}

	public static void registerDiscord(Main m) {
		JDABuilder builder = JDABuilder.createDefault(Main.getString("Discord.token"));

		builder.setActivity(Activity.playing("/help para ayuda"));
		builder.setLargeThreshold(50);
		builder.setAutoReconnect(false);

		try {
			m.api = builder.build();
		} catch (LoginException e) {
			e.printStackTrace();
		}

		m.api.addEventListener(new DiscordReady());
		m.api.addEventListener(new Help());
		m.api.addEventListener(new ReportSuggest(m.api));
		m.api.addEventListener(new ServerInfo(m.api, m));
		m.api.addEventListener(new Summon(m));
	}
}
