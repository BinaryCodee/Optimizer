package it.blacked.optimizer.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import it.blacked.optimizer.Optimizer;
import java.io.File;
import java.io.IOException;

public class ConfigManager {
    private final Optimizer plugin;
    private FileConfiguration config;
    private File configFile;
    private boolean autoTaskEnabled = true;
    private int taskInterval = 300;
    private int maxChunksPerWorld = 2048;
    private boolean limitWorldEditOperations = true;
    private int maxWorldEditBlocks = 50000;
    private boolean optimizeTPS = true;
    private boolean optimizeRAM = true;
    private boolean optimizeCPU = true;
    private boolean optimizeDisk = true;
    private boolean optimizeChunkLoading = true;

    public ConfigManager(Optimizer plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), "config.yml");
        }

        if (!configFile.exists()) {
            plugin.getDataFolder().mkdirs();
            plugin.saveResource("config.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(configFile);
        autoTaskEnabled = config.getBoolean("auto-task.enabled", autoTaskEnabled);
        taskInterval = config.getInt("auto-task.interval-seconds", taskInterval);
        maxChunksPerWorld = config.getInt("optimization.max-chunks-per-world", maxChunksPerWorld);
        limitWorldEditOperations = config.getBoolean("worldedit.limit-operations", limitWorldEditOperations);
        maxWorldEditBlocks = config.getInt("worldedit.max-blocks", maxWorldEditBlocks);
        optimizeTPS = config.getBoolean("optimization.tps", optimizeTPS);
        optimizeRAM = config.getBoolean("optimization.ram", optimizeRAM);
        optimizeCPU = config.getBoolean("optimization.cpu", optimizeCPU);
        optimizeDisk = config.getBoolean("optimization.disk", optimizeDisk);
        optimizeChunkLoading = config.getBoolean("optimization.chunk-loading", optimizeChunkLoading);
    }

    public void saveConfig() {
        if (config == null || configFile == null) {
            return;
        }

        config.set("auto-task.enabled", autoTaskEnabled);
        config.set("auto-task.interval-seconds", taskInterval);
        config.set("optimization.max-chunks-per-world", maxChunksPerWorld);
        config.set("worldedit.limit-operations", limitWorldEditOperations);
        config.set("worldedit.max-blocks", maxWorldEditBlocks);
        config.set("optimization.tps", optimizeTPS);
        config.set("optimization.ram", optimizeRAM);
        config.set("optimization.cpu", optimizeCPU);
        config.set("optimization.disk", optimizeDisk);
        config.set("optimization.chunk-loading", optimizeChunkLoading);

        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save config to " + configFile);
            e.printStackTrace();
        }
    }

    public boolean isAutoTaskEnabled() {
        return autoTaskEnabled;
    }

    public void setAutoTaskEnabled(boolean autoTaskEnabled) {
        this.autoTaskEnabled = autoTaskEnabled;
    }

    public int getTaskInterval() {
        return taskInterval;
    }

    public int getMaxChunksPerWorld() {
        return maxChunksPerWorld;
    }

    public boolean isLimitWorldEditOperations() {
        return limitWorldEditOperations;
    }

    public int getMaxWorldEditBlocks() {
        return maxWorldEditBlocks;
    }

    public boolean isOptimizeTPS() {
        return optimizeTPS;
    }

    public boolean isOptimizeRAM() {
        return optimizeRAM;
    }

    public boolean isOptimizeCPU() {
        return optimizeCPU;
    }

    public boolean isOptimizeDisk() {
        return optimizeDisk;
    }

    public boolean isOptimizeChunkLoading() {
        return optimizeChunkLoading;
    }
}