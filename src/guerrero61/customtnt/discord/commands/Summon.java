package guerrero61.customtnt.discord.commands;

import guerrero61.customtnt.Main;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;

import java.util.concurrent.ExecutionException;

public class Summon extends ListenerAdapter {

    private final Main main;

    private final String id = "290223330773172225L";

    public Summon(Main m) {
        main = m;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (Main.checkCommand("summon", event.getMessage(), event.getChannel())
                || !event.getAuthor().getId().equals("290223330773172225")) {
            return;
        }
        try {
            boolean success = Bukkit
                    .getScheduler().callSyncMethod(main, () -> Bukkit
                            .dispatchCommand(main.getServer().getConsoleSender(), "boss summonp PhyPsi15 BossPalPepsi"))
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        event.getMessage().delete().queue();
    }
}
