package it.blacked.optimizer.utils;

import org.bukkit.Bukkit;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ServerUtils {

    private static final String SERVER_VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    private static Object minecraftServer;
    private static Field recentTps;

    static {
        try {
            Class<?> craftServerClass = Class.forName("org.bukkit.craftbukkit." + SERVER_VERSION + ".CraftServer");
            Method getServerMethod = craftServerClass.getMethod("getServer");
            minecraftServer = getServerMethod.invoke(Bukkit.getServer());
            Class<?> minecraftServerClass = minecraftServer.getClass();
            recentTps = minecraftServerClass.getDeclaredField("recentTps");
            recentTps.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double getTPS() {
        try {
            double[] tps = (double[]) recentTps.get(minecraftServer);
            return tps[0];
        } catch (Exception e) {
            e.printStackTrace();
            return 20.0;
        }
    }

    public static boolean limitWorldEdit(int maxBlocks) {
        try {
            if (Bukkit.getPluginManager().getPlugin("WorldEdit") != null) {
                Class<?> localConfigurationClass = Class.forName("com.sk89q.worldedit.LocalConfiguration");
                Class<?> worldEditClass = Class.forName("com.sk89q.worldedit.bukkit.WorldEditPlugin");

                Object worldEditPlugin = Bukkit.getPluginManager().getPlugin("WorldEdit");
                Method getConfigMethod = worldEditClass.getMethod("getConfig");
                Object config = getConfigMethod.invoke(worldEditPlugin);

                Field maxBlocksField = localConfigurationClass.getField("maxChangeLimit");
                maxBlocksField.setAccessible(true);
                maxBlocksField.set(config, maxBlocks);

                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }
}