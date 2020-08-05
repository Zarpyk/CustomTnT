package guerrero61.customtnt.discord.commands;

import guerrero61.customtnt.Main;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Busca extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (Main.checkCommand("busca", event.getMessage(), event.getChannel())) {
            return;
        }
        boolean haveMention = event.getMessage().getMentionedUsers().size() != 0;

        event.getChannel().sendMessage("<https://lmgtfy.com/?q=Como+buscar+en+google&iie=1> "
                + (haveMention ? event.getMessage().getMentionedUsers().get(0).getAsMention() : "")).queue();
        event.getMessage().delete().queue();
    }
}
