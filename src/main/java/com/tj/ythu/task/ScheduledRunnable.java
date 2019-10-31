package com.tj.ythu.task;

import com.tj.ythu.enums.SysTaskFiredStateEnum;
import com.tj.ythu.service.SysTaskFiredService;
import com.tj.ythu.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 调度任务实体（线程），该对象会放在任务线程池中执行
 *
 * @author ythu
 * @date 2019/10/30 10:45
 */
public class ScheduledRunnable implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledRunnable.class);

    private String beanName;
    private String methodName;
    private String params;

    private Object objectObtain = new Object();

    public ScheduledRunnable(String beanName, String methodName, String params) {
        this.beanName = beanName;
        this.methodName = methodName;
        this.params = params;
    }

    public ScheduledRunnable(String beanName, String methodName) {
        this.beanName = beanName;
        this.methodName = methodName;
    }

    private SysTaskFiredService sysTaskFiredService;

    public void setSysTaskFiredService(SysTaskFiredService sysTaskFiredService) {
        this.sysTaskFiredService = sysTaskFiredService;
    }

    private Long firedId;

    public void setFiredId(Long firedId) {
        this.firedId = firedId;
    }

    private ScheduledCronTaskRegistrar scheduledCronTaskRegistrar;

    public void setScheduledCronTaskRegistrar(ScheduledCronTaskRegistrar scheduledCronTaskRegistrar) {
        this.scheduledCronTaskRegistrar = scheduledCronTaskRegistrar;
    }

    @Override
    public void run() {
        synchronized (objectObtain) {
            if (Thread.currentThread().isInterrupted()) {
                return;
            }
            long ct = System.currentTimeMillis();
            sysTaskFiredService.updateState(firedId, SysTaskFiredStateEnum.RUN);
            LOGGER.info("调度任务开始执行 -- beanName: {}, methodName: {}, params: {}", beanName, methodName, params);
            try {
                Object obj = SpringContextUtil.getBean(beanName);
                Method method = null;
                Method[] declaredMethods = obj.getClass().getDeclaredMethods();
                if (Objects.isNull(params)) {
                    method = obj.getClass().getDeclaredMethod(methodName);
                } else {
                    method = obj.getClass().getDeclaredMethod(methodName, String.class);
                }
                ReflectionUtils.makeAccessible(method);

                // 执行目标方法
                if (Objects.isNull(params)) {
                    method.invoke(obj);
                } else {
                    method.invoke(obj, params);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // 程序异常
                scheduledCronTaskRegistrar.removeCronTask(this, SysTaskFiredStateEnum.EXCEPTION);
                Thread.currentThread().interrupt();
                sysTaskFiredService.updateState(firedId, SysTaskFiredStateEnum.EXCEPTION);
                LOGGER.error("调度任务执行异常 -- beanName: {}, methodName: {}, params, {}\r\n", beanName, methodName, params, e.getMessage());
                return;
            }
            sysTaskFiredService.updateState(firedId, SysTaskFiredStateEnum.WAIT);
            LOGGER.info("调度任务执行成功 -- beanName: {}, methodName: {}, params: {}, 耗时: {}s, 结果: {}", beanName, methodName, params, (System.currentTimeMillis() - ct) / 1000.0);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (Objects.isNull(obj) || this.getClass() != obj.getClass()) return false;
        ScheduledRunnable runnable = (ScheduledRunnable) obj;
        if (Objects.isNull(runnable.params)) {
            return beanName.equals(runnable.beanName) && methodName.equals(runnable.methodName) && runnable.params == null;
        }
        return beanName.equals(runnable.beanName) && methodName.equals(runnable.methodName) && params.equals(runnable.params);
    }

    @Override
    public int hashCode() {
        if (Objects.isNull(params)) {
            return Objects.hash(beanName, methodName);
        }
        return Objects.hash(beanName, methodName, params);
    }
}
