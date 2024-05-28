package cn.qianzhikang.schedule;

import cn.hutool.extra.mail.MailUtil;
import cn.qianzhikang.entity.UserScheduledTasks;
import cn.qianzhikang.mapper.UserScheduledTasksMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 邮件发送定时任务
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

        for (UserScheduledTasks userScheduledTask : userScheduledTasks) {
            boolean shouldSendEmail = false;

            if (userScheduledTask.getTaskType() == 0) {
                // 每日任务
                LocalTime taskStartTime = convertDateToLocalTime(userScheduledTask.getStartTime());
                if (isTimeWithinTolerance(currentTime, taskStartTime)) {
                    shouldSendEmail = true;
                }
            } else if (userScheduledTask.getTaskType() == 1) {
                // 间隔任务
                LocalTime nextRunTime = convertDateToLocalTime(userScheduledTask.getNextRunTime());
                LocalDateTime lastModifiedTime = convertDateToLocalDateTime(userScheduledTask.getLastModifiedTime());
                LocalDateTime intervalEndTime = lastModifiedTime
                        .plusHours(userScheduledTask.getIntervalHours().getHours())
                        .plusMinutes(userScheduledTask.getIntervalHours().getMinutes())
                        .plusSeconds(userScheduledTask.getIntervalHours().getSeconds());

                if (isTimeWithinTolerance(currentTime, nextRunTime)
                        || (nextRunTime.isBefore(currentTime) && intervalEndTime.isBefore(LocalDateTime.now()))) {
                    shouldSendEmail = true;
                    updateNextRunTime(userScheduledTask, nextRunTime.isBefore(currentTime) && intervalEndTime.isBefore(LocalDateTime.now()));

                }
            }

            if (shouldSendEmail) {
                System.out.println("发送邮件");
                MailUtil.send("957463620@qq.com", "测试", "邮件来自Hutool测试", false);
            }
        }
    }

    private LocalTime convertDateToLocalTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }

    private LocalDateTime convertDateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    private boolean isTimeWithinTolerance(LocalTime currentTime, LocalTime targetTime) {
        int toleranceSeconds = Math.abs(currentTime.toSecondOfDay() - targetTime.toSecondOfDay());
        return toleranceSeconds < DEFAULT_TIME_TOLERANCE;
    }


    private void updateNextRunTime(UserScheduledTasks userScheduledTask, boolean isTimeout) {
        LocalDateTime nextRunTime = convertDateToLocalDateTime(userScheduledTask.getNextRunTime());
        if (isTimeout) {
            nextRunTime = LocalDateTime.now().withSecond(0).withNano(0)
                    .plusHours(userScheduledTask.getIntervalHours().getHours())
                    .plusMinutes(userScheduledTask.getIntervalHours().getMinutes())
                    .plusSeconds(userScheduledTask.getIntervalHours().getSeconds());
        } else {
            nextRunTime = nextRunTime.plusHours(userScheduledTask.getIntervalHours().getHours())
                    .plusMinutes(userScheduledTask.getIntervalHours().getMinutes())
                    .plusSeconds(userScheduledTask.getIntervalHours().getSeconds());
        }
        userScheduledTasksMapper.updateById(UserScheduledTasks.builder().id(userScheduledTask.getId()).nextRunTime(Date.from(nextRunTime.atZone(ZoneId.systemDefault()).toInstant())).build());
    }
}
