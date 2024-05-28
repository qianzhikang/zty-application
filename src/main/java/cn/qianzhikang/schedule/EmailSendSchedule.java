package cn.qianzhikang.schedule;

import cn.hutool.extra.mail.MailUtil;
import cn.qianzhikang.common.EmailTemplate;
import cn.qianzhikang.entity.HourlyData;
import cn.qianzhikang.entity.Location;
import cn.qianzhikang.entity.UserScheduledTasks;
import cn.qianzhikang.mapper.UserScheduledTasksMapper;
import cn.qianzhikang.service.WeatherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 邮件发送定时任务
 */
@Component
public class EmailSendSchedule {

    @Resource
    private UserScheduledTasksMapper userScheduledTasksMapper;

    @Resource
    private WeatherService weatherService;

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
                /// 间隔任务
                // 获取下次运行时间
                LocalTime nextRunTime = convertDateToLocalTime(userScheduledTask.getNextRunTime());
                // 获取上次执行时间
                LocalDateTime lastModifiedTime = convertDateToLocalDateTime(userScheduledTask.getLastModifiedTime());
                // 计算理论下次应当执行的时间
                LocalDateTime intervalEndTime = lastModifiedTime
                        .plusHours(userScheduledTask.getIntervalHours().getHours())
                        .plusMinutes(userScheduledTask.getIntervalHours().getMinutes())
                        .plusSeconds(userScheduledTask.getIntervalHours().getSeconds());
                // 判断是否在允许的时间误差内 或 已经超时未执行
                if (isTimeWithinTolerance(currentTime, nextRunTime)
                        || (nextRunTime.isBefore(currentTime) && intervalEndTime.isBefore(LocalDateTime.now()))) {
                    shouldSendEmail = true;
                    updateNextRunTime(userScheduledTask, nextRunTime.isBefore(currentTime) && intervalEndTime.isBefore(LocalDateTime.now()));

                }
            }
            if (shouldSendEmail) {
                sendEmail(userScheduledTask);
            }
        }
    }


    private void sendEmail(UserScheduledTasks userScheduledTask) {
        // 获取地区位置
        Location location = new Location();
        location.setName(userScheduledTask.getCityName());
        location.setLon(userScheduledTask.getLon());
        location.setLat(userScheduledTask.getLat());
        // 查询天气信息
        List<HourlyData> hourlyData = weatherService.queryWeatherFor24WithLocation(location);
        Assert.notEmpty(hourlyData, "天气API异常");

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
     * 时间转换（含年月日）
     *
     * @param date
     * @return
     */
    private LocalDateTime convertDateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
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


    /**
     * 更新下次运行时间
     *
     * @param userScheduledTask
     * @param isTimeout
     */
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
        UserScheduledTasks userScheduledTasks = new UserScheduledTasks();
        userScheduledTasks.setId(userScheduledTask.getId());
        userScheduledTasks.setNextRunTime(Date.from(nextRunTime.atZone(ZoneId.systemDefault()).toInstant()));
        userScheduledTasksMapper.updateById(userScheduledTasks);
    }
}
