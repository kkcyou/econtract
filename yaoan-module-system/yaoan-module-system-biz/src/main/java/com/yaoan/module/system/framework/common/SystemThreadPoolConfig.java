package com.yaoan.module.system.framework.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Description:
 * @Author: doujl
 */
@Configuration
@EnableAsync
public class SystemThreadPoolConfig {
    @Value("${thread.pool.system.corePoolSize}")
    private int corePoolSize;
    @Value("${thread.pool.system.maxPoolSize}")
    private int maxPoolSize;
    @Value("${thread.pool.system.queueCapacity}")
    private int queueCapacity;
    @Value("${thread.pool.system.keepAliveSeconds}")
    private int keepAliveSeconds;
    @Value("${thread.pool.system.waitForTasksToCompleteOnShutdown}")
    private boolean waitForTasksToCompleteOnShutdown;
    @Value("${thread.pool.system.threadNamePrefix}")
    private String threadNamePrefix;

    @Bean(name="systemTaskExecutor")
    public TaskExecutor taskExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(corePoolSize);
        // 设置最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        // 设置队列容量
        executor.setQueueCapacity(queueCapacity);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(keepAliveSeconds);
        // 设置默认线程名称
        executor.setThreadNamePrefix(threadNamePrefix);
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(waitForTasksToCompleteOnShutdown);
        return executor;
    }
}
