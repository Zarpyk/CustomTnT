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

public class MessagesConfig {

	public FileConfiguration getMessagesConfig(Main m) {
		if (Main.messagesConfig == null) {
			reloadMessagesConfig(m);
		}
		return Main.messagesConfig;
	}

	public void reloadMessagesConfig(Main m) {
		if (Main.messagesConfig == null) {
			Main.messagesConfigFile = new File(m.getDataFolder(), "messages.yml");
		}
		Main.messagesConfig = YamlConfiguration.loadConfiguration(Main.messagesConfigFile);
		Reader defConfigStream;
		defConfigStream = new InputStreamReader(Objects.requireNonNull(m.getResource("messages.yml")),
				StandardCharsets.UTF_8);
		YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
		Main.messagesConfig.setDefaults(defConfig);
	}

	public void saveMessagesConfig() {
		try {
			Main.messagesConfig.save(Main.messagesConfigFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
