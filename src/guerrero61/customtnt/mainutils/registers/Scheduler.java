package guerrero61.customtnt.mainutils.registers;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.discord.events.ReloadStatus;
import guerrero61.customtnt.formatters.tablist.TabList;
import guerrero61.customtnt.mainutils.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;

public class Scheduler {

    public void startMessageDelayScheduler(Main main) {
        Bukkit.getScheduler()
                .scheduleSyncDelayedTask(main,
                        () -> ReloadStatus.startStopToDiscord("https://imgur.com/uIcXfam.png", main.api,
                                Config.getString(Config.Options.MessagesStart), new Color(125, 255, 100), "online"),
                        25L);
    }

    public void reloadStatusScheduler(Main main) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> ReloadStatus.reloadStatus(main.api, "online"),
                12000L, 12000L);
    }

    public void registerScoreboard(Main main) {
        TabList tabList = new TabList(main);
        new BukkitRunnable() {
            public void run() {
                tabList.reloadTab();
            }
        }.runTaskTimer(main, 0, 200L);
    }

    @Deprecated
    public void registerDependencies(Main main) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> new Registers().registerDependencies(main), 25L);
    }
}
