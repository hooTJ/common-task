package com.tj.ythu.enums;

/**
 * 系统任务状态
 *
 * @author ythu
 * @date 2019/10/25 17:14
 */
public enum SysTaskStateEnum {
    NORMAL(1, "正常"),

    PAUSE(0, "停止");

    private int value;
    private String desc;

    SysTaskStateEnum(int value, String desc) {
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