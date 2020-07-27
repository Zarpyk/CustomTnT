package guerrero61.customtnt.MainUtils.Registers;

import java.io.File;
import java.util.Objects;

import javax.security.auth.login.LoginException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.MainUtils.Formatter;
import guerrero61.customtnt.MainUtils.Config.Config;
import guerrero61.customtnt.MainUtils.Config.DiscordConfig;
import guerrero61.customtnt.MainUtils.Config.MessagesConfig;
import guerrero61.customtnt.commands.MainCommand;
import guerrero61.customtnt.commands.tabcompleter.MainCommandCompleter;
import guerrero61.customtnt.discord.commands.*;
import guerrero61.customtnt.discord.events.DiscordReady;
import guerrero61.customtnt.discord.minecraft.DiscordToMinecraft;
import guerrero61.customtnt.discord.minecraft.MinecraftToDiscord;
import guerrero61.customtnt.events.*;
import guerrero61.customtnt.events.formats.TabList;
import guerrero61.customtnt.mobs.TnTDragon;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.cloud.CloudExpansion;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.luckperms.api.LuckPerms;

public class RegisterEvents {

	/**
	 * Sirve para registrar los archivos de configuración
	 */
	public void registerConfig(Main m) {
		File fConfig = new File(m.getDataFolder(), "config.yml");
		m.configPath = fConfig.getPath();
		if (!fConfig.exists()) {
			m.getConfig().options().copyDefaults(true);
			m.saveConfig();
		}
		Main.config = m.getConfig();
	}

	public void registerMessagesConfig(Main m) {
		m.messagesConfigFile = new File(m.getDataFolder(), "messages.yml");
		if (!m.messagesConfigFile.exists()) {
			MessagesConfig mc = new MessagesConfig();
			MessagesConfig.getMessagesConfig(m).options().copyDefaults(true);
			mc.saveMessagesConfig(m);
		}
	}

	public void registerDiscordConfig(Main m) {
		m.discordConfigFile = new File(m.getDataFolder(), "discord.yml");
		if (!m.discordConfigFile.exists()) {
			DiscordConfig dc = new DiscordConfig();
			DiscordConfig.getDiscordConfig(m).options().copyDefaults(true);
			dc.saveDiscordConfig(m);
		}
	}

	/**
	 * Sirve para iniciar el bot de Discord
	 */
	public void registerDiscord(Main m) {
		JDABuilder builder = JDABuilder.createDefault(Config.getString(Config.Options.Token));

		builder.setActivity(Activity.playing("/help para ayuda"));

		try {
			m.api = builder.build();
		} catch (LoginException e) {
			e.printStackTrace();
		}

		m.api.addEventListener(new DiscordReady());
		m.api.addEventListener(new DiscordToMinecraft(m.api));
		m.api.addEventListener(new Help());
		m.api.addEventListener(new IP());
		m.api.addEventListener(new ReportSuggest(m.api));
		m.api.addEventListener(new ServerInfo(m.api, m));
		m.api.addEventListener(new Summon(m));
		m.api.addEventListener(new Busca());
	}

	public void registerDependencies(Main m) {
		if (Bukkit.getPluginManager().getPlugin("LuckPerms") != null) {
			m.lpApi = registerLuckPerms();
		}
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			PlaceholderAPI.registerPlaceholderHook("customtnt", new RegisterPlaceholderAPI(m));
			//registerPAPI(m);
		}
	}

	/**
	 * Sirve para registrar los eventos
	 */
	public void registerEvents(Main m) {
		PluginManager pm = m.getServer().getPluginManager();
		pm.registerEvents(new Death(m), m);
		pm.registerEvents(new Sleep(m), m);
		pm.registerEvents(new Weather(m.api), m);
		pm.registerEvents(new Totem(m.api), m);
		pm.registerEvents(new DisableCustomRepair(), m);
		pm.registerEvents(new DisableTrident(), m);
		pm.registerEvents(new MinecraftToDiscord(m, m.api), m);
		pm.registerEvents(new TnTDragon(m), m);
		pm.registerEvents(new TabList(m), m);
		//pm.registerEvents(new ColorChat(), m);
	}

	/**
	 * Sirve para registrar los comandos
	 */
	public void registerCommands(Main m) {
		register("tnt", m);
	}

	private void register(String command, Main m) {
		Objects.requireNonNull(m.getCommand(command)).setExecutor(new MainCommand(m, m.api));
		Objects.requireNonNull(m.getCommand(command)).setTabCompleter(new MainCommandCompleter());
	}

	private LuckPerms registerLuckPerms() {
		RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
		if (provider != null) {
			return provider.getProvider();
		} else {
			Main.consoleMsg(Formatter.FText("&cNo se ha podido cargar LuckPerms."));
			return null;
		}
	}

	@Deprecated
	private void registerPAPI(Main m) {
		String[] expansionList = new String[] { "Vault", "Player", "Server" };
		PlaceholderAPIPlugin papi = (PlaceholderAPIPlugin) Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
		assert papi != null;
		for (String expansionName : expansionList) {
			CloudExpansion expansion = papi.getExpansionCloud().getCloudExpansion(expansionName);
			if (expansion == null) {
				Main.consoleMsg(Formatter.FText("&bPAPI: &cNo expansion found with the name: &f" + expansionName));
				return;
			}
			PlaceholderExpansion loaded = papi.getExpansionManager().getRegisteredExpansion(expansionName);
			if (loaded != null && loaded.isRegistered())
				PlaceholderAPI.unregisterPlaceholderHook(loaded.getIdentifier());
			String version = expansion.getLatestVersion();
			Main.consoleMsg(Formatter.FText("&bPAPI: &aDownload starting for expansion: &f" + expansion.getName()
					+ " &aversion: &f" + version));
			papi.getExpansionCloud().downloadExpansion(null, expansion, version);
			papi.getExpansionCloud().clean();
			papi.getExpansionCloud().fetch(papi.getPlaceholderAPIConfig().cloudAllowUnverifiedExpansions());
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(m, () -> papi.reloadConf(Bukkit.getConsoleSender()), 100L);
	}
}
