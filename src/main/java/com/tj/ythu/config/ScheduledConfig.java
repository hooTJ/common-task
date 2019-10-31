package com.tj.ythu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 任务调度配置
 *
 * @author ythu
 * @date 2019/10/30 10:35
 */
@Configuration
public class ScheduledConfig {

    /**
     * 线程池任务调度
     * <p>ThreadPoolTaskScheduler是线程池任务调度类，能够开启线程池进行任务调度</p>
     *
     * @return
     */
    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(1024); // 设置线程池容量
        taskScheduler.setThreadNamePrefix("TaskScheduler-task-"); // 线程名前缀
        taskScheduler.setWaitForTasksToCompleteOnShutdown(true);// 当调度器shutdown被调用时等待当前被调度的任务完成
        taskScheduler.setRemoveOnCancelPolicy(true); // 当任务被取消的同时从当前调度器移除的策略
        taskScheduler.setAwaitTerminationSeconds(300); // 等待时常
        return taskScheduler;
    }

}
