package nezd53.sneakfart;

import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootTables;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Compatibility layer for handling Spigot API differences across versions.
 * This class provides methods to abstract over API changes, ensuring compatibility with multiple Spigot versions.
 */
final class SpigotCompat {

    static final Particle DUST_PARTICLE = getDustParticle();
    static final LootTable EMPTY_LOOT_TABLE = getEmptyLootTable();
    public static final PotionEffectType NAUSEA_EFFECT = getNauseaEffect();
    static final Attribute SCALE_ATTRIBUTE = getScaleAttribute();

    private SpigotCompat() {
    }

    /**
     * Retrieves the appropriate redstone particle type based on the server's Spigot API version.
     *
     * <p>In Spigot API 1.13 and later, {@code Particle.DUST} is used for redstone particles.
     * In earlier versions, {@code Particle.REDSTONE} is used.</p>
     *
     * @return the {@link Particle} enum constant for redstone particles.
     */
    @NotNull
    private static Particle getDustParticle() {
        try {
            return Particle.valueOf("DUST");
        } catch (IllegalArgumentException e) {
            return Particle.valueOf("REDSTONE");
        }
    }

    /**
     * Retrieves an empty loot table appropriate for the server's Spigot API version.
     *
     * <p>In Spigot API 1.16.2 and later, setting the loot table to {@code null} is the correct way to indicate no loot.
     * In earlier versions, {@code LootTables.EMPTY.getLootTable()} is used to represent an empty loot table.</p>
     *
     * @return {@code null} for newer versions, or {@link LootTable} representing an empty loot table for older versions.
     */
    @Nullable
    private static LootTable getEmptyLootTable() {
        try {
            LootTables empty = LootTables.valueOf("EMPTY");
            return empty.getLootTable();
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Retrieves the appropriate potion effect type for nausea based on the server's Spigot API version.
     *
     * <p>In Spigot API 1.13 and later, {@code PotionEffectType.NAUSEA} is used.
     * In earlier versions, {@code PotionEffectType.CONFUSION} is used to represent the same effect.</p>
     *
     * @return the {@link PotionEffectType} for nausea.
     */
    @NotNull
    private static PotionEffectType getNauseaEffect() {
        PotionEffectType type = getByName("nausea");
        if (type == null) {
            type = getByName("confusion");
        }
        if (type == null) {
            throw new IllegalArgumentException("Neither 'NAUSEA' nor 'CONFUSION' potion effect type is available.");
        }
        return type;
    }

    // `getByName()` is deprecated but the best option for now as PotionEffectType has changed quite a lot between 1.19 and 1.21
    @SuppressWarnings("deprecation")
    private static @Nullable PotionEffectType getByName(String key) {
        return PotionEffectType.getByName(key);
    }

    /**
     * Retrieves the correct scale type or null if it does not exist.
     *
     * <p> 1.20.5 introduced the attribute {@code GENERIC_SCALE}. This was renamed to {@code SCALE} in 1.21.2.
     * Earlier versions do not have the scale attribute. </p>
     *
     * @return the {@link Attribute} for scale, or null if it does not exist
     */
    @Nullable
    @SuppressWarnings("deprecation")
    private static Attribute getScaleAttribute() {
        try {
            return Attribute.valueOf("SCALE");
        } catch (IllegalArgumentException | IncompatibleClassChangeError e1) {
            try {
                return Attribute.valueOf("GENERIC_SCALE");
            } catch (IllegalArgumentException | IncompatibleClassChangeError e2) {
                return null;
            }
        }
    }
}
