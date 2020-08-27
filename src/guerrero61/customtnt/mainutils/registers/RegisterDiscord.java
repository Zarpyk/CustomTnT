package guerrero61.customtnt.mainutils.registers;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.discord.commands.*;
import guerrero61.customtnt.discord.events.DiscordReady;
import guerrero61.customtnt.discord.minecraft.DiscordToMinecraft;
import guerrero61.customtnt.discord.minecraft.Verify;
import guerrero61.customtnt.mainutils.config.Config;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class RegisterDiscord {

    private final Main main;

    public RegisterDiscord(Main m) {
        main = m;
    }

    public void registerDiscord() {
        JDABuilder builder = JDABuilder.createDefault(Config.getString(Config.Options.Token));

        builder.setActivity(Activity.playing("/help para ayuda"));

        try {
            main.api = builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }

        main.api.addEventListener(new DiscordReady());
        main.api.addEventListener(new DiscordToMinecraft(main.api));

        main.api.addEventListener(new Help());
        main.api.addEventListener(new IP());
        main.api.addEventListener(new ReportSuggest(main.api));
        main.api.addEventListener(new ServerInfo(main.api, main));
        main.api.addEventListener(new Summon(main));
        main.api.addEventListener(new Busca());
        main.api.addEventListener(new Mapa());
        if(Config.getBool(Config.Options.DiscordEnable) && Config.getBool(Config.Options.VerifyEnable)) {
            main.api.addEventListener(new Verify(main));
        }
    }

}
