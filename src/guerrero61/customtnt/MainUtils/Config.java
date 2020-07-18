package guerrero61.customtnt.MainUtils;

import java.util.List;
import java.util.Objects;

import guerrero61.customtnt.Main;

public class Config {

	public enum CONFIG {
		Main, Messages, Discord
	}

	public enum MainConfigOptions {
		DeathCoordsEnable("Death.coords-enable"), SleepExplosive("Sleep.explosive"), StormEnable("Storm.enable"),
		StormAddSeconds("Storm.add-seconds"), StormSoundEnable("Storm.sound-enable"),
		StormTitleEnable("Storm.title-enable"), TotemFailEnable("Totem.fail-enable"),
		TotelProbability("Totem.probability"), MainWorld("MainWorld"), CustomIPEnable("CustomIP.enable"),
		CustomIPIP("CustomIP.IP");

		private final String string;

		MainConfigOptions(String string) {
			this.string = string;
		}

		public String getValue() {
			return string;
		}
	}

	public enum MessagesConfigOptions {
		Prefix("Prefix"), ErrorsNoExist("Errors.no-exist"), ErrorsNoConsole("Errors.no-console"),
		CheckPlayer("Check.player"), CheckConsole("Check.console"), DeathMsg("Death.msg"),
		DeathCoordsEnable("Death.coords-enable"), DeathCoordsMsg("Death.coords-msg"), DeathTrain("Death.train"),
		DeathTrainActionBan("Death.train-actionbar"), DeathTitle("Death.title"), DeathSubtitle("Death.subtitle"),
		DeathPlayers("Death.players"), Reload("Reload"), SleepMsg("Sleep.msg"), StormMsg("Storm.msg"),
		TotemFailMsg("Totem.fail-msg"), TotemUseMsg("Totem.use-msg");

		private final String string;

		MessagesConfigOptions(String string) {
			this.string = string;
		}

		public String getValue() {
			return string;
		}
	}

	public enum DiscordConfigOptions {
		Token("Token"), ChannelsSendMsgChannel("Channels.send-msg-channel"),
		ChannelsCommandChannel("Channels.command-channel"), MessagesChannelDescription("Messages.channel-description"),
		MessagesStartMsg("Messages.start-msg"), MessagesStopMsg("Messages.stop-msg"),
		MessagesJoinMsg("Messages.join-msg"), MessagesFirstJoinMsg("Messages.first-join-msg"),
		MessagesQuitMsg("Messages.quit-msg"),
		MessagesMinecraftToDiscordChatMsg("Messages.minecraft-to-discord-chat-msg"),
		MessagesDiscordToMinecraftChatMsg("Messages.discord-to-minecraft-chat-msg"), MessagesAdvMsg("Messages.adv-msg"),
		MessagesCommandList("Messages.command-list");

		private final String string;

		DiscordConfigOptions(String string) {
			this.string = string;
		}

		public String getValue() {
			return string;
		}
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
