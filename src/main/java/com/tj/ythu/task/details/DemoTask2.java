package com.tj.ythu.task.details;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * description
 *
 * @author ythu
 * @date 2019/10/31 14:21
 */
@Component
public class DemoTask2 {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoTask2.class);

    public void execute(String params) {
        LOGGER.info("我被执行了，有参数哦....{}", params);
    }
}
