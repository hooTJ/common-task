package com.tj.ythu.task;

import java.util.concurrent.ScheduledFuture;

/**
 * ScheduledFuture的包装
 *
 * @author ythu
 * @date 2019/10/30 16:29
 */
public final class ScheduledTaskFuture {

    private volatile ScheduledFuture<?> future;
    private volatile Long taskId;
    private volatile Long firedId;


    public ScheduledTaskFuture(ScheduledFuture<?> future, Long taskId, Long firedId) {
        this.future = future;
        this.taskId = taskId;
        this.firedId = firedId;
    }

    /**
     * 取消操作
     */
    public void cancel() {
        if (this.future != null) {
            this.future.cancel(true);
        }
    }

    public ScheduledFuture<?> getFuture() {
        return future;
    }

    public Long getTaskId() {
        return taskId;
    }

    public Long getFiredId() {
        return firedId;
    }
}
