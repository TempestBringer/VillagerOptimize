package tempestissimo.club.villageroptimize;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import static org.bukkit.Bukkit.getLogger;

public class VillagerOptimizeListener implements Listener {
    public Boolean villager_spawn_zombie;
    public Integer zombie_summon_count;
    public Integer villager_no_ai_limit;

    @EventHandler
    public void onVillagerSpawn(CreatureSpawnEvent e){
        if (villager_spawn_zombie&&e.getEntityType()== EntityType.VILLAGER){
            Location loc = e.getLocation();
            if(e.getSpawnReason()== CreatureSpawnEvent.SpawnReason.BREEDING){
                getLogger().info("Villager Spawn Cancelled");
                for(int i=0;i<zombie_summon_count;i++)
                    e.getLocation().getWorld().spawnEntity(loc,EntityType.ZOMBIE);
                e.setCancelled(true);
            }else if(e.getSpawnReason()== CreatureSpawnEvent.SpawnReason.CURED){
                getLogger().info("Villager Cure Cancelled");
                for(int i=0;i<zombie_summon_count;i++)
                    e.getLocation().getWorld().spawnEntity(loc,EntityType.ZOMBIE);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void autoNoAI(ChunkLoadEvent e){
        if (e.getChunk().isLoaded()){
            Integer count = 0;
            Entity[] entities = e.getChunk().getEntities();
            if (entities.length>0)
            for (int i = 0; i < entities.length; i++) {
                if (entities[i].getType() == EntityType.VILLAGER && !entities[i].getName().equalsIgnoreCase("qwertyuiop")) {
                    if (count < villager_no_ai_limit) {
                        ((Villager) entities[i]).setAI(true);
                    } else {
                        ((Villager) entities[i]).setAI(false);
                    }
                    count += 1;
                } else if (entities[i].getType() == EntityType.VILLAGER && entities[i].getName().equalsIgnoreCase("qwertyuiop")) {
                    ((Villager) entities[i]).setAI(true);
                }
            }
        }
    }

    @EventHandler
    public void playerGrantAI(PlayerInteractEntityEvent e){
        Entity rightClicked = e.getRightClicked();
        if (rightClicked.getType()==EntityType.VILLAGER){
            String name = rightClicked.getName();
            getLogger().info("Player "+e.getPlayer().getName()+" Interact With A Villager : "+name);
            ((Villager)rightClicked).setAI(true);
        }
    }

    public VillagerOptimizeListener(Boolean villager_spawn_zombie, Integer zombie_summon_count, Integer villager_no_ai_limit) {
        this.villager_spawn_zombie = villager_spawn_zombie;
        this.zombie_summon_count = zombie_summon_count;
        this.villager_no_ai_limit = villager_no_ai_limit;
    }
}
