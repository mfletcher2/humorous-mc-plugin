package nezd53.sneakfart;

import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTables;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.UUID;

import static java.lang.Math.*;
import static nezd53.sneakfart.SneakFart.*;

public class FartHandler {
    static void fart(Player player) {
        Particle.DustOptions options = new Particle.DustOptions(Color.fromRGB(139, 69, 19), (float) fartParticleSize);
        float yaw = player.getLocation().getYaw();
        Vector offset = new Vector(sin(toRadians(yaw)) * fartDistance, 0.25, -cos(toRadians(yaw)) * fartDistance);
        Location l = player.getLocation().add(offset);
        player.getWorld().spawnParticle(Particle.REDSTONE, l,
                25, fartOffset, fartOffset, fartOffset, options);
        player.playSound(l, Sound.BLOCK_WET_GRASS_PLACE, (float) fartVolume, 0.005f);

        if (random() < poopChance)
            poop(player, offset, l);

        if (random() < deadlyPoopChance)
            deadlyPoop(l);

        if (random() < nauseaChance)
            inflictNausea(l, player);

        SneakFart.fartCount++;
    }

    private static void poop(Player player, Vector offset, Location l) {
        Item item = player.getWorld().dropItem(l, new ItemStack(Material.BROWN_DYE));
        item.setPickupDelay(32767);
        item.setTicksLived(5900);
        item.setCustomName(player.getName() + "'s " + poopName);
        item.setCustomNameVisible(true);
        item.setVelocity(offset.clone().divide(new Vector(10, 1, 10)));
    }

    private static void deadlyPoop(Location l) {
        String textureStr = "e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWFhMDU5OTBkNjMzYzhmYjFhYWVmYTM1YzcwYzViMWU0YWFiODE2YWI1MmI4YzAyZDU0MzY4ODdhNjI3YTI0MCJ9fX0=";
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);

        NBT.modify(head, nbt -> {
            final ReadWriteNBT skullOwnerCompound = nbt.getOrCreateCompound("SkullOwner");

            skullOwnerCompound.setUUID("Id", UUID.randomUUID());
          
            skullOwnerCompound.getOrCreateCompound("Properties")
                    .getCompoundList("textures")
                    .addCompound()
                    .setString("Value", textureStr);
    });
      
        Optional.ofNullable(l.getWorld())
                .ifPresent(world -> {
                    Zombie zombie = (Zombie) world.spawnEntity(l, EntityType.ZOMBIE);
                    zombie.setBaby();
                    Optional.ofNullable(zombie.getEquipment())
                            .ifPresent(equipment -> equipment.setHelmet(headNbt.getItem()));
                    zombie.setInvisible(true);
                    zombie.setSilent(true);
                    zombie.setHealth(1);
                    zombie.setLootTable(LootTables.EMPTY.getLootTable());
                    zombie.setInvisible(true);
                    zombie.setCustomName("Deadly Poop");
                });
    }

    private static void inflictNausea(Location l, Player player) {
        Optional.ofNullable(l.getWorld())
                .stream()
                .map(World::getPlayers)
                .flatMap(List::stream)
                .filter(p -> p.getLocation().distance(l) <= nauseaDistance)
                .filter(Predicate.not(player::equals))
                .forEach(p -> p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 5 * 20, 5)));
    }
}
