package com.tj.ythu.vo;

import lombok.Data;

/**
 * description
 *
 * @author ythu
 * @date 2019/10/30 16:13
 */
@Data
public class SysTaskFiredVO {
    private Long id;

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
}
