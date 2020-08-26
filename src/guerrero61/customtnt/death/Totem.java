package guerrero61.customtnt.death;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.mainutils.Avatar;
import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mainutils.config.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;

import java.awt.*;

public class Totem implements Listener {

    private final JDA api;

    public Totem(JDA a) {
        api = a;
    }

    @EventHandler
    public void totemNerf(EntityResurrectEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;
        if (((Player) event.getEntity()).getInventory().getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING
                || ((Player) event.getEntity()).getInventory().getItemInOffHand()
                .getType() == Material.TOTEM_OF_UNDYING) {
            if (!Config.getBool(Config.Options.TotemFailEnable))
                return;
            Player player = (Player) event.getEntity();
            String playerN = player.getName();
            int failProb = Config.getInt(Config.Options.TotemProbability);

            if (failProb > 100) {
                failProb = 100;
            } else if (failProb < 0) {
                failProb = 0;
            }
            int random = Main.random(1, 99);
            int resta = 100 - failProb;
            if (random >= resta) {
                message(player, random, resta, ">=", true);
                event.setCancelled(true);
            } else {
                message(player, random, resta, "<", false);
            }
        }
    }

    private void message(Player player, int random, int resta, String symbol, boolean fail) {
        String totemMessage = Formatter.FText(
                Config.getString(Config.Options.TotemUseMsg).replace("%porcent%", symbol)
                        .replace("%totem_fail%", Integer.toString(random)).replace("%number%", Integer.toString(resta)),
                true, player);
        String totemFail = Formatter.FText(Config.getString(Config.Options.TotemFailMsg), true, player);

        if (!fail) {
            Bukkit.broadcastMessage(Formatter.FText(totemMessage));
            if (Config.getBool(Config.Options.DiscordEnable)) {
                sendDiscordMsg(player, totemMessage, new Color(255, 250, 90));
            }
        } else {
            Bukkit.broadcastMessage(Formatter.FText(totemMessage));
            Bukkit.broadcastMessage(Formatter.FText(totemFail));
            if (Config.getBool(Config.Options.DiscordEnable)) {
                sendDiscordMsg(player, totemMessage + " " + totemFail, new Color(255, 10, 10));
            }
        }
    }

    private void sendDiscordMsg(Player player, String msg, Color color) {
        EmbedBuilder embed = new EmbedBuilder().setAuthor(Formatter.RemoveFormat(msg), null, "attachment://avatar.png")
                .setColor(color);
        for (String channelID : Config.getStringList(Config.Options.ChannelsSendMsg)) {
            TextChannel textChannel = api.getTextChannelById(channelID);
            if (textChannel != null) {
                textChannel.sendFile(Avatar.getPlayerAvatar(player), "avatar.png").embed(embed.build()).queue();
            }
        }
    }
}
