package guerrero61.tntcore.commands.discord;

import guerrero61.tntcore.Main;
import org.bukkit.Bukkit;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class Summon implements MessageCreateListener {

    private final Main main;

    private final Long id = 290223330773172225L;

    public Summon(Main m) {
        main = m;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if (!event.getMessageContent().equalsIgnoreCase("/summon")
                || event.getMessage().getAuthor().getId() != id) {
            main.getLogger().warning(Long.toString(event.getMessage().getAuthor().getId()));
            return;
        }

        main.getLogger().warning(Long.toString(event.getMessage().getAuthor().getId()));

        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "boss summonp PhyPsi15 BossPalPepsi");
        event.getMessage().delete();
    }
}
