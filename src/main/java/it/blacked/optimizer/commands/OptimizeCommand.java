package it.blacked.optimizer.commands;

import it.blacked.optimizer.Optimizer;
import it.blacked.optimizer.utils.MemoryUtils;
import it.blacked.optimizer.utils.ServerUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class OptimizeCommand implements CommandExecutor {

    private final Optimizer plugin;

    public OptimizeCommand(Optimizer plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            showHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "starttask":
                startTask(sender);
                break;
            case "stoptask":
                stopTask(sender);
                break;
            case "manualtask":
                manualTask(sender);
                break;
            case "info":
                showInfo(sender);
                break;
            default:
                showHelp(sender);
                break;
        }

        return true;
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage("§d§lOPTIMIZER §5v1.0 §8- §eby §fblacked104");
        sender.sendMessage("");
        sender.sendMessage("§8* §7/§eoptimize help §f- §dShow this message");
        sender.sendMessage("§8* §7/§eoptimize starttask §f- §dStarting Auto-Task for optimize");
        sender.sendMessage("§8* §7/§eoptimize stoptask §f- §dStopping Auto-Task for optimize");
        sender.sendMessage("§8* §7/§eoptimize manualtask §f- §dStarting Manual-Task for optimize");
        sender.sendMessage("§8* §7/§eoptimize info §f- §dShow server optimization status");
    }

    private void startTask(CommandSender sender) {
        if (plugin.getOptimizationTask().isRunning()) {
            sender.sendMessage("§d§lOPTIMIZER §8» §cOptimization task is already running!");
            return;
        }
        plugin.getOptimizationTask().startTask();
        plugin.getConfigManager().setAutoTaskEnabled(true);
        plugin.getConfigManager().saveConfig();
        sender.sendMessage("§d§lOPTIMIZER §8» §aAutomatic optimization task has been started!");
    }

    private void stopTask(CommandSender sender) {
        if (!plugin.getOptimizationTask().isRunning()) {
            sender.sendMessage("§d§lOPTIMIZER §8» §cOptimization task is not running!");
            return;
        }
        plugin.getOptimizationTask().stopTask();
        plugin.getConfigManager().setAutoTaskEnabled(false);
        plugin.getConfigManager().saveConfig();
        sender.sendMessage("§d§lOPTIMIZER §8» §aAutomatic optimization task has been stopped!");
    }

    private void manualTask(CommandSender sender) {
        sender.sendMessage("§d§lOPTIMIZER §8» §aStarting manual optimization...");
        plugin.getOptimizationTask().runOptimizations();
        sender.sendMessage("§d§lOPTIMIZER §8» §aManual optimization completed!");
    }

    private void showInfo(CommandSender sender) {
        sender.sendMessage("§d§lOPTIMIZER §5v1.0 §8- §eServer Status");
        sender.sendMessage("");

        boolean taskRunning = plugin.getOptimizationTask().isRunning();
        sender.sendMessage("§8* §7Task Status: " + (taskRunning ? "§aRunning" : "§cStopped"));
        if (taskRunning) {
            sender.sendMessage("§8* §7Task Interval: §e" + plugin.getConfigManager().getTaskInterval() + " §7seconds");
        }

        double tps = ServerUtils.getTPS();
        String tpsColor = tps > 19.0 ? "§a" : (tps > 17.0 ? "§e" : "§c");
        sender.sendMessage("§8* §7Current TPS: " + tpsColor + String.format("%.2f", tps));

        long usedMemory = MemoryUtils.getUsedMemory();
        long maxMemory = MemoryUtils.getMaxMemory();
        long freeMemory = MemoryUtils.getFreeMemory();
        double memoryPercent = MemoryUtils.getMemoryUsagePercent();
        String memColor = memoryPercent < 70 ? "§a" : (memoryPercent < 85 ? "§e" : "§c");

        sender.sendMessage("§8* §7Memory Usage: " + memColor + usedMemory + "MB§7/§e" +
                maxMemory + "MB §7(Free: §e" + freeMemory + "MB§7)");

        int totalChunks = 0;
        for (World world : Bukkit.getWorlds()) {
            int worldChunks = world.getLoadedChunks().length;
            totalChunks += worldChunks;
            int maxChunks = plugin.getConfigManager().getMaxChunksPerWorld();
            String chunkColor = worldChunks < maxChunks * 0.7 ? "§a" : (worldChunks < maxChunks ? "§e" : "§c");
            sender.sendMessage("§8* §7World §e" + world.getName() + "§7: " +
                    chunkColor + worldChunks + "§7/§e" + maxChunks + " §7chunks");
        }

        sender.sendMessage("§8* §7Total Loaded Chunks: §e" + totalChunks);

        int totalEntities = 0;
        int totalMobs = 0;
        int totalItems = 0;

        for (World world : Bukkit.getWorlds()) {
            for (org.bukkit.entity.Entity entity : world.getEntities()) {
                totalEntities++;
                if (entity instanceof org.bukkit.entity.Monster ||
                        entity instanceof org.bukkit.entity.Animals) {
                    totalMobs++;
                } else if (entity instanceof org.bukkit.entity.Item) {
                    totalItems++;
                }
            }
        }

        sender.sendMessage("§8* §7Total Entities: §e" + totalEntities +
                " §7(Mobs: §e" + totalMobs + "§7, Items: §e" + totalItems + "§7)");

        if (plugin.getConfigManager().isLimitWorldEditOperations() &&
                Bukkit.getPluginManager().getPlugin("WorldEdit") != null) {
            sender.sendMessage("§8* §7WorldEdit Max Blocks: §e" +
                    plugin.getConfigManager().getMaxWorldEditBlocks());
        }

        sender.sendMessage("§8* §7Enabled Optimizations: " +
                (plugin.getConfigManager().isOptimizeTPS() ? "§aTPS " : "§cTPS ") +
                (plugin.getConfigManager().isOptimizeRAM() ? "§aRAM " : "§cRAM ") +
                (plugin.getConfigManager().isOptimizeCPU() ? "§aCPU " : "§cCPU ") +
                (plugin.getConfigManager().isOptimizeDisk() ? "§aDisk " : "§cDisk ") +
                (plugin.getConfigManager().isOptimizeChunkLoading() ? "§aChunks" : "§cChunks"));
    }
}