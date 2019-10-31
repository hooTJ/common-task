package com.tj.ythu.service.impl;

import com.tj.ythu.entity.SysTaskFired;
import com.tj.ythu.enums.SysTaskFiredStateEnum;
import com.tj.ythu.repository.SysTaskFiredRepository;
import com.tj.ythu.service.SysTaskFiredService;
import com.tj.ythu.vo.SysTaskFiredVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 系统任务执行业务
 *
 * @author ythu
 * @date 2019/10/30 16:12
 */
@Service
public class SysTaskFiredServiceImpl implements SysTaskFiredService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysTaskFiredServiceImpl.class);

    @Autowired
    private SysTaskFiredRepository sysTaskFiredRepository;

    /**
     * 保存
     *
     * @param sysTaskFiredVO
     * @return
     */
    @Transactional
    @Override
    public long save(SysTaskFiredVO sysTaskFiredVO) {
        if (Objects.isNull(sysTaskFiredVO) || StringUtils.isEmpty(sysTaskFiredVO.getTaskId()) || StringUtils.isEmpty(sysTaskFiredVO.getType())) {
            LOGGER.error("调用参数非法...{}", sysTaskFiredVO);
            throw new RuntimeException("调用参数非法");
        }
        SysTaskFired sysTaskFired = new SysTaskFired();
        BeanUtils.copyProperties(sysTaskFiredVO, sysTaskFired);
        sysTaskFired.setTime(new Date());
        sysTaskFired.setUpdateTime(new Date());
        if (sysTaskFiredVO.getState() == null) {
            sysTaskFired.setState(SysTaskFiredStateEnum.WAIT.getValue());
        }

        SysTaskFired entity = sysTaskFiredRepository.save(sysTaskFired);
        if (Objects.isNull(entity) || entity.getId() < 1) {
            LOGGER.error("保存失败...{}", sysTaskFiredVO);
            throw new RuntimeException("保存失败");
        }

        return entity.getId();
    }

    /**
     * 更改状态
     *
     * @param id
     * @param firedState
     * @return
     */
    @Transactional
    @Override
    public int updateState(Long id, SysTaskFiredStateEnum firedState) {
        int rows = sysTaskFiredRepository.updateState(id, firedState.getValue());
        if (rows < 1) {
            LOGGER.error("更新状态失败...{}", rows);
            throw new RuntimeException("更新状态失败");
        }
        return rows;
    }

    /**
     * 查询某个状态的列表
     *
     * @param firedState
     * @return
     */
    @Override
    public List<SysTaskFired> findByState(SysTaskFiredStateEnum firedState) {
        return sysTaskFiredRepository.findByState(firedState.getValue());
    }
}
