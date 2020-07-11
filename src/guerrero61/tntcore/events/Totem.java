package guerrero61.tntcore.events;

import guerrero61.tntcore.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;

import java.util.Objects;
import java.util.Random;

public class Totem implements Listener {

    @EventHandler
    public void totemNerf(EntityResurrectEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;
        if (((Player) event.getEntity()).getInventory().getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING
                || ((Player) event.getEntity()).getInventory().getItemInOffHand()
                .getType() == Material.TOTEM_OF_UNDYING) {
            if (!Main.getBool("Totem.fail-enable"))
                return;
            Player p = (Player) event.getEntity();
            String player = p.getName();
            int failProb = Main.getInt("Totem.probability");
            String totemFail = Objects.requireNonNull(Main.getString("Totem.msg-fail"));
            String totemMessage = Objects.requireNonNull(Main.getString("Totem.msg-used-totem"));
            if (failProb >= 101)
                failProb = 100;
            if (failProb < 0)
                failProb = 1;
            if (failProb == 100) {
                Bukkit.broadcastMessage(Main.FText(totemMessage.replace("%player%", player).replace("%porcent%", "=")
                        .replace("%totem_fail%", String.valueOf(100)).replace("%number%", String.valueOf(failProb))));
                Bukkit.broadcastMessage(Main.FText(totemFail.replace("%player%", player)));
                event.setCancelled(true);
            } else {
                int random = (new Random()).nextInt(99);
                random++;
                int resta = 100 - failProb;
                if (random > resta) {
                    Bukkit.broadcastMessage(Main.FText(totemMessage.replace("%player%", player)
                            .replace("%porcent%", "=>").replace("%totem_fail%", String.valueOf(random))
                            .replace("%number%", String.valueOf(resta))));
                    Bukkit.broadcastMessage(Main.FText(totemFail.replace("%player%", player)));
                    event.setCancelled(true);
                } else {
                    Bukkit.broadcastMessage(Main.FText(totemMessage.replace("%player%", player)
                            .replace("%porcent%", "!=").replace("%totem_fail%", String.valueOf(random))
                            .replace("%number%", String.valueOf(resta))));
                }
            }
        }
    }
}
