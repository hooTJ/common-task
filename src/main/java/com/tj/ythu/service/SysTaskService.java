package com.tj.ythu.service;

import com.tj.ythu.entity.SysTask;
import com.tj.ythu.enums.SysTaskStateEnum;
import com.tj.ythu.vo.SysTaskVO;

import java.util.List;

/**
 * description
 *
 * @author ythu
 * @date 2019/10/30 14:05
 */
public interface SysTaskService {
    long save(SysTaskVO sysTaskVO);

    int update(SysTaskVO sysTaskVO);

    int delete(Long id);

    int updateState(Long id, SysTaskStateEnum taskState);

    List<SysTask> findByState(SysTaskStateEnum taskState);
}
