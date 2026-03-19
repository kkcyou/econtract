package com.yaoan.module.econtract.util.codegenerate;

import java.time.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SortUtil {
    // 初始计数器值
    private static final AtomicInteger sortCounter = new AtomicInteger(0);

    // 设置定时任务：每天凌晨归零
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    static {
        // 获取当前时间的午夜时分
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        ZonedDateTime nextMidnight = now.with(LocalTime.MIDNIGHT).plusDays(1);

        long initialDelay = Duration.between(now, nextMidnight).toMillis();
        long period = 24 * 60 * 60 * 1000; // 24小时的毫秒数

        // 安排任务：每天午夜重置计数器
        scheduler.scheduleAtFixedRate(SortUtil::resetCounter, initialDelay, period, TimeUnit.MILLISECONDS);
    }

    // 获取并自增计数器
    public static int getSort() {
        return sortCounter.incrementAndGet();
    }

    // 重置计数器为0
    private static void resetCounter() {
        sortCounter.set(0);
    }

    // 关闭调度器（在应用程序结束时调用）
    public static void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException ex) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 示例代码：获取计数器值
        for (int i = 0; i < 10; i++) {
            System.out.println(SortUtil.getSort());
            Thread.sleep(1000); // 模拟延迟
        }

        // 关闭调度器
        SortUtil.shutdown();
    }
}
