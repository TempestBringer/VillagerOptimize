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

import java.util.List;

import static org.bukkit.Bukkit.getLogger;

public class VillagerOptimizeListener implements Listener {
    public Boolean villager_spawn_zombie;
    public Integer zombie_summon_count;
    public Integer villager_no_ai_limit;
    public List<String> no_optimize_list;

    public boolean completely_no_new_villager;

    @EventHandler
    public void onVillagerSpawn(CreatureSpawnEvent e){
        if (e.getEntityType()!=EntityType.VILLAGER)
            return;
        if (completely_no_new_villager){
            e.setCancelled(true);
            return;
        }
        if (villager_spawn_zombie){
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
    public void autoNoAIOnLoadChunk(ChunkLoadEvent e){
        if (e.getChunk().isLoaded()){
            Integer count = 0;
            Entity[] entities = e.getChunk().getEntities();
            if (entities.length>0)
            for (int i = 0; i < entities.length; i++) {
                if (entities[i].getType() == EntityType.VILLAGER && !no_optimize_list.contains(entities[i].getName())) {
                    if (count < villager_no_ai_limit) {
                        ((Villager) entities[i]).setAI(true);
                    } else {
                        ((Villager) entities[i]).setAI(false);
                    }
                    count += 1;
                } else if (entities[i].getType() == EntityType.VILLAGER && no_optimize_list.contains(entities[i].getName())) {
                    ((Villager) entities[i]).setAI(true);
                }
            }
        }
    }

    @EventHandler
    public void autoNoAIOnUnloadChunk(ChunkUnloadEvent e){
        if (e.getChunk().isLoaded()){
            Integer count = 0;
            Entity[] entities = e.getChunk().getEntities();
            if (entities.length>0)
                for (int i = 0; i < entities.length; i++) {
                    if (entities[i].getType() == EntityType.VILLAGER && !no_optimize_list.contains(entities[i].getName())) {
                        if (count < villager_no_ai_limit) {
                            ((Villager) entities[i]).setAI(true);
                        } else {
                            ((Villager) entities[i]).setAI(false);
                        }
                        count += 1;
                    } else if (entities[i].getType() == EntityType.VILLAGER && no_optimize_list.contains(entities[i].getName())) {
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

    public VillagerOptimizeListener(Boolean villager_spawn_zombie, Integer zombie_summon_count,
                                    Integer villager_no_ai_limit, List<String> no_optimize_list,
                                    boolean completely_no_new_villager) {
        this.villager_spawn_zombie = villager_spawn_zombie;
        this.zombie_summon_count = zombie_summon_count;
        this.villager_no_ai_limit = villager_no_ai_limit;
        this.no_optimize_list = no_optimize_list;
        this.completely_no_new_villager = completely_no_new_villager;
    }
}
