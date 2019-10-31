package com.tj.ythu.vo;

import lombok.Data;
import lombok.ToString;

/**
 * description
 *
 * @author ythu
 * @date 2019/10/30 14:07
 */
@Data
@ToString
public class SysTaskVO {
    private Long id;
    private String beanName;
    private String method;
    private String params;
    private String cronExpression;
    private String remark;
    private Integer state;
}