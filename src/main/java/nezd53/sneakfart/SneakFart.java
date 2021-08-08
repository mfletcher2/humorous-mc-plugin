package nezd53.sneakfart;

import org.bukkit.plugin.java.JavaPlugin;

public final class SneakFart extends JavaPlugin {

    static boolean sneakFarts, fartCommand;
    static double fartDistance, fartTimeStart, fartTimeEnd,
            fartOffset, fartParticleSize, fartVolume, poopChance, nauseaChance, nauseaDistance;
    static int fartParticleCount;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        sneakFarts = getConfig().getBoolean("EnableFarts", true);
        fartCommand = getConfig().getBoolean("FartCommand", true);
        fartDistance = getConfig().getDouble("FartDistance", 0.8);
        fartTimeStart = getConfig().getDouble("FartTimeStart", 15);
        fartTimeEnd = getConfig().getDouble("FartTimeEnd", 30);
        fartOffset = getConfig().getDouble("FartOffset", 0.25);
        fartParticleCount = getConfig().getInt("FartParticleCount", 25);
        fartParticleSize = getConfig().getDouble("FartParticleSize", 1);
        fartVolume = getConfig().getDouble("FartVolume", 1);
        poopChance = getConfig().getDouble("PoopChance", 0.05);
        nauseaChance = getConfig().getDouble("NauseaChance", 0.3);
        nauseaDistance = getConfig().getDouble("NauseaDistance", 2);

        if (sneakFarts)
            getServer().getPluginManager().registerEvents(new FartListener(), this);

        if (fartCommand)
            this.getCommand("fart").setExecutor(new FartCommandExecutor());
    }

    @Override
    public void onDisable() {
    }
}
