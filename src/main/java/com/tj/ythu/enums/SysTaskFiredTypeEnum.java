package com.tj.ythu.enums;

/**
 * 系统任务触发类型
 *
 * @author ythu
 * @date 2019/10/30 15:51
 */
public enum SysTaskFiredTypeEnum {
    ACTIVE(1, "主动"),

    PASSIVE(2, "被动");

    private int value;
    private String desc;

    SysTaskFiredTypeEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
