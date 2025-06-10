package nezd53.sneakfart;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

import static nezd53.sneakfart.SneakFart.fartTimeEnd;
import static nezd53.sneakfart.SneakFart.fartTimeStart;

public class FartListener implements Listener {
    private static final Random random = new Random();

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        if (event.isSneaking())
            handleSneak(event.getPlayer());
    }

    private static void handleSneak(Player player) {
        int startingTick = player.getStatistic(Statistic.SNEAK_TIME);
        int endTick = (int) (startingTick + random.nextFloat(fartTimeStart, fartTimeEnd + 1) * 20);

        player.getServer().getScheduler().scheduleSyncDelayedTask(JavaPlugin.getProvidingPlugin(SneakFart.class), () -> {
            if (player.getStatistic(Statistic.SNEAK_TIME) >= endTick)
                FartHandler.fart(player);
        }, endTick - startingTick + 5L);

    }
}
