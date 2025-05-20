package it.blacked.optimizer.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class MemoryUtils {
    private static final MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();

    public static long getUsedMemory() {
        MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
        return heapUsage.getUsed() / (1024 * 1024);
    }

    public static long getMaxMemory() {
        MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
        return heapUsage.getMax() / (1024 * 1024);
    }

    public static long getFreeMemory() {
        MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
        return (heapUsage.getMax() - heapUsage.getUsed()) / (1024 * 1024);
    }

    public static double getMemoryUsagePercent() {
        MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
        return ((double) heapUsage.getUsed() / heapUsage.getMax()) * 100;
    }

    public static boolean isMemoryCritical(int threshold) {
        return getMemoryUsagePercent() > threshold;
    }

    public static void requestGarbageCollection() {
        System.gc();
    }
}