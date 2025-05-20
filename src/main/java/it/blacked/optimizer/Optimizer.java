package it.blacked.optimizer;

import it.blacked.optimizer.commands.OptimizeCommand;
import it.blacked.optimizer.commands.OptimizeTabCompleter;
import it.blacked.optimizer.config.ConfigManager;
import it.blacked.optimizer.listeners.ChunkListener;
import it.blacked.optimizer.listeners.WorldEditListener;
import it.blacked.optimizer.tasks.OptimizationTask;
import it.blacked.optimizer.utils.ServerUtils;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

public class Optimizer extends JavaPlugin {

    private ConfigManager configManager;
    private OptimizationTask optimizationTask;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        configManager = new ConfigManager(this);
        configManager.loadConfig();
        optimizationTask = new OptimizationTask(this);
        getCommand("optimize").setExecutor(new OptimizeCommand(this));
        getCommand("optimize").setTabCompleter((TabCompleter) new OptimizeTabCompleter());
        getServer().getPluginManager().registerEvents(new ChunkListener(this), this);
        getServer().getPluginManager().registerEvents(new WorldEditListener(this), this);
        if (configManager.isLimitWorldEditOperations() &&
                getServer().getPluginManager().getPlugin("WorldEdit") != null) {
            ServerUtils.limitWorldEdit(configManager.getMaxWorldEditBlocks());
            getLogger().info("Applied WorldEdit block limit: " + configManager.getMaxWorldEditBlocks());
        }
        getLogger().info("Optimizer v1.0 has been enabled!");
        if (configManager.isAutoTaskEnabled()) {
            optimizationTask.startTask();
        }
    }

    @Override
    public void onDisable() {
        if (optimizationTask != null) {
            optimizationTask.stopTask();
        }
        configManager.saveConfig();
        getLogger().info("Optimizer v1.0 has been disabled!");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public OptimizationTask getOptimizationTask() {
        return optimizationTask;
    }
}