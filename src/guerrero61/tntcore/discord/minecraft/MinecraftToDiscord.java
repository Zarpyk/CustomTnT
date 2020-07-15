package guerrero61.tntcore.discord.minecraft;

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

import guerrero61.tntcore.Main;
import guerrero61.tntcore.MainUtils.StormActionBar;
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
			jqMsg(player, "Discord.join-msg", new Color(125, 255, 100), !player.hasPermission("essentials.silentjoin"));
		} else {
			main.getConfig().set("Discord.unique-players", Main.getInt("Discord.unique-players") + 1);
			jqMsg(player, "Discord.first-join-msg", new Color(255, 250, 90),
					!player.hasPermission("essentials.silentjoin"));
		}
	}

	@EventHandler
	public void playerLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		jqMsg(player, "Discord.quit-msg", new Color(255, 61, 61), !player.hasPermission("essentials.silentquit"));
	}

	private void jqMsg(Player p, String s, Color c, Boolean b) {
		String playerN = p.getName();
		String urlSkin = getPlayerHeadUrl(p);

		if (!Main.isVanish(p) && b) {
			EmbedBuilder embed = new EmbedBuilder()
					.setAuthor(Main.FTextNPrefix(Main.getString(s).replace("%player%", playerN)), urlSkin, urlSkin)
					.setColor(c);

			textChannel = Objects.requireNonNull(api.getTextChannelById(Main.getString("Discord.send-msg-channel")));
			textChannel.sendMessage(embed.build()).queue();
		}
	}

	@EventHandler
	public void playerChat(AsyncPlayerChatEvent event) {
		String message = event.getMessage();
		Player player = event.getPlayer();
		String playerN = event.getPlayer().getName();
		String role = Main.capitalize(
				Objects.requireNonNull(main.lpApi.getUserManager().getUser(player.getUniqueId())).getPrimaryGroup());

		textChannel.sendMessage(Main.getString("Discord.minecraft-to-discord-chat-msg").replace("%role%", role)
				.replace("%player%", playerN).replace("%msg%", message)).queue();
	}

	@EventHandler
	public void deathMsg(PlayerDeathEvent event) {
		String deathMessage = event.getDeathMessage();
		Player player = event.getEntity();
		String urlSkin = getPlayerHeadUrl(player);

		Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> {
			EmbedBuilder embed = new EmbedBuilder().setAuthor(Main.FTextNPrefix(deathMessage), urlSkin, urlSkin)
					.addField("Horas de tormenta", (StormActionBar.stormTime), false).setColor(new Color(255, 10, 10));
			textChannel = Objects.requireNonNull(api.getTextChannelById(Main.getString("Discord.send-msg-channel")));
			textChannel.sendMessage(embed.build()).queue();
		}, 20L);

	}

	@EventHandler
	public void advancementMsg(PlayerAdvancementDoneEvent event) {
		if (event.getAdvancement().getKey().getKey().contains("recipe/"))
			return;
		Advancement advancement = event.getAdvancement();
		String rawAdvancementName = advancement.getKey().getKey();
		String advancementName = Arrays
				.stream(rawAdvancementName.substring(rawAdvancementName.lastIndexOf("/") + 1).toLowerCase().split("_"))
				.map(s -> s.substring(0, 1).toUpperCase() + s.substring(1)).collect(Collectors.joining(" "));
		Player player = event.getPlayer();
		String playerN = player.getName();
		String urlSkin = getPlayerHeadUrl(player);

		EmbedBuilder embed = new EmbedBuilder().setAuthor(Main.FTextNPrefix(
				Main.getString("Discord.adv-msg").replace("%player%", playerN).replace("%adv%", advancementName)),
				urlSkin, urlSkin).setColor(new Color(125, 255, 100));

		textChannel = Objects.requireNonNull(api.getTextChannelById(Main.getString("Discord.send-msg-channel")));
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
