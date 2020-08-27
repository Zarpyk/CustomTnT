package guerrero61.customtnt.death;

import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mainutils.config.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import java.awt.*;

public class Weather implements Listener {

    private final JDA api;

    public Weather(JDA a) {
        api = a;
    }

    @EventHandler
    public void onWeatherStorm(WeatherChangeEvent event) {
        boolean weather = event.getWorld().hasStorm();
        if(weather) {
            String StormMessage = Config.getString(Config.Options.StormEndMsg);
            Bukkit.broadcastMessage(Formatter.FText(StormMessage, null));

            if(Config.getBool(Config.Options.DiscordEnable)) {
                EmbedBuilder startEmbed = new EmbedBuilder().setAuthor(Formatter.RemoveFormat(StormMessage, null),
                        "https://imgur.com/U7bc9ii.png", "https://imgur.com/U7bc9ii.png").setColor(
                        new Color(125, 255, 100));
                for (String channelID : Config.getStringList(Config.Options.ChannelsSendMsg)) {
                    TextChannel textChannel = api.getTextChannelById(channelID);
                    if(textChannel != null) {
                        textChannel.sendMessage(startEmbed.build()).queue();
                    }
                }
            }
        }
    }

}
