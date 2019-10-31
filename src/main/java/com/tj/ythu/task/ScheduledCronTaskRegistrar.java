package com.tj.ythu.task;

import com.tj.ythu.entity.SysTask;
import com.tj.ythu.enums.SysTaskFiredStateEnum;
import com.tj.ythu.enums.SysTaskFiredTypeEnum;
import com.tj.ythu.enums.SysTaskStateEnum;
import com.tj.ythu.service.SysTaskFiredService;
import com.tj.ythu.service.SysTaskService;
import com.tj.ythu.vo.SysTaskFiredVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * 调度任务登记
 *
 * @author ythu
 * @date 2019/10/30 11:09
 * @see org.springframework.scheduling.config.ScheduledTaskRegistrar
 */
@Component
public class ScheduledCronTaskRegistrar implements InitializingBean, DisposableBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledCronTaskRegistrar.class);

    private static final Map<Runnable, ScheduledTaskFuture> ALL_TASKS = new ConcurrentHashMap<>(16);

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Autowired
    private SysTaskFiredService sysTaskFiredService;

    @Autowired
    private SysTaskService sysTaskService;

    /**
     * 添加调度任务
     *
     * @param runnable
     * @param cron
     * @param taskId
     * @param firedType
     */
    public void addCronTask(Runnable runnable, String cron, Long taskId, SysTaskFiredTypeEnum firedType) {
        addCronTask(new CronTask(runnable, cron), taskId, firedType);
    }

    /**
     * 添加调度任务
     *
     * @param cronTask
     * @param taskId
     * @param firedType
     */
    public void addCronTask(CronTask cronTask, Long taskId, SysTaskFiredTypeEnum firedType) {
        if (Objects.nonNull(cronTask)) {
            Runnable runnable = cronTask.getRunnable();
            LOGGER.info("添加调度任务: {}", runnable);
            if (ALL_TASKS.containsKey(runnable)) {
                ScheduledTaskFuture scheduledTaskFuture = ALL_TASKS.get(runnable);
                removeCronTask(runnable, SysTaskFiredStateEnum.STOP);
                if (taskId != scheduledTaskFuture.getTaskId()) { // 如果当前移除的taskId不等于传入的taskId，那么就需要停止当前移除的taskId
                    sysTaskService.updateState(scheduledTaskFuture.getTaskId(), SysTaskStateEnum.PAUSE);
                }
            }

            // 准备运行状态的数据
            SysTaskFiredVO vo = new SysTaskFiredVO();
            vo.setTaskId(taskId);
            vo.setType(firedType.getValue());
            vo.setState(SysTaskFiredStateEnum.WAIT.getValue());
            long firedId = sysTaskFiredService.save(vo);

            ScheduledRunnable scheduledRunnable = (ScheduledRunnable) runnable;
            scheduledRunnable.setSysTaskFiredService(sysTaskFiredService);
            scheduledRunnable.setFiredId(firedId);
            scheduledRunnable.setScheduledCronTaskRegistrar(this);

            ScheduledFuture<?> scheduledFuture = scheduleCronTask(cronTask);
            ScheduledTaskFuture scheduledTaskFuture = new ScheduledTaskFuture(scheduledFuture, taskId, firedId);
            ALL_TASKS.put(runnable, scheduledTaskFuture);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("添加任务 -- {}：{}", runnable, scheduledTaskFuture);
            }
        }
    }

    /**
     * 移除调度任务
     *
     * @param runnable
     */
    public void removeCronTask(Runnable runnable, SysTaskFiredStateEnum taskFiredState) {
        ScheduledTaskFuture scheduledTaskFuture = ALL_TASKS.remove(runnable);
        if (scheduledTaskFuture != null) {
            scheduledTaskFuture.cancel();
        }
        sysTaskFiredService.updateState(scheduledTaskFuture.getFiredId(), taskFiredState == null ? SysTaskFiredStateEnum.STOP : taskFiredState);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("移除任务 -- {}：{}", runnable, scheduledTaskFuture);
        }
    }

    /**
     * 提交调度任务
     *
     * @param cronTask
     * @return
     */
    private ScheduledFuture<?> scheduleCronTask(CronTask cronTask) {
        return threadPoolTaskScheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger());
    }

    /**
     * 对象销毁
     *
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        LOGGER.info("对象销毁时调用");
        ALL_TASKS.keySet().stream().forEach(runnable -> {
            removeCronTask(runnable, SysTaskFiredStateEnum.DESTROY);
        });
        ALL_TASKS.clear();
    }

    /**
     * 对象创建
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.info("对象创建时调用");
        List<SysTask> taskList = sysTaskService.findByState(SysTaskStateEnum.NORMAL);
        for (SysTask sysTask : taskList) {
            ScheduledRunnable runnable = new ScheduledRunnable(sysTask.getBeanName(), sysTask.getMethod(), sysTask.getParams());
            addCronTask(runnable, sysTask.getCronExpression(), sysTask.getId(), SysTaskFiredTypeEnum.PASSIVE);
        }
    }
}
