package guerrero61.customtnt.MainUtils;

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
		if (m.messagesConfig == null) {
			reloadMessagesConfig(m);
		}
		return m.messagesConfig;
	}

	private static void reloadMessagesConfig(Main m) {
		if (m.messagesConfig == null) {
			m.messagesConfigFile = new File(m.getDataFolder(), "messages.yml");
		}
		m.messagesConfig = YamlConfiguration.loadConfiguration(m.messagesConfigFile);
		Reader defConfigStream;
		defConfigStream = new InputStreamReader(Objects.requireNonNull(m.getResource("messages.yml")),
				StandardCharsets.UTF_8);
		YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
		m.messagesConfig.setDefaults(defConfig);
	}

	public void saveMessagesConfig(Main m) {
		try {
			m.messagesConfig.save(m.messagesConfigFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
