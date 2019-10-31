package com.tj.ythu.repository;

import com.tj.ythu.entity.SysTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 系统任务持久化
 *
 * @author ythu
 * @date 2019/10/30 13:52
 */
public interface SysTaskRepository extends JpaRepository<SysTask, Long> {

    @Modifying
    @Query(nativeQuery = true, value = "update t_sys_task set state = :state where id= :id")
    int updateState(@Param("id") long id, @Param("state") int state);

    List<SysTask> findByState(int state);

    @Modifying
    @Query(nativeQuery = true, value = "update t_sys_task set del_flag = 1, state = 0 where id= :id and del_flag = 0")
    int updateDelFlag(@Param("id") long id);

}
