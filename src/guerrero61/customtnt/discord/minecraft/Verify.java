package guerrero61.customtnt.discord.minecraft;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mainutils.config.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Verify extends ListenerAdapter implements CommandExecutor, TabCompleter {

	private final Main main;
	private static File verifyFile;
	private static FileConfiguration verifyConfig;

	boolean isPlayer;
	Player player;

	public Verify(Main m) {
		main = m;
		verifyFile = new File(m.getDataFolder(), "discord-verify.yml");
		verifyConfig = YamlConfiguration.loadConfiguration(verifyFile);
		if (!verifyFile.exists()) {
			try {
				//noinspection ResultOfMethodCallIgnored
				verifyFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void set(String key, String value) {
		verifyConfig.set(key, value);
		try {
			verifyConfig.save(verifyFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void set(String key, Boolean value) {
		verifyConfig.set(key, value);
		try {
			verifyConfig.save(verifyFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getString(String key) {
		return verifyConfig.getString(key);
	}

	public static Boolean getBool(String key) {
		return verifyConfig.getBoolean(key);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			isPlayer = true;
			player = (Player) sender;
		} else {
			isPlayer = false;
			Main.consoleMsg(Formatter.FText(Config.getString(Config.Options.ErrorsNoConsole)));
			return true;
		}
		if (args.length == 1 && args[0].contains("#") && args[0].split("#").length == 2 && !args[0].endsWith("#")) {
			if (args[0].split("#")[0].length() <= 32 && args[0].split("#")[1].length() == 4
					&& Main.isNumeric(args[0].split("#")[1])) {
				for (String key : Objects.requireNonNull(verifyConfig.getConfigurationSection("")).getKeys(false)) {
					Main.debug(key);
					if (getString(key + ".nick") != null) {
						if (getString(key + ".nick").equals(player.getName())) {
							set(key + ".nick", (String) null);
							set(key + ".verified", (String) null);
							set(key, (String) null);
							main.perms.playerRemove(player, "suffix.20."
									+ Formatter.FText(Config.getString(Config.Options.VerifyPrefix), true, player));
						}
					}
				}
				if (getString(args[0] + ".nick") == null || !getBool(args[0] + ".verified")) {
					set(args[0] + ".nick", player.getName());
					set(args[0] + ".verified", false);
					player.sendMessage(Formatter.FText(Config.getString(Config.Options.VerifyDiscord), player));
				} else if (getString(args[0] + ".nick").equals(player.getName())) {
					player.sendMessage(Formatter.FText(Config.getString(Config.Options.VerifyYouVerified), player));
				} else {
					player.sendMessage(
							Formatter.FText(Config.getString(Config.Options.VerifyUserAlredyVerified), player));
				}
			} else {
				player.sendMessage(Formatter.FText(Config.getString(Config.Options.VerifyCommandError), player));
			}
		} else {
			player.sendMessage(Formatter.FText(Config.getString(Config.Options.VerifyCommandError), player));
		}
		return true;
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (Main.checkCommand("verify", event.getMessage(), event.getChannel())) {
			return;
		}

		String[] args = event.getMessage().getContentDisplay().split(" ");
		String autorAvatar = event.getAuthor().getAvatarUrl();
		EmbedBuilder embed;

		if (args[1].equals(getString(event.getAuthor().getAsTag() + ".nick"))) {
			Player player = Bukkit.getPlayer(getString(event.getAuthor().getAsTag() + ".nick"));
			if (player == null) {
				embed = new EmbedBuilder()
						.setAuthor(Formatter.RemoveFormat(Config.getString(Config.Options.VerifyNoOnline)), autorAvatar,
								autorAvatar)
						.setColor(new Color(255, 61, 61));
			} else {
				if (!getBool(event.getAuthor().getAsTag() + ".verified")) {
					main.perms.playerAdd(player, "suffix.20."
							+ Formatter.FText(Config.getString(Config.Options.VerifyPrefix), true, player));
					set(event.getAuthor().getAsTag() + ".verified", true);
					embed = new EmbedBuilder()
							.setAuthor(Formatter.RemoveFormat(Config.getString(Config.Options.VerifySuccess)),
									autorAvatar, autorAvatar)
							.setColor(new Color(125, 255, 100));
				} else {
					embed = new EmbedBuilder()
							.setAuthor(Formatter.RemoveFormat(Config.getString(Config.Options.VerifyYouVerified)),
									autorAvatar, autorAvatar)
							.setColor(new Color(255, 61, 61));
				}
			}
		} else if (getString(event.getAuthor().getAsTag() + ".nick") == null) {
			embed = new EmbedBuilder()
					.setAuthor(Formatter.RemoveFormat(Config.getString(Config.Options.VerifyError)
							.replace("%discord_tag%", event.getAuthor().getAsTag())), autorAvatar, autorAvatar)
					.setColor(new Color(255, 61, 61));
		} else {
			embed = new EmbedBuilder()
					.setAuthor(Formatter.RemoveFormat(Config.getString(Config.Options.VerifyErrorNick)), autorAvatar,
							autorAvatar)
					.setColor(new Color(255, 61, 61));
		}

		event.getChannel().sendMessage(embed.build()).queue();
		event.getMessage().delete().queue();
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (args.length == 1) {
			List<String> subcommands = new ArrayList<>();
			subcommands.add("TuTagDeDiscord#0000");
			return subcommands;
		}
		return null;
	}
}
