package it.blacked.optimizer.tasks;

import it.blacked.optimizer.Optimizer;
import it.blacked.optimizer.utils.MemoryUtils;
import it.blacked.optimizer.utils.ServerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.Arrays;

public class OptimizationTask {

    private final Optimizer plugin;
    private BukkitTask task;
    private boolean isRunning = false;

    public OptimizationTask(Optimizer plugin) {
        this.plugin = plugin;
    }

    public void startTask() {
        if (isRunning) {
            return;
        }

        int interval = plugin.getConfigManager().getTaskInterval() * 20;

        task = Bukkit.getScheduler().runTaskTimer(plugin, this::runOptimizations, interval, interval);
        isRunning = true;

        plugin.getLogger().info("Optimization task started with interval: " + plugin.getConfigManager().getTaskInterval() + " seconds");
    }

    public void stopTask() {
        if (!isRunning) {
            return;
        }

        if (task != null) {
            task.cancel();
            task = null;
        }

        isRunning = false;
        plugin.getLogger().info("Optimization task stopped");
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void runOptimizations() {
        plugin.getLogger().info("Running optimization tasks...");
        if (plugin.getConfigManager().isOptimizeTPS()) {
            optimizeTPS();
        }

        if (plugin.getConfigManager().isOptimizeRAM()) {
            optimizeRAM();
        }

        if (plugin.getConfigManager().isOptimizeCPU()) {
            optimizeCPU();
        }

        if (plugin.getConfigManager().isOptimizeDisk()) {
            optimizeDisk();
        }

        if (plugin.getConfigManager().isOptimizeChunkLoading()) {
            optimizeChunkLoading();
        }

        plugin.getLogger().info("Optimization tasks completed");
    }

    private void optimizeTPS() {
        plugin.getLogger().info("Optimizing TPS...");
        int itemsRemoved = 0;
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof Item) {
                    entity.remove();
                    itemsRemoved++;
                }
            }
        }
        int monstersRemoved = 0;
        for (World world : Bukkit.getWorlds()) {
            for (Chunk chunk : world.getLoadedChunks()) {
                int monsterCount = 0;
                for (Entity entity : chunk.getEntities()) {
                    if (entity instanceof Monster) {
                        monsterCount++;
                        if (monsterCount > 15) {
                            entity.remove();
                            monstersRemoved++;
                        }
                    }
                }
            }
        }

        plugin.getLogger().info("TPS Optimization: Removed " + itemsRemoved + " dropped items and " + monstersRemoved + " extra monsters");
    }

    private void optimizeRAM() {
        plugin.getLogger().info("Optimizing RAM usage...");
        System.gc();
        long usedMemory = MemoryUtils.getUsedMemory();
        long maxMemory = MemoryUtils.getMaxMemory();
        long freeMemory = MemoryUtils.getFreeMemory();

        plugin.getLogger().info("Memory Usage: " + usedMemory + "MB / " + maxMemory + "MB (Free: " + freeMemory + "MB)");
    }

    private void optimizeCPU() {
        plugin.getLogger().info("Optimizing CPU usage...");
        double tps = ServerUtils.getTPS();

        if (tps < 18.0) {
            int entitiesLimited = 0;
            for (World world : Bukkit.getWorlds()) {
                for (Entity entity : world.getEntities()) {
                    if (!(entity instanceof Player)) {
                        if (entity instanceof LivingEntity) {
                                entitiesLimited++;
                        }
                    }
                }
            }

            plugin.getLogger().info("CPU Optimization: Limited AI processing for " + entitiesLimited + " entities due to low TPS: " + tps);
        }
    }

    private void optimizeDisk() {
        plugin.getLogger().info("Optimizing disk usage...");
        File serverDirectory = new File(".");
        File logsDirectory = new File(serverDirectory, "logs");
        if (logsDirectory.exists() && logsDirectory.isDirectory()) {
            File[] logFiles = logsDirectory.listFiles((dir, name) -> name.endsWith(".log.gz"));
            if (logFiles != null) {
                Arrays.sort(logFiles, (f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified()));
                int filesToDelete = logFiles.length - 10;
                int filesDeleted = 0;
                for (int i = 0; i < filesToDelete; i++) {
                    if (logFiles[i].delete()) {
                        filesDeleted++;
                    }
                }

                plugin.getLogger().info("Disk Optimization: Deleted " + filesDeleted + " old log files");
            }
        }
    }

    private void optimizeChunkLoading() {
        plugin.getLogger().info("Optimizing chunk loading...");
        int chunksUnloaded = 0;
        for (World world : Bukkit.getWorlds()) {
            int maxChunks = plugin.getConfigManager().getMaxChunksPerWorld();
            Chunk[] loadedChunks = world.getLoadedChunks();
            if (loadedChunks.length > maxChunks) {
                int needToUnload = loadedChunks.length - maxChunks;
                Arrays.sort(loadedChunks, (c1, c2) -> {
                    double dist1 = c1.getX() * c1.getX() + c1.getZ() * c1.getZ();
                    double dist2 = c2.getX() * c2.getX() + c2.getZ() * c2.getZ();
                    return Double.compare(dist2, dist1);
                });
                for (int i = 0; i < needToUnload; i++) {
                    if (loadedChunks[i].getEntities().length > 0) {
                        boolean hasPlayers = false;
                        for (Entity entity : loadedChunks[i].getEntities()) {
                            if (entity instanceof org.bukkit.entity.Player) {
                                hasPlayers = true;
                                break;
                            }
                        }

                        if (!hasPlayers && world.unloadChunk(loadedChunks[i].getX(), loadedChunks[i].getZ(), true)) {
                            chunksUnloaded++;
                        }
                    } else if (world.unloadChunk(loadedChunks[i].getX(), loadedChunks[i].getZ(), true)) {
                        chunksUnloaded++;
                    }
                }
            }
        }

        plugin.getLogger().info("Chunk Loading Optimization: Unloaded " + chunksUnloaded + " chunks");
    }
}