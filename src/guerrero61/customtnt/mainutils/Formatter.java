package guerrero61.customtnt.mainutils;

import guerrero61.customtnt.Main;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Formatter {

    private static final Pattern pattern = Pattern.compile("(?<!\\\\)(#[a-fA-F0-9]{6})");
    private static final Pattern pattern2 = Pattern.compile("([&§][0-9a-fA-Fk-oK-OrR])");

    public static String FText(String text) {
        return FText(text, true, null);
    }

    public static String FText(String text, Player player) {
        return FText(text, true, player);
    }

    public static String FText(String text, boolean noPrefix) {
        return FText(text, noPrefix, null);
    }

    public static String FText(String text, boolean noPrefix, @Nullable Player player) {
        if(text == null || text.isEmpty()) {
            return text;
        }
        if(noPrefix) {
            text = org.bukkit.ChatColor.translateAlternateColorCodes('&', text);
        } else {
            text = org.bukkit.ChatColor.translateAlternateColorCodes('&', Main.prefix + text);
        }
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String color = text.substring(matcher.start(), matcher.end());
            text = text.replace(color, "" + ChatColor.of(color));
        }
        if(player != null) {
            text = PlaceholderAPI.setPlaceholders(player, text);
        }
        return text;
    }

    public static TextComponent FText2(String text, boolean noPrefix, @Nullable Player player) {
        if(text == null || text.isEmpty()) {
            return Component.empty();
        }
        if(player != null) {
            text = PlaceholderAPI.setPlaceholders(player, text);
        }
        TextComponent textComponent = Component.empty();
        for (String string : text.split("&")) {
            boolean firstPart = false;
            if(string.contains("#")) {
                for (String hexColorString : string.split("#")) {
                    Matcher matcher = pattern.matcher(string);
                    while (matcher.find()) {
                        if(!firstPart) {
                            if(matcher.start() != 0) {
                                textComponent = textComponent.append(
                                        LegacyComponentSerializer.legacyAmpersand().deserialize(
                                                string.substring(0, matcher.start())));
                            }
                            firstPart = true;
                        }
                        int hexColor = Integer.decode(
                                hexColorString.substring(matcher.start(), matcher.end()).replace("#", "0x"));
                        textComponent = textComponent.append(
                                Component.text(hexColorString.substring(matcher.end())).color(
                                        TextColor.color(hexColor)));
                    }
                }
            }
        }
        return textComponent;
    }

    public static String Capitalize(String str) {
        if(str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String RemoveFormat(String text) {
        return RemoveFormat(text, null);
    }

    public static String RemoveFormat(String text, @Nullable Player player) {
        if(text == null || text.isEmpty()) {
            return text;
        }

        if(player != null) {
            text = FText(text, true, player);
        }

        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String color = text.substring(matcher.start(), matcher.end());
            text = text.replace(color, "");
        }

        return text.replaceAll("([&§][0-9a-fA-Fk-oK-OrR])", "");
    }
}
