package nezd53.sneakfart;

import org.bukkit.plugin.java.JavaPlugin;

public final class SneakFart extends JavaPlugin {

    static boolean enableFarts;
    static double fartDistance, fartTimeStart, fartTimeEnd,
            fartOffset, fartParticleSize, fartVolume, poopChance;
    static int fartParticleCount;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        enableFarts = getConfig().getBoolean("EnableFarts");
        fartDistance = getConfig().getDouble("FartDistance");
        fartTimeStart = getConfig().getDouble("FartTimeStart");
        fartTimeEnd = getConfig().getDouble("FartTimeEnd");
        fartOffset = getConfig().getDouble("FartOffset");
        fartParticleCount = getConfig().getInt("FartParticleCount");
        fartParticleSize = getConfig().getDouble("FartParticleSize");
        fartVolume = getConfig().getDouble("FartVolume");
        poopChance = getConfig().getDouble("PoopChance");

        if(enableFarts)
            getServer().getPluginManager().registerEvents(new FartListener(), this);

    }

    @Override
    public void onDisable() {
    }
}
