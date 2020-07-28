package guerrero61.customtnt.mainutils.registers;

import java.io.File;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.mainutils.config.Config;
import guerrero61.customtnt.mainutils.config.DiscordConfig;
import guerrero61.customtnt.mainutils.config.MessagesConfig;

public class RegisterConfigs {
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
		Main.configMap.put(Config.CONFIG.Main, Main.config);
	}

	public void registerMessagesConfig(Main m) {
		m.messagesConfigFile = new File(m.getDataFolder(), "messages.yml");
		if (!m.messagesConfigFile.exists()) {
			MessagesConfig.getMessagesConfig(m).options().copyDefaults(true);
			MessagesConfig.saveMessagesConfig(m);
		}
		Main.configMap.put(Config.CONFIG.Messages,
				Main.messagesConfig == null ? MessagesConfig.getMessagesConfig(m) : Main.messagesConfig);
	}

	public void registerDiscordConfig(Main m) {
		m.discordConfigFile = new File(m.getDataFolder(), "discord.yml");
		if (!m.discordConfigFile.exists()) {
			DiscordConfig.getDiscordConfig(m).options().copyDefaults(true);
			DiscordConfig.saveDiscordConfig(m);
		}
		Main.configMap.put(Config.CONFIG.Discord,
				Main.discordConfig == null ? DiscordConfig.getDiscordConfig(m) : Main.discordConfig);
	}
}
