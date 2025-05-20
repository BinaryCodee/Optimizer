package it.blacked.optimizer.listeners;

import it.blacked.optimizer.Optimizer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class WorldEditListener implements Listener {

    private final Optimizer plugin;

    public WorldEditListener(Optimizer plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldEditCommand(PlayerCommandPreprocessEvent event) {
        if (!plugin.getConfigManager().isLimitWorldEditOperations()) {
            return;
        }

        Player player = event.getPlayer();
        String command = event.getMessage().toLowerCase();

        if (command.startsWith("/paste") ||
                command.startsWith("//paste") ||
                command.startsWith("//copy") ||
                command.startsWith("/schem load ") ||
                command.startsWith("/schematic load")) {

            player.sendMessage("§d§lOPTIMIZER §8» §eOptimizing WorldEdit paste operation...");
            performWorldEditOptimization(player);
        }
    }

    private void performWorldEditOptimization(Player player) {
        System.gc();
        plugin.getLogger().info("Optimizing for WorldEdit operation by " + player.getName());
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            int entitiesRemoved = 0;
            int chunksOptimized = 0;

            for (int x = -5; x <= 5; x++) {
                for (int z = -5; z <= 5; z++) {
                    if (player.getLocation().getWorld().isChunkLoaded(
                            player.getLocation().getBlockX() / 16 + x,
                            player.getLocation().getBlockZ() / 16 + z)) {

                        chunksOptimized++;
                        entitiesRemoved += optimizeChunkForWorldEdit(player.getLocation().getWorld().getChunkAt(
                                player.getLocation().getBlockX() / 16 + x,
                                player.getLocation().getBlockZ() / 16 + z));
                    }
                }
            }

            player.sendMessage("§d§lOPTIMIZER §8» §aOptimized " + chunksOptimized + " chunks and removed " +
                    entitiesRemoved + " entities to prevent crashes");
        });
    }

    private int optimizeChunkForWorldEdit(org.bukkit.Chunk chunk) {
        int entitiesRemoved = 0;
        for (org.bukkit.entity.Entity entity : chunk.getEntities()) {
            if (!(entity instanceof org.bukkit.entity.Player) &&
                    !(entity instanceof org.bukkit.entity.Villager) &&
                    !(entity instanceof org.bukkit.entity.ItemFrame)) {

                entity.remove();
                entitiesRemoved++;
            }
        }

        return entitiesRemoved;
    }
}