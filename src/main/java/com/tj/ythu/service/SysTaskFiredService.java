package com.tj.ythu.service;

import com.tj.ythu.entity.SysTaskFired;
import com.tj.ythu.enums.SysTaskFiredStateEnum;
import com.tj.ythu.vo.SysTaskFiredVO;

import java.util.List;

/**
 * description
 *
 * @author ythu
 * @date 2019/10/30 16:12
 */
public interface SysTaskFiredService {

    long save(SysTaskFiredVO sysTaskFiredVO);

    int updateState(Long id, SysTaskFiredStateEnum firedState);

    List<SysTaskFired> findByState(SysTaskFiredStateEnum firedState);
}
