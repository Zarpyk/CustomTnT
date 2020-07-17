package guerrero61.tntcore.MainUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import guerrero61.tntcore.Main;

public class DiscordConfig {

	public FileConfiguration getDiscordConfig(Main m) {
		if (Main.discordConfig == null) {
			reloadDiscordConfig(m);
		}
		return Main.discordConfig;
	}

	public void reloadDiscordConfig(Main m) {
		if (Main.discordConfig == null) {
			Main.discordConfigFile = new File(m.getDataFolder(), "discord.yml");
		}
		Main.discordConfig = YamlConfiguration.loadConfiguration(Main.discordConfigFile);
		Reader defConfigStream;
		defConfigStream = new InputStreamReader(Objects.requireNonNull(m.getResource("discord.yml")),
				StandardCharsets.UTF_8);
		YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
		Main.discordConfig.setDefaults(defConfig);
	}

	public void saveDiscordConfig() {
		try {
			Main.discordConfig.save(Main.discordConfigFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
