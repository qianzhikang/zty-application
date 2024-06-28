package cn.qianzhikang.schedule;

import cn.hutool.extra.mail.MailUtil;
import cn.qianzhikang.common.EmailTemplate;
import cn.qianzhikang.entity.HourlyData;
import cn.qianzhikang.entity.Location;
import cn.qianzhikang.entity.UserScheduledTasks;
import cn.qianzhikang.service.UserScheduledTasksService;
import cn.qianzhikang.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.time.*;
import java.util.Date;
import java.util.List;

import java.time.LocalDateTime;

import java.time.ZoneId;

/**
 * @author qianzhikang
 */

@Slf4j
@Component
public class WeatherSchedule {


    @Resource
    private UserScheduledTasksService userScheduledTasksService;

    @Resource
    private WeatherService weatherService;

    // 允许的时间误差
    @Value("${app.constant.default-time-tolerance}")
    private Integer DEFAULT_TIME_TOLERANCE;

    @Scheduled(cron = "*/15 * * * * *")
    public void taskSchedule() {
        LocalDateTime now = LocalDateTime.now();
        log.info("执行中..当前时间：{}", now);
        // 执行每日任务
        List<UserScheduledTasks> dailyTasks = userScheduledTasksService.selectDailyTasks();
        for (UserScheduledTasks task : dailyTasks) {
            // 每日任务
            if (isTimeWithinTolerance(LocalTime.now(), convertDateToLocalTime(task.getStartTime()))) {
                sendEmail(task);
                updateLastExecutionTime(task, now);
            }
        }

        // 执行间隔任务
        List<UserScheduledTasks> intervalTasks = userScheduledTasksService.selectIntervalTasks(now);
        for (UserScheduledTasks task : intervalTasks) {
            if (shouldExecuteIntervalTask(task, now)) {
                // 执行
                sendEmail(task);
                // 更新任务执行时间
                updateLastExecutionTime(task, now);
            }
        }
    }


    /**
     * 发邮件
     * @param userScheduledTask
     */
    private void sendEmail(UserScheduledTasks userScheduledTask) {
        // 获取地区位置
        Location location = new Location();
        location.setName(userScheduledTask.getCityName());
        location.setLon(userScheduledTask.getLon());
        location.setLat(userScheduledTask.getLat());
        // 查询天气信息
        List<HourlyData> hourlyData = weatherService.queryWeatherFor24WithLocation(location);
        Assert.notEmpty(hourlyData, "天气API异常");

        log.info("为" + userScheduledTask.getEmail() + "发送邮件");
        MailUtil.send(userScheduledTask.getEmail(), "接下来一小时的天气情况", hourlyData.get(0).getText(), false);


        boolean isRain = false;
        for (HourlyData hourlyDatum : hourlyData) {
            if (hourlyDatum.getText().contains("雨")) {
                isRain = true;
                break;
            }
        }
        if (isRain){
            System.out.println("发送邮件");
            MailUtil.send(userScheduledTask.getEmail(), EmailTemplate.SUBJECT, EmailTemplate.CONTEXT, false);
        }
    }


    private boolean shouldExecuteIntervalTask(UserScheduledTasks task, LocalDateTime now) {
        Date lastModifiedTime = task.getLastModifiedTime();
        LocalDateTime localLastModifiedTime = convertDateToLocalDateTime(lastModifiedTime);
        // 到达或超过执行间隔
        if (task.getLastModifiedTime() == null ||
                Duration.between(localLastModifiedTime, now).toMinutes() >= task.getIntervalMinutes()) {
            return true;
        }
        return false;
    }


    /**
     * 是否在允许误差时间内
     *
     * @param currentTime
     * @param targetTime
     * @return
     */
    private boolean isTimeWithinTolerance(LocalTime currentTime, LocalTime targetTime) {
        int toleranceSeconds = Math.abs(currentTime.toSecondOfDay() - targetTime.toSecondOfDay());
        return toleranceSeconds < DEFAULT_TIME_TOLERANCE;
    }


    // 更新下次执行时间
    private void updateLastExecutionTime(UserScheduledTasks task, LocalDateTime executionTime) {
        Date execTime = convertLocalDateTimeToDate(executionTime);
        task.setLastModifiedTime(execTime);
        // 注意：这里假设有一个方法来保存更新后的实体，实际操作中可能需要事务管理
        userScheduledTasksService.updateById(task);
    }


    /**
     * 时间转换（不含年月日）
     *
     * @param date
     * @return
     */
    private LocalTime convertDateToLocalTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }

    /**
     * 时间转换
     * @param date
     * @return
     */
    private LocalDateTime convertDateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * 时间转换
     * @param localDateTime
     * @return
     */
    private Date convertLocalDateTimeToDate(LocalDateTime localDateTime) {
        // 将 LocalDateTime 转换为带时区的 ZonedDateTime
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        // 将 ZonedDateTime 转换为 Instant
        Instant instant = zonedDateTime.toInstant();
        // 从 Instant 创建 Date 对象
        return Date.from(instant);
    }

}
