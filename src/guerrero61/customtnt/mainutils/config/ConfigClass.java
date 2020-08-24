package guerrero61.customtnt.mainutils.config;

import guerrero61.customtnt.Main;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigClass {

    protected String protectedFileName;
    protected FileConfiguration dataConfig;

    public ConfigClass() {
    }

    public ConfigClass(String fileName) {
        protectedFileName = fileName;
        dataConfig = CreateConfig(protectedFileName);
    }

    protected FileConfiguration CreateConfig(String fileName) {
        protectedFileName = fileName;
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

    public void deleteConfig() {
        dataConfig = null;
        boolean delete = new File(Main.getPlugin().getDataFolder(), protectedFileName + ".yml").delete();
        protectedFileName = null;
        Main.debug("Delete File:" + delete);
    }

    public void rename(String newName) {
        dataConfig = null;
        if (new File(Main.getPlugin().getDataFolder(), newName + ".yml").exists()) {
            boolean deleteFileDelete = new File(Main.getPlugin().getDataFolder(), newName + ".yml").delete();
            Main.debug("Rename File Delete:" + deleteFileDelete);
        }
        boolean rename = new File(Main.getPlugin().getDataFolder(), protectedFileName + ".yml")
                .renameTo(new File(Main.getPlugin().getDataFolder(), newName + ".yml"));
        protectedFileName = null;
        Main.debug("Rename File:" + rename);
    }

    public void set(String key, String value) {
        dataConfig.set(key.replace(" ", "-"), value);
        try {
            File dataFile = new File(Main.getPlugin().getDataFolder(), protectedFileName + ".yml");
            dataConfig.save(dataFile);
            dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void set(String key, Integer value) {
        dataConfig.set(key.replace(" ", "-"), value);
        try {
            File dataFile = new File(Main.getPlugin().getDataFolder(), protectedFileName + ".yml");
            dataConfig.save(dataFile);
            dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void set(String key, Double value) {
        dataConfig.set(key.replace(" ", "-"), value);
        try {
            File dataFile = new File(Main.getPlugin().getDataFolder(), protectedFileName + ".yml");
            dataConfig.save(dataFile);
            dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void set(String key, Boolean value) {
        dataConfig.set(key.replace(" ", "-"), value);
        try {
            File dataFile = new File(Main.getPlugin().getDataFolder(), protectedFileName + ".yml");
            dataConfig.save(dataFile);
            dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getString(String key) {
        dataConfig = CreateConfig(protectedFileName);
        return dataConfig.getString(key);
    }

    public Integer getInt(String key) {
        dataConfig = CreateConfig(protectedFileName);
        return dataConfig.getInt(key);
    }

    public Double getDouble(String key) {
        dataConfig = CreateConfig(protectedFileName);
        return dataConfig.getDouble(key);
    }

    public Boolean getBool(String key) {
        dataConfig = CreateConfig(protectedFileName);
        return dataConfig.getBoolean(key);
    }

    public ConfigurationSection getConfigurationSection(String path) {
        dataConfig = CreateConfig(protectedFileName);
        return dataConfig.getConfigurationSection(path);
    }
}
