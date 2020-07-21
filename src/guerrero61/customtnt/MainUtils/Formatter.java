package guerrero61.customtnt.MainUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.entity.Player;

import guerrero61.customtnt.Main;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;

public class Formatter {

	private static final Pattern pattern = Pattern.compile("(?<!\\\\)(#[a-fA-F0-9]{6})");

	public static String FText(String text) {
		text = org.bukkit.ChatColor.translateAlternateColorCodes('&', Main.prefix + text);
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			String color = text.substring(matcher.start(), matcher.end());
			text = text.replace(color, "" + ChatColor.of(color));
		}
		text = PlaceholderAPI.setPlaceholders(null, text);
		return text;
	}

	public static String FText(String text, Player player) {
		text = org.bukkit.ChatColor.translateAlternateColorCodes('&', Main.prefix + text);
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			String color = text.substring(matcher.start(), matcher.end());
			text = text.replace(color, "" + ChatColor.of(color));
		}
		text = PlaceholderAPI.setPlaceholders(player, text);
		return text;
	}

	public static String FText(String text, boolean noPrefix) {
		if (noPrefix) {
			text = org.bukkit.ChatColor.translateAlternateColorCodes('&', text);
		} else {
			text = org.bukkit.ChatColor.translateAlternateColorCodes('&', Main.prefix + text);
		}
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			String color = text.substring(matcher.start(), matcher.end());
			text = text.replace(color, "" + ChatColor.of(color));
		}
		text = PlaceholderAPI.setPlaceholders(null, text);
		return text;
	}

	public static String FText(String text, boolean noPrefix, Player player) {
		if (noPrefix) {
			text = org.bukkit.ChatColor.translateAlternateColorCodes('&', text);
		} else {
			text = org.bukkit.ChatColor.translateAlternateColorCodes('&', Main.prefix + text);
		}
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			String color = text.substring(matcher.start(), matcher.end());
			text = text.replace(color, "" + ChatColor.of(color));
		}
		text = PlaceholderAPI.setPlaceholders(player, text);
		return text;
	}

	public static String Capitalize(String str) {
		if (str == null || str.isEmpty()) {
			return str;
		}
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	public static String RemoveFormat(String text) {

		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			String color = text.substring(matcher.start(), matcher.end());
			text = text.replace(color, "");
		}

		String t = text.replace("§", "&");
		return t.replace("&a", "").replace("&b", "").replace("&c", "").replace("&d", "").replace("&e", "")
				.replace("&f", "").replace("&1", "").replace("&2", "").replace("&3", "").replace("&4", "")
				.replace("&5", "").replace("&6", "").replace("&7", "").replace("&8", "").replace("&9", "")
				.replace("&0", "").replace("&k", "").replace("&l", "").replace("&m", "").replace("&n", "")
				.replace("&o", "").replace("&r", "");
	}

	public static String RemoveFormat(String text, Player player) {
		text = FText(text, true, player);

		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			String color = text.substring(matcher.start(), matcher.end());
			text = text.replace(color, "");
		}

		String t = text.replace("§", "&");
		return t.replace("&a", "").replace("&b", "").replace("&c", "").replace("&d", "").replace("&e", "")
				.replace("&f", "").replace("&1", "").replace("&2", "").replace("&3", "").replace("&4", "")
				.replace("&5", "").replace("&6", "").replace("&7", "").replace("&8", "").replace("&9", "")
				.replace("&0", "").replace("&k", "").replace("&l", "").replace("&m", "").replace("&n", "")
				.replace("&o", "").replace("&r", "");
	}
}
