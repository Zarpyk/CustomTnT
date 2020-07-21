package guerrero61.customtnt.MainUtils.Config;

import java.util.List;
import java.util.Objects;

import guerrero61.customtnt.Main;

public class Config {

	public enum CONFIG {
		Main, Messages, Discord
	}

	public enum Options {
		/*------------------
		 * Main Config
		 ------------------*/
		//Death
		DeathMsgEnable("Death.msg-enable", CONFIG.Main), DeathCoordsEnable("Death.coords-enable", CONFIG.Main),
		//Sleep
		SleepExplosive("Sleep.explosive", CONFIG.Main),
		//Storm
		StormEnable("Storm.enable", CONFIG.Main), StormAddSeconds("Storm.add-seconds", CONFIG.Main),
		StormSoundEnable("Storm.sound-enable", CONFIG.Main), StormTitleEnable("Storm.title-enable", CONFIG.Main),
		StormActionBarEnable("Storm.actionbar-enable", CONFIG.Main),
		StormActionBarDelay("Storm.actionbar-delay", CONFIG.Main),
		//Totem
		TotemFailEnable("Totem.fail-enable", CONFIG.Main), TotemProbability("Totem.probability", CONFIG.Main),
		//MainWorld
		MainWorld("MainWorld", CONFIG.Main),
		/*------------------
		 * Messages Config
		 ------------------*/
		Prefix("Prefix", CONFIG.Messages),
		//Errors
		ErrorsNoExist("Errors.no-exist", CONFIG.Messages), ErrorsNoConsole("Errors.no-console", CONFIG.Messages),
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
