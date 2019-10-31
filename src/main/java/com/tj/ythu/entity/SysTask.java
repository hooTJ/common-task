package com.tj.ythu.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 系统任务实体
 *
 * @author ythu
 * @date 2019/10/24 19:40
 */
@Entity
@Table(name = "t_sys_task")
@Data
public class SysTask {
    /**
     * 任务ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 创建时间
     */
    private Date time;
    /**
     * bean名称
     */
    private String beanName;
    /**
     * 方法名称
     */
    private String method;
    /**
     * 方法参数
     */
    private String params;
    /**
     * cron表达式
     */
    private String cronExpression;
    /**
     * 备注
     */
    private String remark;
    /**
     * 状态（1正常 0暂停）
     */
    private Integer state;
    /**
     * 删除标识（0未执行删除，1执行删除）
     */
    private Boolean delFlag;
    /**
     * 更新时间
     */
    private Date updateTime;
}

