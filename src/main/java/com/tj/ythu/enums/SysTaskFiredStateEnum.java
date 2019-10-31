package com.tj.ythu.enums;

/**
 * 系统任务触发状态
 *
 * @author ythu
 * @date 2019/10/30 15:51
 */
public enum SysTaskFiredStateEnum {
    WAIT(1, "等待运行"),

    RUN(2, "正在运行"),

    STOP(-1, "正常停止"),

    DESTROY(-2, "容器销毁停止"),

    EXCEPTION(-3, "异常停止");

    private int value;
    private String desc;

    SysTaskFiredStateEnum(int value, String desc) {
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
