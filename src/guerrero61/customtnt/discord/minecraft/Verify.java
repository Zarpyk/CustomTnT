package guerrero61.customtnt.discord.minecraft;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mainutils.config.Config;
import guerrero61.customtnt.mainutils.config.ConfigClass;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Verify extends ListenerAdapter implements CommandExecutor, TabCompleter {

    private final Main main;
    private final ConfigClass configClass;

    boolean isPlayer;
    Player player;

    public Verify(Main m) {
        main = m;
        configClass = new ConfigClass("discord-verify");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(Config.getBool(Config.Options.DiscordEnable) && Config.getBool(Config.Options.VerifyEnable)) {
            if(sender instanceof Player) {
                isPlayer = true;
                player = (Player) sender;
            } else {
                isPlayer = false;
                Main.consoleMsg(Formatter.FText(Config.getString(Config.Options.ErrorsNoConsole)));
                return true;
            }
            if(args.length == 1 && args[0].contains("#") && args[0].split("#").length == 2 && !args[0].endsWith("#") &&
               !args[0].startsWith("#")) {
                if(args[0].split("#")[0].length() <= 32 && args[0].split("#")[1].length() == 4 &&
                   Main.isNumeric(args[0].split("#")[1])) {
                    for (String key : Objects.requireNonNull(configClass.getConfigurationSection("")).getKeys(false)) {
                        Main.debug(key);
                        if(configClass.getString(key + ".nick") != null) {
                            if(configClass.getString(key + ".nick").equals(player.getName())) {
                                configClass.set(key + ".nick", (String) null);
                                configClass.set(key + ".verified", (String) null);
                                configClass.set(key, (String) null);
                                main.perms.playerRemove(player, "suffix.20." + Formatter.FText(
                                        Config.getString(Config.Options.VerifyPrefix), true, player));
                            }
                        }
                    }
                    if(configClass.getString(args[0] + ".nick") == null ||
                       !configClass.getBool(args[0] + ".verified")) {
                        configClass.set(args[0] + ".nick", player.getName());
                        configClass.set(args[0] + ".verified", false);
                        player.sendMessage(Formatter.FText(Config.getString(Config.Options.VerifyDiscord), player));
                    } else if(configClass.getString(args[0] + ".nick").equals(player.getName())) {
                        player.sendMessage(Formatter.FText(Config.getString(Config.Options.VerifyYouVerified), player));
                    } else {
                        player.sendMessage(
                                Formatter.FText(Config.getString(Config.Options.VerifyUserAlredyVerified), player));
                    }
                } else {
                    player.sendMessage(Formatter.FText(Config.getString(Config.Options.VerifyCommandError), player));
                }
            } else {
                player.sendMessage(Formatter.FText(Config.getString(Config.Options.VerifyCommandError), player));
            }
        }
        return true;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(Main.checkCommand("verify", event.getMessage(), event.getChannel())) {
            return;
        }

        String[] args = event.getMessage().getContentDisplay().split(" ");
        if(args.length < 1) {
            return;
        }
        String autorAvatar = event.getAuthor().getAvatarUrl();
        EmbedBuilder embed;

        if(args[1].equals(configClass.getString(event.getAuthor().getAsTag().replace(" ", "_") + ".nick"))) {
            Player player = Bukkit.getPlayer(
                    configClass.getString(event.getAuthor().getAsTag().replace(" ", "_") + ".nick"));
            if(player == null) {
                embed = new EmbedBuilder().setAuthor(
                        Formatter.RemoveFormat(Config.getString(Config.Options.VerifyNoOnline)), autorAvatar,
                        autorAvatar).setColor(new Color(255, 61, 61));
            } else {
                if(!configClass.getBool(event.getAuthor().getAsTag().replace(" ", "_") + ".verified")) {
                    main.perms.playerAdd(null, player, "suffix.20." +
                                                       Formatter.FText(Config.getString(Config.Options.VerifyPrefix),
                                                               true, player));
                    configClass.set(event.getAuthor().getAsTag().replace(" ", "_") + ".verified", true);
                    Objects.requireNonNull(event.getMember()).modifyNickname(player.getName()).queue();
                    event.getGuild().addRoleToMember(event.getMember(), Objects.requireNonNull(
                            event.getGuild().getRoleById(Config.getString(Config.Options.VerifyRoleID)))).queue();
                    embed = new EmbedBuilder().setAuthor(
                            Formatter.RemoveFormat(Config.getString(Config.Options.VerifySuccess)), autorAvatar,
                            autorAvatar).setColor(new Color(125, 255, 100));
                } else {
                    embed = new EmbedBuilder().setAuthor(
                            Formatter.RemoveFormat(Config.getString(Config.Options.VerifyYouVerified)), autorAvatar,
                            autorAvatar).setColor(new Color(255, 61, 61));
                }
            }
        } else if(configClass.getString(event.getAuthor().getAsTag().replace(" ", "_") + ".nick") == null) {
            embed = new EmbedBuilder().setAuthor(Formatter.RemoveFormat(
                    Config.getString(Config.Options.VerifyError).replace("%discord_tag%",
                            event.getAuthor().getAsTag().replace(" ", "_"))), autorAvatar, autorAvatar).setColor(
                    new Color(255, 61, 61));
        } else {
            embed = new EmbedBuilder().setAuthor(
                    Formatter.RemoveFormat(Config.getString(Config.Options.VerifyErrorNick)), autorAvatar,
                    autorAvatar).setColor(new Color(255, 61, 61));
        }

        event.getChannel().sendMessage(embed.build()).queue();
        event.getMessage().delete().queue();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(Config.getBool(Config.Options.DiscordEnable) && Config.getBool(Config.Options.VerifyEnable)) {
            if(args.length == 1) {
                List<String> subcommands = new ArrayList<>();
                subcommands.add("TuTagDeDiscord#0000");
                return subcommands;
            }
        }
        return null;
    }
}
