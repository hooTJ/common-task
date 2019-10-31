package com.tj.ythu.service.impl;

import com.tj.ythu.entity.SysTask;
import com.tj.ythu.enums.SysTaskFiredStateEnum;
import com.tj.ythu.enums.SysTaskFiredTypeEnum;
import com.tj.ythu.enums.SysTaskStateEnum;
import com.tj.ythu.repository.SysTaskRepository;
import com.tj.ythu.service.SysTaskService;
import com.tj.ythu.task.ScheduledCronTaskRegistrar;
import com.tj.ythu.task.ScheduledRunnable;
import com.tj.ythu.vo.SysTaskVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 系统任务业务
 *
 * @author ythu
 * @date 2019/10/30 14:05
 */
@Service
public class SysTaskServiceImpl implements SysTaskService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysTaskServiceImpl.class);
    @Autowired
    private SysTaskRepository sysTaskRepository;

    @Autowired
    private ScheduledCronTaskRegistrar scheduledCronTaskRegistrar;

    /**
     * 保存系统任务
     *
     * @param sysTaskVO
     * @return
     */
    @Transactional
    @Override
    public long save(SysTaskVO sysTaskVO) {
        if (sysTaskVO == null || StringUtils.isEmpty(sysTaskVO.getBeanName()) || StringUtils.isEmpty(sysTaskVO.getMethod())
                || StringUtils.isEmpty(sysTaskVO.getCronExpression()) || StringUtils.isEmpty(sysTaskVO.getState())) {
            LOGGER.error("调用参数非法...{}", sysTaskVO);
            throw new RuntimeException("调用参数非法");
        }

        SysTask sysTask = new SysTask();
        BeanUtils.copyProperties(sysTaskVO, sysTask);
        sysTask.setTime(new Date());
        sysTask.setUpdateTime(new Date());

        SysTask entity = sysTaskRepository.save(sysTask);
        if (entity == null || entity.getId() < 1) {
            LOGGER.error("保存失败...{}", sysTaskVO);
            throw new RuntimeException("保存失败");
        }

        ScheduledRunnable runnable = new ScheduledRunnable(sysTask.getBeanName(), sysTask.getMethod(), sysTask.getParams());
        scheduledCronTaskRegistrar.addCronTask(runnable, sysTask.getCronExpression(), entity.getId(), SysTaskFiredTypeEnum.ACTIVE);

        return entity.getId();
    }

    /**
     * 更新任务
     *
     * @param sysTaskVO
     * @return
     */
    @Transactional
    @Override
    public int update(SysTaskVO sysTaskVO) {
        Optional<SysTask> optional = sysTaskRepository.findById(sysTaskVO.getId());
        if (!optional.isPresent()) {
            LOGGER.error("数据不存在...{}", sysTaskVO);
            throw new RuntimeException("数据不存在");
        }
        SysTask sysTask = optional.get();

        boolean flag = false;
        if (sysTaskVO.getState() != sysTask.getState() ||
                sysTaskVO.getBeanName() != sysTask.getBeanName() ||
                sysTaskVO.getMethod() != sysTask.getMethod() ||
                sysTaskVO.getParams() != sysTask.getParams()) {
            flag = true;
        }

        ScheduledRunnable runnable = null;
        if (flag) {
            runnable = new ScheduledRunnable(sysTask.getBeanName(), sysTask.getMethod(), sysTask.getParams());
            scheduledCronTaskRegistrar.removeCronTask(runnable, SysTaskFiredStateEnum.STOP);
        }

        BeanUtils.copyProperties(sysTaskVO, sysTask);
        sysTaskRepository.save(sysTask);
        if (sysTask == null || sysTask.getId() < 1) {
            LOGGER.error("更新失败...{}", sysTaskVO);
            throw new RuntimeException("更新失败");
        }

        if (flag) {
            runnable = new ScheduledRunnable(sysTaskVO.getBeanName(), sysTaskVO.getMethod(), sysTaskVO.getParams());
            scheduledCronTaskRegistrar.addCronTask(runnable, sysTaskVO.getCronExpression(), sysTask.getId(), SysTaskFiredTypeEnum.ACTIVE);
        }

        return 1;
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Transactional
    @Override
    public int delete(Long id) {
        Optional<SysTask> optional = sysTaskRepository.findById(id);
        SysTask sysTask = optional.get();
        int rows = sysTaskRepository.updateDelFlag(id);
        if (rows < 1) {
            LOGGER.error("删除...{}", sysTask);
            throw new RuntimeException("删除失败");
        }

        ScheduledRunnable runnable = new ScheduledRunnable(sysTask.getBeanName(), sysTask.getMethod(), sysTask.getParams());
        scheduledCronTaskRegistrar.removeCronTask(runnable, SysTaskFiredStateEnum.STOP);

        return rows;
    }

    /**
     * 更新状态（不更新任务）
     *
     * @param id
     * @param taskState
     * @return
     */
    @Transactional
    @Override
    public int updateState(Long id, SysTaskStateEnum taskState) {
        Optional<SysTask> optional = sysTaskRepository.findById(id);
        SysTask sysTask = optional.get();
        int rows = sysTaskRepository.updateState(id, taskState.getValue());
        if (rows < 1) {
            LOGGER.error("更新失败...{}", sysTask);
            throw new RuntimeException("更新失败");
        }
        return rows;
    }

    /**
     * 根据状态查询任务列表
     *
     * @param taskState
     * @return
     */
    @Override
    public List<SysTask> findByState(SysTaskStateEnum taskState) {
        return sysTaskRepository.findByState(taskState.getValue());
    }
}
