package guerrero61.customtnt.discord.minecraft;

import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mainutils.config.Config;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;

import java.util.Objects;

public class DiscordToMinecraft extends ListenerAdapter {

    private final JDA api;

    public DiscordToMinecraft(JDA a) {
        api = a;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().getId().equals(api.getSelfUser().getId()) || event.getAuthor().isBot()) {
            return;
        }
        boolean isChannel = false;
        for (String channelID : Config.getStringList(Config.Options.ChannelsSendMsg)) {
            if (event.getChannel().getId().equals(channelID)) {
                for (String commandList : Config.getStringList(Config.Options.MessagesCommandList)) {
                    if (event.getMessage().getContentDisplay().startsWith(commandList)) {
                        return;
                    }
                }
                String nickname = Objects.requireNonNull(event.getMember()).getNickname();
                if (nickname == null) {
                    nickname = event.getAuthor().getName();
                }
                Bukkit.broadcastMessage(Formatter
                        .FText(Config.getString(Config.Options.MessagesDiscordToMinecraftChat)
                                .replace("%nick%", nickname)
                                .replace("%have_image%", (event.getMessage().getAttachments().size() > 0 ? Config
                                        .getString(Config.Options.MessagesHaveImageText) : ""))
                                .replace("%msg%", Formatter
                                        .FText(event.getMessage().getContentDisplay(), true)), true));
                for (String channelID2 : Config.getStringList(Config.Options.ChannelsSendMsg)) {
                    if (!event.getChannel().getId().equals(channelID2)) {
                        TextChannel textChannel = api.getTextChannelById(channelID2);
                        if (textChannel != null) {
                            String sendMessage = Config.getString(Config.Options.MessagesDiscordToOtherDiscordChat)
                                    .replace("%user_name%", event.getAuthor().getName())
                                    .replace("%msg%", event.getMessage().getContentDisplay()).replace("@", "");
                            textChannel.sendMessage(sendMessage).queue();
                        }
                    }
                }
                break;
            }
        }
    }
}
