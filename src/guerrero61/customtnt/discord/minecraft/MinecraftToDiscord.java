package guerrero61.customtnt.discord.minecraft;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.mainutils.Avatar;
import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mainutils.StormActionBar;
import guerrero61.customtnt.mainutils.config.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.awt.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class MinecraftToDiscord implements Listener {

    private final Main main;
    private final JDA api;

    private TextChannel textChannel;

    public MinecraftToDiscord(Main m, JDA a) {
        main = m;
        api = a;

    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(player.hasPlayedBefore()) {
            jqMsg(player, Config.Options.MessagesJoin, new Color(125, 255, 100),
                    !player.hasPermission("essentials.silentjoin"));
        } else {
            jqMsg(player, Config.Options.MessagesFirstJoin, new Color(255, 250, 90),
                    !player.hasPermission("essentials.silentjoin"));
        }
    }

    @EventHandler
    public void playerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        jqMsg(player, Config.Options.MessagesQuit, new Color(255, 61, 61),
                !player.hasPermission("essentials.silentquit"));
    }

    private void jqMsg(Player player, Config.Options co, Color c, Boolean b) {
        if(!Main.isVanish(player) && b) {
            EmbedBuilder embed = new EmbedBuilder().setAuthor(Formatter.RemoveFormat(Config.getString(co), player),
                    null, "attachment://avatar.png").setColor(c);
            for (String channelID : Config.getStringList(Config.Options.ChannelsSendMsg)) {
                textChannel = api.getTextChannelById(channelID);
                if(textChannel != null) {
                    textChannel.sendFile(Avatar.getPlayerAvatar(player), "avatar.png").embed(embed.build()).queue();
                }
            }
        }
    }

    @EventHandler
    public void playerChat(AsyncPlayerChatEvent event) {
        if(!event.isCancelled()) {
            String message = Formatter.RemoveFormat(event.getMessage());
            Player player = event.getPlayer();
            String sendMessage = Formatter.RemoveFormat(Config.getString(Config.Options.MessagesMinecraftToDiscordChat),
                    player);
            for (String rank : Config.getStringList(Config.Options.MessagesRemoveRank)) {
                if(sendMessage.contains(rank)) {
                    sendMessage = sendMessage.replace(rank, "");
                }
            }
            sendMessage = sendMessage.replace("%msg%", message).replace("@", "");
            for (String channelID : Config.getStringList(Config.Options.ChannelsSendMsg)) {
                textChannel = api.getTextChannelById(channelID);
                if(textChannel != null) {
                    textChannel.sendMessage(sendMessage).queue();
                }
            }
        }
    }

    @EventHandler
    public void deathMsg(PlayerDeathEvent event) {
        String deathMessage = event.getDeathMessage();
        Player player = event.getEntity();

        Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> {
            assert deathMessage != null;
            EmbedBuilder embed = new EmbedBuilder().setAuthor(Formatter.FText(deathMessage, true), null,
                    "attachment://avatar.png");
            if(Config.getBool(Config.Options.StormEnable)) {
                embed.addField("Horas de tormenta", (StormActionBar.stormTime), false);
            }
            embed.setColor(new Color(255, 10, 10));
            for (String channelID : Config.getStringList(Config.Options.ChannelsSendMsg)) {
                textChannel = api.getTextChannelById(channelID);
                if(textChannel != null) {
                    textChannel.sendFile(Avatar.getPlayerAvatar(player), "avatar.png").embed(embed.build()).queue();
                }
            }
        }, 20L);

    }

    @EventHandler
    public void advancementMsg(PlayerAdvancementDoneEvent event) {
        if(!Objects.requireNonNull(
                Objects.requireNonNull(Bukkit.getWorld(Config.getString(Config.Options.MainWorld))).getGameRuleValue(
                        GameRule.ANNOUNCE_ADVANCEMENTS))) {
            return;
        }
        Advancement advancement = event.getAdvancement();
        String rawAdvancementName = advancement.getKey().getKey();
        Main.consoleMsg(Formatter.FText("&e" + event.getPlayer().getName() + " complete: " + rawAdvancementName));
        if(rawAdvancementName.contains("recipes/") || rawAdvancementName.contains("recipe/") ||
           rawAdvancementName.contains("root") || rawAdvancementName.contains("branch")) {
            return;
        }
        String advancementName = Arrays.stream(
                rawAdvancementName.substring(rawAdvancementName.lastIndexOf("/") + 1).toLowerCase().split("_")).map(
                s -> s.substring(0, 1).toUpperCase() + s.substring(1)).collect(Collectors.joining(" "));
        if(advancementName.equalsIgnoreCase("A Wizards Breakfast Fail") ||
           advancementName.equalsIgnoreCase("Castaway Fail") ||
           advancementName.equalsIgnoreCase("Just Keeps Going Fail") || advancementName.equalsIgnoreCase("Root")) {
            return;
        }

        Player player = event.getPlayer();

        EmbedBuilder embed = new EmbedBuilder().setAuthor(
                Formatter.FText(Config.getString(Config.Options.MessagesAdvancement).replace("%adv%", advancementName),
                        true, player), null, "attachment://avatar.png").setColor(new Color(125, 255, 100));

        for (String channelID : Config.getStringList(Config.Options.ChannelsSendMsg)) {
            textChannel = api.getTextChannelById(channelID);
            if(textChannel != null) {
                textChannel.sendFile(Avatar.getPlayerAvatar(player), "avatar.png").embed(embed.build()).queue();
            }
        }
    }
}
