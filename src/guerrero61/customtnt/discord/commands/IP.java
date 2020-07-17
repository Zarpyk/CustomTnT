package guerrero61.customtnt.discord.commands;

import java.awt.*;

import org.bukkit.Bukkit;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.MainUtils.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class IP extends ListenerAdapter {

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (Main.checkCommand("ip", event.getMessage(), event.getChannel())) {
			return;
		}

		String autorAvatar = event.getAuthor().getAvatarUrl();

		EmbedBuilder embed = new EmbedBuilder().setAuthor(
				"IP: " + (Config.getBool("CustomIP.enable") ? Config.getString("CustomIP.IP") : Main.getIp())
						+ (Bukkit.getServer().getPort() == 25565 ? "" : ":" + Bukkit.getServer().getPort()),
				autorAvatar, autorAvatar).setColor(new Color(125, 255, 100));
		event.getChannel().sendMessage(embed.build()).queue();
		event.getMessage().delete().queue();
	}
}
