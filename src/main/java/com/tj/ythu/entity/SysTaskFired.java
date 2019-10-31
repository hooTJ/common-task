package com.tj.ythu.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 系统任务触发实体
 *
 * @author ythu
 * @date 2019/10/30 15:43
 */
@Entity
@Table(name = "t_sys_task_fired")
@Data
public class SysTaskFired {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 创建时间
     */
    private Date time;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 触发类型（1主动，2被动）
     */
    private Integer type;

    /**
     * 任务执行的状态（1等待运行，2正在运行，-1正常停止，-2异常停止）
     */
    private Integer state;

    /**
     * 创建时间
     */
    private Date updateTime;
}