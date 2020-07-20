package guerrero61.customtnt.MainUtils.Config;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import guerrero61.customtnt.Main;

public class DiscordConfig {

	public static FileConfiguration getDiscordConfig(Main m) {
		if (m.discordConfig == null) {
			reloadDiscordConfig(m);
		}
		return m.discordConfig;
	}

	private static void reloadDiscordConfig(Main m) {
		if (m.discordConfig == null) {
			m.discordConfigFile = new File(m.getDataFolder(), "discord.yml");
		}
		m.discordConfig = YamlConfiguration.loadConfiguration(m.discordConfigFile);
		Reader defConfigStream;
		defConfigStream = new InputStreamReader(Objects.requireNonNull(m.getResource("discord.yml")),
				StandardCharsets.UTF_8);
		YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
		m.discordConfig.setDefaults(defConfig);
	}

	public void saveDiscordConfig(Main m) {
		try {
			m.discordConfig.save(m.discordConfigFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
