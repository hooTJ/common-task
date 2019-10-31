package com.tj.ythu.task.details;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 测试任务
 *
 * @author ythu
 * @date 2019/10/30 14:20
 */
@Component
public class DemoTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoTask.class);

    public void execute() {
        LOGGER.info("我被执行了，没有参数哦....");
    }
}
