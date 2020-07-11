package guerrero61.tntcore.events;

import guerrero61.tntcore.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class Weather implements Listener {

    @EventHandler
    public void onWeatherStorm(WeatherChangeEvent event) {
        boolean weather = event.getWorld().hasStorm();
        if (weather) {
            String StormMessage = Main.getString("Storm.end-msg");
            Bukkit.broadcastMessage(Main.FText(StormMessage));
        }
    }

}
