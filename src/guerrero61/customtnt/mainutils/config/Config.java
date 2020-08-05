package guerrero61.customtnt.mainutils.config;

import guerrero61.customtnt.Main;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Objects;

public class Config {

    public enum CONFIG {
        Main, Messages, Discord
    }

    public enum Options {
        /*------------------
         * Main Config
         ------------------*/
        //Features
        DeathEnable("Features.Death", CONFIG.Main), DisableEnable("Features.Disable", CONFIG.Main),
        DiscordEnable("Features.Discord", CONFIG.Main), FormatterEnable("Features.Formatter", CONFIG.Main),
        CustomDragonEnable("Features.CustomDragon", CONFIG.Main),
        //Features Plugin Hooks
        SkinsRestorerEnable("Features.SkinsRestorer", CONFIG.Main), MMOItemsEnable("Features.MMOItems", CONFIG.Main),
        LuckPermsEnable("Features.LuckPerms", CONFIG.Main),
        //Death
        DeathMsgEnable("Death.msg-enable", CONFIG.Main), DeathCoordsEnable("Death.coords-enable", CONFIG.Main),
        //Death.Sleep
        SleepExplosive("Death.Sleep.explosive", CONFIG.Main),
        //Death.Storm
        StormEnable("Death.Storm.enable", CONFIG.Main), StormAddSeconds("Death.Storm.add-seconds", CONFIG.Main),
        StormSoundEnable("Death.Storm.sound-enable", CONFIG.Main),
        StormTitleEnable("Death.Storm.title-enable", CONFIG.Main),
        StormActionBarEnable("Death.Storm.actionbar-enable", CONFIG.Main),
        StormActionBarDelay("Death.Storm.actionbar-delay", CONFIG.Main),
        //Death.Totem
        TotemFailEnable("Death.Totem.fail-enable", CONFIG.Main),
        TotemProbability("Death.Totem.probability", CONFIG.Main),
        //Disable
        DisableTrident("Disable.trident", CONFIG.Main),
        //Formatter
        FormatterTabList("Formatter.tab-list", CONFIG.Main), FormatterCustomChat("Formatter.custom-chat", CONFIG.Main),
        //CustomDragon
        CustomDragonName("CustomDragon.name", CONFIG.Main),
        //MMOItems
        MMOItemsDisableCutomsRepair("MMOItems.disable-custom-repair", CONFIG.Main),
        //MainWorld
        MainWorld("MainWorld", CONFIG.Main),
        //Debug Mode
        DebugMode("Debug-mode", CONFIG.Main),
        /*------------------
         * Messages Config
         ------------------*/
        Prefix("Prefix", CONFIG.Messages),
        //Errors
        ErrorsNoExist("Errors.no-exist", CONFIG.Messages), ErrorsNoConsole("Errors.no-console", CONFIG.Messages),
        ErrorsArgsMiss("Errors.args-miss", CONFIG.Messages), ErrorsArgsError("Errors.args-error", CONFIG.Messages), ErrorsNoPerm("Errors.no-perm", CONFIG.Messages),
        //Check
        CheckPlayer("Check.player", CONFIG.Messages), CheckConsole("Check.console", CONFIG.Messages),
        //Death
        DeathMsg("Death.msg", CONFIG.Messages), DeathCoordsMsg("Death.coords-msg", CONFIG.Messages),
        DeathTrainMsg("Death.train-msg", CONFIG.Messages),
        DeathTrainActionBar("Death.train-actionbar", CONFIG.Messages), DeathTitle("Death.title", CONFIG.Messages),
        DeathSubtitle("Death.subtitle", CONFIG.Messages), DeathPlayers("Death.players", CONFIG.Messages),
        //Reload
        Reload("Reload", CONFIG.Messages),
        //Sleep
        SleepMsg("Sleep.msg", CONFIG.Messages),
        //Storm
        StormEndMsg("Storm.end-msg", CONFIG.Messages),
        //Totem
        TotemFailMsg("Totem.fail-msg", CONFIG.Messages), TotemUseMsg("Totem.use-msg", CONFIG.Messages),
        //Chat
        ChatFormat("Chat.format", CONFIG.Messages),
        /*------------------
         * Discord Config
         ------------------*/
        //Token
        Token("Token", CONFIG.Discord),
        //Channels
        ChannelsSendMsg("Channels.send-msg", CONFIG.Discord), ChannelsCommands("Channels.commands", CONFIG.Discord),
        //Channel Description
        ChannelDescription("Channel-description", CONFIG.Discord),
        //Messages
        MessagesStart("Messages.start", CONFIG.Discord), MessagesStop("Messages.stop", CONFIG.Discord),
        MessagesJoin("Messages.join", CONFIG.Discord), MessagesFirstJoin("Messages.first-join", CONFIG.Discord),
        MessagesQuit("Messages.quit", CONFIG.Discord),
        MessagesMinecraftToDiscordChat("Messages.minecraft-to-discord-chat", CONFIG.Discord),
        MessagesRemoveRank("Messages.remove-rank", CONFIG.Discord),
        MessagesDiscordToMinecraftChat("Messages.discord-to-minecraft-chat", CONFIG.Discord),
        MessagesAdvancement("Messages.advancement", CONFIG.Discord),
        MessagesCommandList("Messages.command-list", CONFIG.Discord),
        //Verify
        VerifyEnable("Verify.enable", CONFIG.Discord), VerifyPrefix("Verify.prefix", CONFIG.Discord),
        VerifyRoleID("Verify.role-id", CONFIG.Discord), VerifyDiscord("Verify.discord", CONFIG.Discord),
        VerifyCommandError("Verify.command-error", CONFIG.Discord),
        VerifyYouVerified("Verify.you-verified", CONFIG.Discord),
        VerifyUserAlredyVerified("Verify.user-alredy-verified", CONFIG.Discord),
        VerifySuccess("Verify.success", CONFIG.Discord), VerifyError("Verify.error", CONFIG.Discord),
        VerifyErrorNick("Verify.error-nick", CONFIG.Discord), VerifyNoOnline("Verify.no-online", CONFIG.Discord),
        //CustomIP
        CustomIPEnable("CustomIP.enable", CONFIG.Main), CustomIPIP("CustomIP.IP", CONFIG.Main);

