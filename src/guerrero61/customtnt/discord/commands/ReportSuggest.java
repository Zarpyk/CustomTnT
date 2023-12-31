package guerrero61.customtnt.discord.commands;

import guerrero61.customtnt.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.time.Instant;

public class ReportSuggest extends ListenerAdapter {

    private final JDA api;

    public ReportSuggest(JDA a) {
        api = a;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(Main.checkCommand("report", "suggest", event.getMessage(), event.getChannel())) {
            return;
        }

        String autorAvatar = event.getAuthor().getAvatarUrl();

        EmbedBuilder embed = new EmbedBuilder().setAuthor("Reportar/Sugerir", null, autorAvatar).setThumbnail(
                api.getSelfUser().getAvatarUrl()).addField("Para reportar o sugerir cosas de minecraft o del bot",
                "https://github.com/GuerreroCraft61/TnTReport/issues", false).addField(
                "Para reportar o sugerir cosas de Discord",
                "Puedes usar el canal <#405374777579012107>\n" + "O usar el canal " + "<#401082634433855498>",
                true).setColor(new Color(255, 61, 61)).setFooter(Main.getIp(),
                "https://imgur.com/jrz2u0a.png").setTimestamp(Instant.now());
        event.getChannel().sendMessage(embed.build()).queue();
        event.getMessage().delete().queue();
    }
}
