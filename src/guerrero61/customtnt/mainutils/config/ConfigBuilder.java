package guerrero61.customtnt.mainutils.config;

import guerrero61.customtnt.Main;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigBuilder {

    private final String fileName;
    protected FileConfiguration dataConfig;

    public ConfigBuilder(String fileName) {
        this.fileName = fileName;
        dataConfig = CreateConfig(fileName);
    }

    private FileConfiguration CreateConfig(String fileName) {
        File dataFile = new File(Main.getPlugin().getDataFolder(), fileName + ".yml");
        if (!dataFile.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return YamlConfiguration.loadConfiguration(dataFile);
    }

    /*public static void deleteConfig(String fileName) {
        //noinspection ResultOfMethodCallIgnored
        new File(Main.getPlugin().getDataFolder(), fileName + ".yml").delete();
    }

    public static void rename(String fileName, String newName) {
        //noinspection ResultOfMethodCallIgnored
        new File(Main.getPlugin().getDataFolder(), fileName + ".yml")
                .renameTo(new File(Main.getPlugin().getDataFolder(), newName + ".yml"));
    }*/

    public void deleteConfig() {
        //noinspection ResultOfMethodCallIgnored
        new File(Main.getPlugin().getDataFolder(), fileName + ".yml").delete();
    }

    public void rename(String newName) {
        //noinspection ResultOfMethodCallIgnored
        new File(Main.getPlugin().getDataFolder(), fileName + ".yml")
                .renameTo(new File(Main.getPlugin().getDataFolder(), newName + ".yml"));
    }

    public void set(String key, String value) {
        dataConfig.set(key, value);
        try {
            File dataFile = new File(Main.getPlugin().getDataFolder(), fileName + ".yml");
            dataConfig.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void set(String key, Integer value) {
        dataConfig.set(key, value);
        try {
            File dataFile = new File(Main.getPlugin().getDataFolder(), fileName + ".yml");
            dataConfig.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void set(String key, Double value) {
        dataConfig.set(key, value);
        try {
            File dataFile = new File(Main.getPlugin().getDataFolder(), fileName + ".yml");
            dataConfig.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void set(String key, Boolean value) {
        dataConfig.set(key, value);
        try {
            File dataFile = new File(Main.getPlugin().getDataFolder(), fileName + ".yml");
            dataConfig.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getString(String key) {
        return dataConfig.getString(key);
    }

    public Integer getInt(String key) {
        return dataConfig.getInt(key);
    }

    public Double getDouble(String key) {
        return dataConfig.getDouble(key);
    }

    public Boolean getBool(String key) {
        return dataConfig.getBoolean(key);
    }

    public ConfigurationSection getConfigurationSection(String path) {
        return dataConfig.getConfigurationSection(path);
    }
}
