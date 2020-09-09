package guerrero61.customtnt.discord.commands;

import guerrero61.customtnt.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class IP extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(Main.checkCommand("ip", event.getMessage(), event.getChannel())) {
            return;
        }

        String autorAvatar = event.getAuthor().getAvatarUrl();

        EmbedBuilder embed = new EmbedBuilder().setAuthor("IP: " + Main.getIp(), null, autorAvatar).setColor(
                new Color(125, 255, 100));
        event.getChannel().sendMessage(embed.build()).queue();
        event.getMessage().delete().queue();
    }
}
