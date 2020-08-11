package guerrero61.customtnt.mobs.enderdragon.dragonskills;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mobs.enderdragon.TnTDragon;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class DragonSkill5 implements Listener {

    private final Main main;
    public static String skillName = "Mates Explosiva";
    public static int minNumber = 15;
    public static int maxNumber = 200;
    public static String[] operation = new String[]{"+", "-", "*"};
    public static int seconds = 0;
    public static int minSeconds = 12;
    public static int maxSeconds = 17;
    public static float tntPower = 7.5f;
    public static int tntFuseTick = 60;

    public DragonSkill5(Main m) {
        main = m;
    }

    public void Skill5(Player player) {
        Main.debug("Skill 5");
        seconds = Main.random(minSeconds, maxSeconds);
        TnTDragon.set(player.getName() + ".number1", Main.random(minNumber, maxNumber));
        TnTDragon.set(player.getName() + ".number2", Main.random(minNumber, maxNumber));
        TnTDragon.set(player.getName() + ".operation", operation[Main.random(0, operation.length - 1)]);
        player.sendMessage(Formatter
                .FText(TnTDragon.dragonName + " &4&lha usado la habilidad &2&l" + skillName + " &4&len ti."));
        player.sendMessage(Formatter
                .FText("&4&lTienes que escribir el resultado de: &2&l" + TnTDragon.getInt(player
                        .getName() + ".number1") + TnTDragon.getString(player
                        .getName() + ".operation") + TnTDragon.getInt(player
                        .getName() + ".number2") + " &4&len menos de " + seconds + "s"));
        TnTDragon.set(player.getName() + ".skill5Active", true);
        BukkitTask task = new BukkitRunnable() {
            int timer = seconds;

            @Override
            public void run() {
                if (timer <= 0) {
                    player.sendMessage(Formatter.FText("&c&lSe acabo el tiempo >:D"));
                    TNTPrimed tnt = player.getLocation().getWorld()
                            .spawn(player.getLocation().add(0, 2, 0), TNTPrimed.class);
                    tnt.setIsIncendiary(true);
                    tnt.setGlowing(true);
                    tnt.setFuseTicks(tntFuseTick);
                    Bukkit.getScheduler()
                            .runTaskLater(main, () -> tnt.getWorld().createExplosion(tnt.getLocation().getX(),
                                    tnt.getLocation().getY(), tnt.getLocation()
                                            .getZ(), tntPower, true, true), tntFuseTick);
                    TnTDragon.set(player.getName() + ".skill5Active", false);
                    cancel();
                } else {
                    player.sendMessage(Formatter.FText("&a&lQuedan " + timer + " segundos"));
                }
                timer -= 1;
            }
        }.runTaskTimer(main, 0, 20);
        new BukkitRunnable() {
            int timerTicks = seconds * 20;

            @Override
            public void run() {
                if (!TnTDragon.getBool(player.getName() + ".skill5Active") || timerTicks <= 0) {
                    TnTDragon.set(player.getName() + ".skill5Active", false);
                    task.cancel();
                    cancel();
                }
                timerTicks -= 1;
            }
        }.runTaskTimer(main, 0, 1);
    }

    @EventHandler
    public void playerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (TnTDragon.getBool(player.getName() + ".skill5Active")) {
            int result = 0;
            switch (TnTDragon.getString(player
                    .getName() + ".operation")) {
                case "+": {
                    result = TnTDragon.getInt(player.getName() + ".number1") +
                            TnTDragon.getInt(player.getName() + ".number2");
                    break;
                }
                case "-": {
                    result = TnTDragon.getInt(player.getName() + ".number1") -
                            TnTDragon.getInt(player.getName() + ".number2");
                    break;
                }
                case "*": {
                    result = TnTDragon.getInt(player.getName() + ".number1") *
                            TnTDragon.getInt(player.getName() + ".number2");
                    break;
                }
            }
            if (!event.getMessage().equals(Integer.toString(result))) {
                player.sendMessage(Formatter.FText("&c&lHas contestado incorrectamente"));
            } else {
                player.sendMessage(Formatter.FText("&a&lContestado correctamente"));
                TnTDragon.set(player.getName() + ".skill5Active", false);
            }
        }
    }
}
