package guerrero61.customtnt.mobs.enderdragon.dragonskills;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.mainutils.Formatter;
import guerrero61.customtnt.mainutils.config.ConfigBuilder;
import guerrero61.customtnt.mobs.enderdragon.TnTDragon;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class DragonSkill5 extends ConfigBuilder implements Listener {

    private final Main main;

    public static String skillName = "Mates Explosiva";
    public static int minNumber = 15;
    public static int maxNumber = 200;
    public static String[] operation = new String[]{"+", "-", "*"};
    public static int seconds = 0;
    public static int minSeconds = 20;
    public static int maxSeconds = 25;
    public static float tntPower = 12;
    public static int tntFuseTick = 40;

    public DragonSkill5(Main m) {
        super(TnTDragon.fileName);
        main = m;
    }

    public void Skill5(Player player) {
        Main.debug("Skill 5");
        seconds = Main.random(minSeconds, maxSeconds);
        set(player.getName() + ".number1", Main.random(minNumber, maxNumber));
        set(player.getName() + ".number2", Main.random(minNumber, maxNumber));
        set(player.getName() + ".operation", operation[Main.random(0, operation.length - 1)]);
        player.sendMessage(Formatter
                .FText(TnTDragon.dragonName + " &6&lha usado la habilidad &c&l" + skillName + " &6&len ti."));
        player.sendMessage(Formatter
                .FText("&6&lTienes que escribir el resultado de: &c&l" + getInt(player
                        .getName() + ".number1") + getString(player
                        .getName() + ".operation") + getInt(player
                        .getName() + ".number2") + " &6&len menos de " + seconds + "s"));
        set(player.getName() + ".skill5Active", true);
        BossBar bossBar = Bukkit.createBossBar(new NamespacedKey(main, "ExplosiveMath" + player.getName()), Formatter
                .FText("&c&lMates explosiva", true), BarColor.YELLOW, BarStyle.SOLID, BarFlag.CREATE_FOG);

        BukkitTask task = new BukkitRunnable() {
            double timer;

            @Override
            public void run() {
                if (timer >= seconds) {
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
                    set(player.getName() + ".skill5Active", false);
                    Bukkit.removeBossBar(new NamespacedKey(main, "ExplosiveMath" + player.getName()));
                    cancel();
                } else {
                    bossBar.setProgress((seconds - timer) / seconds);
                }
                timer++;
            }
        }.runTaskTimer(main, 0, 20);

        new BukkitRunnable() {
            int timerTicks = seconds * 20;

            @Override
            public void run() {
                if (!getBool(player.getName() + ".skill5Active") || timerTicks <= 0 ||
                        getBool("disableSkill5")) {
                    set(player.getName() + ".skill5Active", false);
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
        if (getBool(player.getName() + ".skill5Active") && !getBool("disableSkill5")) {
            int result = 0;
            switch (getString(player
                    .getName() + ".operation")) {
                case "+": {
                    result = getInt(player.getName() + ".number1") + getInt(player.getName() + ".number2");
                    break;
                }
                case "-": {
                    result = getInt(player.getName() + ".number1") -
                            getInt(player.getName() + ".number2");
                    break;
                }
                case "*": {
                    result = getInt(player.getName() + ".number1") *
                            getInt(player.getName() + ".number2");
                    break;
                }
            }
            if (!event.getMessage().equals(Integer.toString(result))) {
                player.sendMessage(Formatter.FText("&c&lHas contestado incorrectamente"));
            } else {
                player.sendMessage(Formatter.FText("&a&lContestado correctamente"));
                set(player.getName() + ".skill5Active", false);
            }
        }
    }
}
