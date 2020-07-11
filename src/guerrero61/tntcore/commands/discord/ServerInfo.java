package guerrero61.tntcore.commands.discord;

import guerrero61.tntcore.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.awt.*;
import java.util.Objects;
import java.util.stream.Collectors;

public class ServerInfo implements MessageCreateListener {

    private final DiscordApi api;
    private final Main main;

    public ServerInfo(DiscordApi a, Main m) {
        api = a;
        main = m;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if (!event.getMessageContent().equalsIgnoreCase("/info")) {
            return;
        }

        String onlinePlayers = Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.joining(","))
                .replace(",", "\n");
        int onlinePlayersCount = main.getServer().getOnlinePlayers().size();

        Icon avatar = api.getYourself().getAvatar();
        String ip = Bukkit.getServer().getIp() + ":" + Bukkit.getServer().getPort();

        EmbedBuilder embed = new EmbedBuilder().setAuthor("Información del server | TnTServer", "", event.getMessageAuthor().getAvatar()).setThumbnail(avatar)
                .addField("Versión",
                        Bukkit.getVersion().replace("git-", "") + "\n"
                                + (Bukkit.getOnlineMode() ? "Premium" : "Premium/No Premium"),
                        true)
                .addField("Jugadores " + onlinePlayersCount + "/" + main.getServer().getMaxPlayers(),
                        (onlinePlayersCount == 0) ? "No hay nadie jugando" : onlinePlayers, true)
                .addField("Horas de tormenta", (Objects.requireNonNull(Bukkit.getWorld("world")).hasStorm() ? main.stormTime : "No hay tormenta"))
                .setColor(new Color(255, 61, 61)).setFooter(ip, "https://imgur.com/jrz2u0a.png").setTimestampToNow();

        new MessageBuilder().setEmbed(embed).send(event.getChannel());
        event.getMessage().delete();
    }
}
