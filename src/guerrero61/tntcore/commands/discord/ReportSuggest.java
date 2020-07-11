package guerrero61.tntcore.commands.discord;

import org.bukkit.Bukkit;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.awt.*;

public class ReportSuggest implements MessageCreateListener {

    private final DiscordApi api;

    public ReportSuggest(DiscordApi a) {
        api = a;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if (!event.getMessageContent().equalsIgnoreCase("/report")
                && !event.getMessageContent().equalsIgnoreCase("/suggest")) {
            return;
        }

        Icon avatar = api.getYourself().getAvatar();
        String ip = Bukkit.getServer().getIp() + ":" + Bukkit.getServer().getPort();

        EmbedBuilder embed = new EmbedBuilder().setAuthor("Reportar/Sugerir", "", event.getMessageAuthor().getAvatar()).setThumbnail(avatar)
                .addField("Para reportar o sugerir cosas de minecraft",
                        "https://github.com/GuerreroCraft61/TnTCoreReport/issues")
                .addField("Para reportar o sugerir cosas del bot",
                        "https://github.com/GuerreroCraft61/TnTCoreReport/issues")
                .addField("Para reportar a un usuario (Discord y Minecraft)",
                        "Puedes usar el canal <#405374777579012107>")
                .addField("Para reportar o sugerir cosas de Discord",
                        "Puedes usar el canal <#405374777579012107>\nO usar el canal <#401082634433855498>", true)
                .setUrl("https://github.com/GuerreroCraft61/TnTCoreReport/issues").setColor(new Color(255, 61, 61))
                .setFooter(ip, "https://imgur.com/jrz2u0a.png").setTimestampToNow();

        new MessageBuilder().setEmbed(embed).send(event.getChannel());
        event.getMessage().delete();
    }
}
