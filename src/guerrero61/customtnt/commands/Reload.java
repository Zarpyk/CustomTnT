package guerrero61.customtnt.commands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.MainUtils.DisableBot;
import guerrero61.customtnt.MainUtils.Formatter;
import guerrero61.customtnt.MainUtils.RegisterEvents;
import guerrero61.customtnt.MainUtils.Config.Config;
import guerrero61.customtnt.MainUtils.Config.DiscordConfig;
import guerrero61.customtnt.MainUtils.Config.MessagesConfig;

public class Reload {

	public Reload(boolean isPlayer, CommandSender sender, Main main) {
		DisableBot.Disable(main);
		Main.configMap = null;
		Main.config = null;
		main.configPath = null;
		main.messagesConfig = null;
		main.messagesConfigFile = null;
		main.discordConfig = null;
		main.discordConfigFile = null;

		Main.configMap = new HashMap<>();
		RegisterEvents registerEvents = new RegisterEvents();
		registerEvents.registerConfig(main);
		Main.configMap.put(Config.CONFIG.Main, Main.config);
		registerEvents.registerMessagesConfig(main);
		Main.configMap.put(Config.CONFIG.Messages,
				main.messagesConfig == null ? MessagesConfig.getMessagesConfig(main) : main.messagesConfig);
		registerEvents.registerDiscordConfig(main);
		Main.configMap.put(Config.CONFIG.Discord,
				main.discordConfig == null ? DiscordConfig.getDiscordConfig(main) : main.discordConfig);

		if (main.api == null) {
			registerEvents.registerDiscord(main);
			main.api.cancelRequests();
		}
		if (isPlayer) {
			sender.sendMessage(Formatter.FText(Config.getString(Config.Options.Reload)));
		} else {
			Bukkit.getConsoleSender().sendMessage(Formatter.FText(Config.getString(Config.Options.Reload)));
		}
	}
}