        private final String string;
        private final CONFIG type;

        Options(String string, CONFIG type) {
            this.string = string;
            this.type = type;
        }

        public String getValue() {
            return string;
        }

        public CONFIG getType() {
            return type;
        }

    }

    public static String getString(Options configOptions) {
        return Main.configMap.get(configOptions.getType()).getString(configOptions.getValue());
    }

    public static List<String> getStringList(Options configOptions) {
        return Main.configMap.get(configOptions.getType()).getStringList(configOptions.getValue());
    }

    public static Integer getInt(Options configOptions) {
        return Main.configMap.get(configOptions.getType()).getInt(configOptions.getValue());
    }

    public static Float getFloat(Options configOptions) {
        return Float.parseFloat(Objects
                .requireNonNull(Main.configMap.get(configOptions.getType()).getString(configOptions.getValue())));
    }

    public static Boolean getBool(Options configOptions) {
        return Main.configMap.get(configOptions.getType()).getBoolean(configOptions.getValue());
    }

    public static void set(Options configOptions, String value) {
        Main.configMap.get(configOptions.getType()).set(configOptions.getValue(), value);
        save(Main.configMap.get(configOptions.getType()));
    }

    public static void set(Options configOptions, List<String> value) {
        Main.configMap.get(configOptions.getType()).set(configOptions.getValue(), value);
        save(Main.configMap.get(configOptions.getType()));
    }

    public static void set(Options configOptions, Integer value) {
        Main.configMap.get(configOptions.getType()).set(configOptions.getValue(), value);
        save(Main.configMap.get(configOptions.getType()));
    }

    public static void set(Options configOptions, Float value) {
        Main.configMap.get(configOptions.getType()).set(configOptions.getValue(), value);
        save(Main.configMap.get(configOptions.getType()));
    }

    public static void set(Options configOptions, Boolean value) {
        Main.configMap.get(configOptions.getType()).set(configOptions.getValue(), value);
        save(Main.configMap.get(configOptions.getType()));
    }

    private static void save(FileConfiguration file) {
        if (file == Main.configMap.get(CONFIG.Main)) {
            Main.getPlugin().saveConfig();
        } else if (file == Main.configMap.get(CONFIG.Messages)) {
            MessagesConfig.saveMessagesConfig(Main.getPlugin());
        } else if (file == Main.configMap.get(CONFIG.Discord)) {
            DiscordConfig.saveDiscordConfig(Main.getPlugin());
        }
    }

	/*public static String getString(String configOption) {
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
	}*/
}
