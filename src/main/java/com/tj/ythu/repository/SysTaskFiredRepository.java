package com.tj.ythu.repository;

import com.tj.ythu.entity.SysTaskFired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 系统任务触发持久化
 *
 * @author ythu
 * @date 2019/10/30 16:09
 */
public interface SysTaskFiredRepository extends JpaRepository<SysTaskFired, Long> {

    @Modifying
    @Query(nativeQuery = true, value = "update t_sys_task_fired set state = :state where id= :id")
    int updateState(@Param("id") long id, @Param("state") int state);

    List<SysTaskFired> findByState(int state);
}
