package tempestissimo.club.villageroptimize;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class VillagerOptimize extends JavaPlugin {
    public static VillagerOptimize pluginSelf;
    public static FileConfiguration config;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        config = this.getConfig();
        pluginSelf = this;
        Boolean villager_spawn_zombie=config.getBoolean("optimize_config.villager_spawn_zombie");
        Integer zombie_summon_count=config.getInt("optimize_config.zombie_summon_count");
        Integer villager_no_ai_limit=config.getInt("optimize_config.villager_no_ai_limit");

        getServer().getPluginManager().registerEvents(new VillagerOptimizeListener(villager_spawn_zombie,zombie_summon_count,villager_no_ai_limit),this);

    }

    @Override
    public void onDisable() {
    }

    public static VillagerOptimize getPluginSelf(){
        return pluginSelf;
    }
}
