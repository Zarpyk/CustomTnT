package guerrero61.customtnt.discord.commands;

import guerrero61.customtnt.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.time.Instant;

public class Help extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (Main.checkCommand("help", event.getMessage(), event.getChannel())) {
            return;
        }

        String autorAvatar = event.getAuthor().getAvatarUrl();

        EmbedBuilder embed = new EmbedBuilder().setAuthor("Comandos disponibles", null, autorAvatar)
                .addField("/info", "Sirve para ver la información del server de minecraft", false)
                .addField("/report - /suggest", "Sirve para ver donde reportar o sugerir", false)
                .addField("/ip", "Sirve para ver el ip", false)
                .addField("/busca <tag>", "Para ayudarle a alguien a aprender a buscar", false)
                .addField("/mapa", "Para ver el mapa del mundo del servidor", false)
                .setColor(new Color(255, 61, 61)).setFooter(Main.getIp(), "https://imgur.com/jrz2u0a.png")
                .setTimestamp(Instant.now());

        event.getChannel().sendMessage(embed.build()).queue();
        event.getMessage().delete().queue();
    }
}
