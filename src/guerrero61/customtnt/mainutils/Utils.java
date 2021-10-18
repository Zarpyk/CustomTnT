package guerrero61.customtnt.mainutils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;

public class Utils {

    public static void broadcast(String text) {
        final TextComponent textComponent = Component.text("You're a ")
                                                     .color(TextColor.color(0x443344))
                                                     .append(Component.text("Bunny", NamedTextColor.LIGHT_PURPLE))
                                                     .append(Component.text("! Press "))
                                                     .append(
                                                             Component.keybind("key.jump")
                                                                      .color(NamedTextColor.LIGHT_PURPLE)
                                                                      .decoration(TextDecoration.BOLD, true)
                                                     )
                                                     .append(Component.text(" to jump!"));
        Bukkit.getServer().broadcast(LegacyComponentSerializer.legacyAmpersand().deserialize("&6Hello &b&lworld&c!"));
    }

}
