package cn.qianzhikang;

import cn.hutool.core.lang.Console;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);

        // 支持秒级别定时任务
        CronUtil.setMatchSecond(true);

        // 使用deamon模式
        CronUtil.start(true);
    }
}
