package nezd53.sneakfart;

import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import static java.lang.Math.*;
import static nezd53.sneakfart.SneakFart.*;

public class FartHandler {
    static void handleSneak(Player player) {
        int startingTick = player.getStatistic(Statistic.SNEAK_TIME);
        int endTick = startingTick + (int) (random() * (fartTimeEnd - fartTimeStart) + fartTimeStart) * 20;

        player.getServer().getScheduler().scheduleSyncDelayedTask(JavaPlugin.getProvidingPlugin(SneakFart.class), () -> {
            if (player.getStatistic(Statistic.SNEAK_TIME) >= endTick)
                fart(player);
        }, endTick - startingTick + 5);

    }

    static void fart(Player player) {
        Particle.DustOptions options = new Particle.DustOptions(Color.fromRGB(139, 69, 19), (float)fartParticleSize);
        float yaw = player.getLocation().getYaw();
        Vector offset = new Vector(sin(toRadians(yaw)) * fartDistance, 0.25, -cos(toRadians(yaw)) * fartDistance);
        Location l = player.getLocation().add(offset);
        player.spawnParticle(Particle.REDSTONE, l,
                25, fartOffset, fartOffset, fartOffset, options);
        player.playSound(l, Sound.BLOCK_WET_GRASS_PLACE, (float)fartVolume, 0.005f);

        if (random() <= poopChance) {
                Item item = player.getWorld().dropItem(l, new ItemStack(Material.BROWN_DYE));
                item.setPickupDelay(32767);
                item.setTicksLived(5900);
                item.setCustomName(player.getName() + "'s Poop");
                item.setCustomNameVisible(true);
                item.setVelocity(offset.divide(new Vector(10,1,10)));
        }
    }
}
