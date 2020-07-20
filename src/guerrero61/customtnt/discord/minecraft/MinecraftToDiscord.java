package guerrero61.customtnt.discord.minecraft;

import java.awt.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.MainUtils.Config;
import guerrero61.customtnt.MainUtils.StormActionBar;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import skinsrestorer.bukkit.SkinsRestorer;

public class MinecraftToDiscord implements Listener {

	private final Main main;
	private final JDA api;

	private TextChannel textChannel;

	public MinecraftToDiscord(Main m, JDA a) {
		main = m;
		api = a;

	}

	@EventHandler
	public void playerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (player.hasPlayedBefore()) {
			jqMsg(player, Config.Options.MessagesJoin, new Color(125, 255, 100),
					!player.hasPermission("essentials.silentjoin"));
		} else {
			jqMsg(player, Config.Options.MessagesFirstJoin, new Color(255, 250, 90),
					!player.hasPermission("essentials.silentjoin"));
		}
	}

	@EventHandler
	public void playerLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		jqMsg(player, Config.Options.MessagesQuit, new Color(255, 61, 61),
				!player.hasPermission("essentials.silentquit"));
	}

	private void jqMsg(Player p, Config.Options co, Color c, Boolean b) {
		String playerN = p.getName();
		String urlSkin = getPlayerHeadUrl(p);

		if (!Main.isVanish(p) && b) {
			EmbedBuilder embed = new EmbedBuilder()
					.setAuthor(Main.FTextNPrefix(Config.getString(co).replace("%player%", playerN)), urlSkin, urlSkin)
					.setColor(c);

			textChannel = Objects
					.requireNonNull(api.getTextChannelById(Config.getString(Config.Options.ChannelsSendMsg)));
			textChannel.sendMessage(embed.build()).queue();
		}
	}

	@EventHandler
	public void playerChat(AsyncPlayerChatEvent event) {
		if (!event.isCancelled()) {
			String message = event.getMessage();
			Player player = event.getPlayer();
			String playerN = event.getPlayer().getName();
			String role = Objects.requireNonNull(main.lpApi.getUserManager().getUser(player.getUniqueId()))
					.getPrimaryGroup();
			String roleC = role.equals("default") ? "" : "**" + Main.capitalize(role) + "**";

			textChannel.sendMessage(Config.getString(Config.Options.MessagesMinecraftToDiscordChat)
					.replace("%role%", roleC).replace("%player%", playerN).replace("%msg%", message)).queue();
		}
	}

	@EventHandler
	public void deathMsg(PlayerDeathEvent event) {
		String deathMessage = event.getDeathMessage();
		Player player = event.getEntity();
		String urlSkin = getPlayerHeadUrl(player);

		Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> {
			assert deathMessage != null;
			EmbedBuilder embed = new EmbedBuilder().setAuthor(Main.FTextNPrefix(deathMessage), urlSkin, urlSkin);
			if (Config.getBool(Config.Options.StormEnable)) {
				embed.addField("Horas de tormenta", (StormActionBar.stormTime), false);
			}
			embed.setColor(new Color(255, 10, 10));
			textChannel = Objects
					.requireNonNull(api.getTextChannelById(Config.getString(Config.Options.ChannelsSendMsg)));
			textChannel.sendMessage(embed.build()).queue();
		}, 20L);

	}

	@EventHandler
	public void advancementMsg(PlayerAdvancementDoneEvent event) {
		Advancement advancement = event.getAdvancement();
		String rawAdvancementName = advancement.getKey().getKey();
		if (rawAdvancementName.contains("recipes/") || rawAdvancementName.contains("recipe/")
				|| rawAdvancementName.equals("root")) {
			return;
		}
		String advancementName = Arrays
				.stream(rawAdvancementName.substring(rawAdvancementName.lastIndexOf("/") + 1).toLowerCase().split("_"))
				.map(s -> s.substring(0, 1).toUpperCase() + s.substring(1)).collect(Collectors.joining(" "));
		Player player = event.getPlayer();
		String playerN = player.getName();
		String urlSkin = getPlayerHeadUrl(player);

		EmbedBuilder embed = new EmbedBuilder()
				.setAuthor(Main.FTextNPrefix(Config.getString(Config.Options.MessagesAdvancement)
						.replace("%player%", playerN).replace("%adv%", advancementName)), urlSkin, urlSkin)
				.setColor(new Color(125, 255, 100));

		textChannel = Objects.requireNonNull(api.getTextChannelById(Config.getString(Config.Options.ChannelsSendMsg)));
		textChannel.sendMessage(embed.build()).queue();

	}

	public static String getPlayerHeadUrl(Player player) {
		String playerN = player.getName();
		String skin = SkinsRestorer.getInstance().getSkinStorage().getPlayerSkin(playerN);

		if (skin != null) {
			return "https://www.mc-heads.net/avatar/" + skin.replaceAll("\\s", "") + ".png";
		} else {
			return "https://www.mc-heads.net/avatar/" + playerN.replaceAll("\\s", "") + ".png";
		}
	}

}