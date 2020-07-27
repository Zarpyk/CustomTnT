package guerrero61.customtnt.events;

import java.awt.*;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;

import guerrero61.customtnt.discord.minecraft.MinecraftToDiscord;
import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mainutils.config.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;

public class Totem implements Listener {

	private final JDA api;

	public Totem(JDA a) {
		api = a;
	}

	@EventHandler
	public void totemNerf(EntityResurrectEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		if (((Player) event.getEntity()).getInventory().getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING
				|| ((Player) event.getEntity()).getInventory().getItemInOffHand()
						.getType() == Material.TOTEM_OF_UNDYING) {
			if (!Config.getBool(Config.Options.TotemFailEnable))
				return;
			Player player = (Player) event.getEntity();
			String playerN = player.getName();
			int failProb = Config.getInt(Config.Options.TotemProbability);

			if (failProb >= 101) {
				failProb = 100;
			} else if (failProb < 1) {
				failProb = 1;
			}
			int random = (int) (Math.random() * 100) + 1;
			int resta = 100 - failProb;
			if (random >= resta) {
				message(player, random, resta, "=>", true);
				event.setCancelled(true);
			} else {
				message(player, random, resta, "!=", false);
			}
		}
	}

	private void message(Player player, int random, int resta, String symbol, boolean fail) {
		String totemMessage = Formatter.FText(
				Config.getString(Config.Options.TotemUseMsg).replace("%porcent%", symbol)
						.replace("%totem_fail%", String.valueOf(random)).replace("%number%", String.valueOf(resta)),
				true, player);
		String totemFail = Formatter.FText(Config.getString(Config.Options.TotemFailMsg), true, player);

		if (!fail) {
			Bukkit.broadcastMessage(Formatter.FText(totemMessage));
			sendDiscordMsg(player, totemMessage, new Color(255, 250, 90));
		} else {
			Bukkit.broadcastMessage(Formatter.FText(totemMessage));
			Bukkit.broadcastMessage(Formatter.FText(totemFail));
			sendDiscordMsg(player, totemMessage + " " + totemFail, new Color(255, 10, 10));
		}
	}

	private void sendDiscordMsg(Player player, String msg, Color color) {
		String urlSkin = MinecraftToDiscord.getPlayerHeadUrl(player);
		EmbedBuilder embed = new EmbedBuilder().setAuthor(Formatter.RemoveFormat(msg), urlSkin, urlSkin)
				.setColor(color);
		TextChannel textChannel = Objects
				.requireNonNull(api.getTextChannelById(Config.getString(Config.Options.ChannelsSendMsg)));
		textChannel.sendMessage(embed.build()).queue();
	}
}
