package com.tj.ythu;

import com.tj.ythu.enums.SysTaskStateEnum;
import com.tj.ythu.service.SysTaskService;
import com.tj.ythu.vo.SysTaskVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 要求：
 * 1、任务可以配置；
 * 2、是否在执行能看到；
 * 3、异常信息能看到；
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.tj.ythu.repository"})
public class CommonTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommonTaskApplication.class, args);
    }


    @Autowired
    private SysTaskService sysTaskService;

    /**
     * 无参数
     *
     * @return
     */
    @Bean
    public ApplicationRunner runner01() {
        return args -> {
            SysTaskVO sysTaskVO = new SysTaskVO();
            sysTaskVO.setBeanName("demoTask");
            sysTaskVO.setMethod("execute");
            sysTaskVO.setRemark("hooTJ test non params...");
            sysTaskVO.setCronExpression("0/10 * * * * *");
            sysTaskVO.setState(SysTaskStateEnum.NORMAL.getValue());
            sysTaskService.save(sysTaskVO);
        };
    }

    /**
     * 无参数
     *
     * @return
     */
    @Bean
    public ApplicationRunner runner02() {
        return args -> {
            SysTaskVO sysTaskVO = new SysTaskVO();
            sysTaskVO.setBeanName("demoTask2");
            sysTaskVO.setMethod("execute");
            sysTaskVO.setRemark("hooTJ test with params...");
            sysTaskVO.setCronExpression("0/30 * * * * *");
            sysTaskVO.setParams("hooTJ");
            sysTaskVO.setState(SysTaskStateEnum.NORMAL.getValue());
            sysTaskService.save(sysTaskVO);
        };
    }
}