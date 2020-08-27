package guerrero61.customtnt.mainutils.config;

import guerrero61.customtnt.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class DiscordConfig {

    public static FileConfiguration getDiscordConfig(Main m) {
        if(Main.discordConfig == null) {
            reloadDiscordConfig(m);
        }
        return Main.discordConfig;
    }

    private static void reloadDiscordConfig(Main m) {
        if(Main.discordConfig == null) {
            m.discordConfigFile = new File(m.getDataFolder(), "discord.yml");
        }
        Main.discordConfig = YamlConfiguration.loadConfiguration(m.discordConfigFile);
        Reader defConfigStream;
        defConfigStream = new InputStreamReader(Objects.requireNonNull(m.getResource("discord.yml")),
                StandardCharsets.UTF_8);
        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
        Main.discordConfig.setDefaults(defConfig);
    }

    public static void saveDiscordConfig(Main m) {
        try {
            Main.discordConfig.save(m.discordConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
