package guerrero61.customtnt.discord.events;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mainutils.config.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.Bukkit;

import java.awt.*;

public class ReloadStatus {

    public static void startStopToDiscord(String url, JDA api, String msg, Color color, String status) {
        EmbedBuilder startEmbed = new EmbedBuilder().setAuthor(msg, url, url).setColor(color);
        for (String channelID : Config.getStringList(Config.Options.ChannelsSendMsg)) {
            TextChannel textChannel = api.getTextChannelById(channelID);
            if (textChannel != null) {
                textChannel.sendMessage(startEmbed.build()).complete();
            }
        }
        reloadStatus(api, status);
    }

    public static void reloadStatus(JDA api, String status) {
        for (String channelID : Config.getStringList(Config.Options.ChannelsSendMsg)) {
            TextChannel textChannel = api.getTextChannelById(channelID);
            if (textChannel != null) {
                textChannel.getManager()
                        .setTopic(Formatter.FText(
                                Config.getString(Config.Options.ChannelDescription)
                                        .replace("%status%",
                                                "Servidor " + status
                                                        + (status.equals("online") ? (" | " + Main
                                                        .getPlayerCount()) : ""))
                                        .replace("%unique-players%", Integer
                                                .toString(Bukkit.getOfflinePlayers().length)),
                                true))
                        .queue();
            }
        }
    }
}
