package com.tj.ythu.task;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

/**
 * 调度任务停止
 *
 * @author ythu
 * @date 2019/10/31 10:46
 */
@Component
public class ScheduledTaskStopListener implements ApplicationListener {
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextClosedEvent) {
            // TODO
        }
    }
}
