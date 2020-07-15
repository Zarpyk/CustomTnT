package guerrero61.tntcore.MainUtils;

import java.io.File;
import java.util.Objects;

import javax.security.auth.login.LoginException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;

import guerrero61.tntcore.Main;
import guerrero61.tntcore.commands.MainCommand;
import guerrero61.tntcore.commands.tabcompleter.MainCommandCompleter;
import guerrero61.tntcore.discord.commands.Help;
import guerrero61.tntcore.discord.commands.ReportSuggest;
import guerrero61.tntcore.discord.commands.ServerInfo;
import guerrero61.tntcore.discord.commands.Summon;
import guerrero61.tntcore.discord.events.DiscordReady;
import guerrero61.tntcore.discord.minecraft.DiscordToMinecraft;
import guerrero61.tntcore.discord.minecraft.MinecraftToDiscord;
import guerrero61.tntcore.events.*;
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
		Main.prefix = Main.getString("Prefix");
	}

	public LuckPerms registerLuckPerms() {
		RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
		if (provider != null) {
			return provider.getProvider();
		} else {
			Main.consoleMsg("&cNo se ha podido cargar LuckPerms.");
			return null;
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
		pm.registerEvents(new MinecraftToDiscord(m, m.api), m);
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

	/**
	 * Sirve para iniciar el bot de Discord
	 */
	public void registerDiscord(Main m) {
		JDABuilder builder = JDABuilder.createDefault(Main.getString("Discord.token"));

		builder.setActivity(Activity.playing("/help para ayuda"));

		try {
			m.api = builder.build();
		} catch (LoginException e) {
			e.printStackTrace();
		}

		m.api.addEventListener(new DiscordReady());
		m.api.addEventListener(new DiscordToMinecraft(m.api));
		m.api.addEventListener(new Help());
		m.api.addEventListener(new ReportSuggest(m.api));
		m.api.addEventListener(new ServerInfo(m.api, m));
		m.api.addEventListener(new Summon(m));
	}
}
