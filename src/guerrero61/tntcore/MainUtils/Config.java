package guerrero61.tntcore.MainUtils;

import java.util.List;
import java.util.Objects;

import guerrero61.tntcore.Main;

public class Config {

	public enum CONFIG {
		Main, Messages, Discord
	}

	public static String getString(String configOption) {
		return Main.config.getString(configOption);
	}

	public static String getString(String configOption, CONFIG c) {
		return Main.configMap.get(c).getString(configOption);
	}

	public static List<String> getStringList(String configOption) {
		return Main.config.getStringList(configOption);
	}

	public static List<String> getStringList(String configOption, CONFIG c) {
		return Main.configMap.get(c).getStringList(configOption);
	}

	public static Integer getInt(String configOption) {
		return Main.config.getInt(configOption);
	}

	public static Integer getInt(String configOption, CONFIG c) {
		return Main.configMap.get(c).getInt(configOption);
	}

	public static Float getFloat(String configOption) {
		return Float.parseFloat(Objects.requireNonNull(Main.config.getString(configOption)));
	}

	public static Float getFloat(String configOption, CONFIG c) {
		return Float.parseFloat(Objects.requireNonNull(Main.configMap.get(c).getString(configOption)));
	}

	public static Boolean getBool(String configOption) {
		return Main.config.getBoolean(configOption);
	}

	public static Boolean getBool(String configOption, CONFIG c) {
		return Main.configMap.get(c).getBoolean(configOption);
	}
}
