package guerrero61.customtnt.mainutils.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import guerrero61.customtnt.Main;

public class MessagesConfig {

	public static FileConfiguration getMessagesConfig(Main m) {
		if (Main.messagesConfig == null) {
			reloadMessagesConfig(m);
		}
		return Main.messagesConfig;
	}

	private static void reloadMessagesConfig(Main m) {
		if (Main.messagesConfig == null) {
			m.messagesConfigFile = new File(m.getDataFolder(), "messages.yml");
		}
		Main.messagesConfig = YamlConfiguration.loadConfiguration(m.messagesConfigFile);
		Reader defConfigStream;
		defConfigStream = new InputStreamReader(Objects.requireNonNull(m.getResource("messages.yml")),
				StandardCharsets.UTF_8);
		YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
		Main.messagesConfig.setDefaults(defConfig);
	}

	public void saveMessagesConfig(Main m) {
		try {
			Main.messagesConfig.save(m.messagesConfigFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
