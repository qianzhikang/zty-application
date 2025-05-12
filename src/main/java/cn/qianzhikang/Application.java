package cn.qianzhikang;

import cn.qianzhikang.schedule.DynamicScheduleTask;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;

@SpringBootApplication
@EnableScheduling
public class Application implements CommandLineRunner {

    @Resource
    private DynamicScheduleTask dynamicScheduleTask;

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }

    @Override
    public void run(String... args) {
        dynamicScheduleTask.startAllTasks();
    }
}
