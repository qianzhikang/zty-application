package cn.qianzhikang.schedule;

import cn.hutool.extra.mail.MailUtil;
import cn.qianzhikang.entity.UserScheduledTasks;
import cn.qianzhikang.mapper.UserScheduledTasksMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;


/**
 * 邮件发送定时任务
 *
 * @author qianzhikang
 */

@Component
public class EmailSendSchedule {

    @Resource
    private UserScheduledTasksMapper userScheduledTasksMapper;

    // 允许的时间误差
    @Value("${app.constant.default-time-tolerance}")
    private Integer DEFAULT_TIME_TOLERANCE;

    @Scheduled(cron = "*/15 * * * * *")
    public void sendEmailSchedule() {
        System.out.println("查询数据库中");
        // 查询status为1的记录
        QueryWrapper<UserScheduledTasks> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        List<UserScheduledTasks> userScheduledTasks = userScheduledTasksMapper.selectList(queryWrapper);

        // 获取当前时间
        LocalTime currentTime = LocalTime.now();

        // 获取当前的小时数,分钟数,秒数
        int currentHour = currentTime.getHour();
        int currentMinute = currentTime.getMinute();
        int currentSecond = currentTime.getSecond();

        for (UserScheduledTasks userScheduledTask : userScheduledTasks) {
            // 遍历所有的Task，如果条件满足就发送邮件
            // 任务类型：0-每日任务，1-间隔任务
            if (userScheduledTask.getTaskType() == 0) {
                int startHour = userScheduledTask.getStartTime().getHours();
                int startMinute = userScheduledTask.getStartTime().getMinutes();
                int startSecond = userScheduledTask.getStartTime().getSeconds();
                // 如果当前时间在指定的时间范围内，就发送邮件
                if (currentHour == startHour && currentMinute == startMinute
                        && Math.abs(currentSecond - startSecond) < DEFAULT_TIME_TOLERANCE) {
                    // 发送邮件
                    System.out.println("发送邮件");
                    MailUtil.send("lmxxmlsq@163.com", "测试", "邮件来自Hutool测试", false);
                }
            }
            // 间隔任务
            if (userScheduledTask.getTaskType() == 1) {
                int nextRunHour = userScheduledTask.getNextRunTime().getHours();
                int nextRunMinute = userScheduledTask.getNextRunTime().getMinutes();
                int nextRunSecond = userScheduledTask.getNextRunTime().getSeconds();
                if (currentHour == nextRunHour && currentMinute == nextRunMinute
                        && Math.abs(currentSecond - nextRunSecond) < DEFAULT_TIME_TOLERANCE) {
                    try {
                        // 发送邮件
                        System.out.println("发送邮件");
                        MailUtil.send("lmxxmlsq@163.com", "测试", "邮件来自Hutool测试", false);
                    } finally {
                        // 重新设置下次运行时间
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(userScheduledTask.getNextRunTime());
                        calendar.add(Calendar.HOUR, userScheduledTask.getIntervalHours().getHours());
                        calendar.add(Calendar.MINUTE, userScheduledTask.getIntervalHours().getMinutes());
                        calendar.add(Calendar.SECOND, userScheduledTask.getIntervalHours().getSeconds());

                        // 将nextRunTime更新为计算后的时间
                        userScheduledTask.setNextRunTime(calendar.getTime());
                        userScheduledTasksMapper.updateById(userScheduledTask);
                    }
                }
            }
        }
    }
}
