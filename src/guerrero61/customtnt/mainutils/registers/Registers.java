package guerrero61.customtnt.mainutils.registers;

import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.commands.MainCommand;
import guerrero61.customtnt.commands.tabcompleter.MainCommandCompleter;
import guerrero61.customtnt.discord.minecraft.Verify;
import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mainutils.config.Config;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.cloud.CloudExpansion;
import net.luckperms.api.LuckPerms;
import net.milkbowl.vault.permission.Permission;

public class Registers {

	/**
	 * Sirve para registrar los archivos de configuración
	 */
	public void registerConfig(Main m) {
		RegisterConfigs registerConfigs = new RegisterConfigs();
		registerConfigs.registerConfig(m);
		registerConfigs.registerMessagesConfig(m);
		if (Config.getBool(Config.Options.DiscordEnable)) {
			registerConfigs.registerDiscordConfig(m);
		}
	}

	/**
	 * Sirve para iniciar el bot de Discord
	 */
	public void registerDiscord(Main m) {
		RegisterDiscord registerDiscord = new RegisterDiscord(m);
		registerDiscord.registerDiscord();
	}

	public void registerDependencies(Main m) {
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			PlaceholderAPI.registerPlaceholderHook("customtnt", new RegisterPlaceholderAPI(m));
			//registerPAPI(m);
		} else {
			Main.consoleMsg(Formatter.FText("&c&lPlaceholderAPI is not in the plugin folder"));
			Bukkit.getPluginManager().disablePlugin(m);
		}
		if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
			RegisteredServiceProvider<Permission> rsp = m.getServer().getServicesManager()
					.getRegistration(Permission.class);
			assert rsp != null;
			m.perms = rsp.getProvider();
		} else {
			Main.consoleMsg(Formatter.FText("&c&lVault is not in the plugin folder"));
			Bukkit.getPluginManager().disablePlugin(m);
		}
		if (Bukkit.getPluginManager().getPlugin("SkinsRestorer") == null) {
			Config.set(Config.Options.SkinsRestorerEnable, false);
		}
		if (Bukkit.getPluginManager().getPlugin("MMOLib") == null
				|| Bukkit.getPluginManager().getPlugin("MMOItems") == null) {
			Config.set(Config.Options.MMOItemsEnable, false);
		}
		if (Bukkit.getPluginManager().getPlugin("LuckPerms") != null) {
			m.lpApi = registerLuckPerms();
		} else {
			Config.set(Config.Options.LuckPermsEnable, false);
		}
	}

	/**
	 * Sirve para registrar los eventos
	 */
	public void registerEvents(Main m) {
		new RegisterEvents(m);
	}

	/**
	 * Sirve para registrar los comandos
	 */
	public void registerCommands(Main m) {
		Objects.requireNonNull(m.getCommand("tnt")).setExecutor(new MainCommand(m, m.api));
		Objects.requireNonNull(m.getCommand("tnt")).setTabCompleter(new MainCommandCompleter());
		if (Config.getBool(Config.Options.VerifyEnable)) {
			Objects.requireNonNull(m.getCommand("verify")).setExecutor(new Verify(m));
			Objects.requireNonNull(m.getCommand("verify")).setTabCompleter(new Verify(m));
		}
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
	private void autoRegisterPAPI(Main m) {
		String[] expansionList = new String[] { "Vault", "Player", "Server", "Essentials" };
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
