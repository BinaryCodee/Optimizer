package it.blacked.optimizer.listeners;

import it.blacked.optimizer.Optimizer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChunkListener implements Listener {
    private final Optimizer plugin;
    private final Map<UUID, Integer> worldChunkCounts = new HashMap<>();

    public ChunkListener(Optimizer plugin) {
        this.plugin = plugin;
        plugin.getServer().getWorlds().forEach(world ->
                worldChunkCounts.put(world.getUID(), world.getLoadedChunks().length));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChunkLoad(ChunkLoadEvent event) {
        UUID worldId = event.getWorld().getUID();
        int currentCount = worldChunkCounts.getOrDefault(worldId, 0);
        worldChunkCounts.put(worldId, currentCount + 1);
        int maxChunks = plugin.getConfigManager().getMaxChunksPerWorld();
        if (currentCount > maxChunks) {
            plugin.getLogger().warning("World " + event.getWorld().getName() +
                    " has " + currentCount + " chunks loaded (max: " + maxChunks + ")");
            if (currentCount > maxChunks * 1.2) {
                plugin.getLogger().warning("Forcing chunk optimization for world " + event.getWorld().getName());
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    plugin.getOptimizationTask().runOptimizations();
                });
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChunkUnload(ChunkUnloadEvent event) {
        UUID worldId = event.getWorld().getUID();
        int currentCount = worldChunkCounts.getOrDefault(worldId, 1);
        worldChunkCounts.put(worldId, Math.max(0, currentCount - 1));
    }

    public int getLoadedChunks(UUID worldId) {
        return worldChunkCounts.getOrDefault(worldId, 0);
    }

    public void updateChunkCount(UUID worldId, int count) {
        worldChunkCounts.put(worldId, count);
    }
}