package cn.qianzhikang.schedule;

import cn.hutool.extra.mail.MailUtil;
import cn.qianzhikang.common.EmailTemplate;
import cn.qianzhikang.entity.HourlyData;
import cn.qianzhikang.entity.Location;
import cn.qianzhikang.entity.UserScheduledTasks;
import cn.qianzhikang.mapper.UserScheduledTasksMapper;
import cn.qianzhikang.service.WeatherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.time.LocalDate;
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
@Slf4j
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
        log.info("查询数据库中");

        // 查询status为1的记录
        QueryWrapper<UserScheduledTasks> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        List<UserScheduledTasks> userScheduledTasks = userScheduledTasksMapper.selectList(queryWrapper);

        // 获取当前时间
        LocalTime currentTime = LocalTime.now();

        for (UserScheduledTasks userScheduledTask : userScheduledTasks) {
            boolean shouldSendEmail = false;
            LocalTime taskStartTime = convertDateToLocalTime(userScheduledTask.getStartTime());
            if (userScheduledTask.getTaskType() == 0) {
                // 每日任务
                if (isTimeWithinTolerance(currentTime, taskStartTime)) {
                    shouldSendEmail = true;
                    updateLastModifiedTime(userScheduledTask.getId());
                }
            } else if (userScheduledTask.getTaskType() == 1) {
                /// 间隔任务
                // 获取下次运行时间
                LocalTime nextRunTime = convertDateToLocalTime(userScheduledTask.getNextRunTime());
                // 获取上次执行时间
                LocalDateTime lastModifiedTime = convertDateToLocalDateTime(userScheduledTask.getLastModifiedTime());
                // 获取免打扰时间
                LocalTime shutdownTime = convertDateToLocalTime(userScheduledTask.getShutdownTime());
                // 计算理论下次应当执行的时间
                LocalDateTime intervalEndTime = lastModifiedTime
                        .plusHours(userScheduledTask.getIntervalHours().getHours())
                        .plusMinutes(userScheduledTask.getIntervalHours().getMinutes())
                        .plusSeconds(userScheduledTask.getIntervalHours().getSeconds());
                // 判断是否在允许的时间误差内 或 已经超时未执行
                if (isTimeWithinTolerance(currentTime, nextRunTime)
                        || (nextRunTime.isBefore(currentTime) && intervalEndTime.isBefore(LocalDateTime.now()))) {
                    // 判断是否在免打扰时间之前
                    if (currentTime.isAfter(taskStartTime) && currentTime.isBefore(shutdownTime)){
                        shouldSendEmail = true;
                    }
                    // 更新下次运行时间
                    updateNextRunTime(userScheduledTask,(nextRunTime.isBefore(currentTime) && intervalEndTime.isBefore(LocalDateTime.now())));

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

        log.info("为"+userScheduledTask.getEmail()+"发送邮件");
        MailUtil.send(userScheduledTask.getEmail(),"接下来一小时的天气情况",hourlyData.get(0).getText(),false);



//        boolean isRain = false;
//        for (HourlyData hourlyDatum : hourlyData) {
//            if (hourlyDatum.getText().contains("雨")) {
//                isRain = true;
//                break;
//            }
//        }
//        if (isRain){
//            System.out.println("发送邮件");
//            MailUtil.send(userScheduledTask.getEmail(), EmailTemplate.SUBJECT, EmailTemplate.CONTEXT, false);
//        }
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
     * 若任务由于某种原因滞后了，未在规定时间执行，则将会把下次运行时间更新为在当前时间之后的下一次执行周期
     * @param userScheduledTask
     * @param
     */
    private void updateNextRunTime(UserScheduledTasks userScheduledTask, boolean isLate) {
        LocalTime nextRunTime = convertDateToLocalTime(userScheduledTask.getNextRunTime());
        LocalTime startTime = convertDateToLocalTime(userScheduledTask.getStartTime());
        LocalTime shutdownTime = convertDateToLocalTime(userScheduledTask.getShutdownTime());
        LocalTime now = LocalTime.now();
        if (isLate){
            // 从开始时间重新计算下一个执行周期的时间
            nextRunTime = startTime;
            while (!nextRunTime.isAfter(now)){
                nextRunTime = nextRunTime.plusHours(userScheduledTask.getIntervalHours().getHours())
                        .plusMinutes(userScheduledTask.getIntervalHours().getMinutes())
                        .plusSeconds(userScheduledTask.getIntervalHours().getSeconds());
            }
        }else {
            // 更新为下一个执行周期的时间
            while (!nextRunTime.isAfter(now)) {
                nextRunTime = nextRunTime.plusHours(userScheduledTask.getIntervalHours().getHours())
                        .plusMinutes(userScheduledTask.getIntervalHours().getMinutes())
                        .plusSeconds(userScheduledTask.getIntervalHours().getSeconds());
            }
        }
        // 检查nextRunTime是否落在免打扰时间段内
        if (!nextRunTime.isAfter(shutdownTime) && nextRunTime.isBefore(startTime)) {
            // 调整nextRunTime到开始时间之后的下一个执行周期
            nextRunTime = startTime;
        }
        UserScheduledTasks userScheduledTasks = new UserScheduledTasks();
        userScheduledTasks.setId(userScheduledTask.getId());
        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), nextRunTime);
        userScheduledTasks.setNextRunTime(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        userScheduledTasksMapper.updateById(userScheduledTasks);
    }

    /**
     * 更新执行时间
     * @param id
     */
    private void updateLastModifiedTime(Integer id) {
        QueryWrapper<UserScheduledTasks> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        UserScheduledTasks userScheduledTasks = new UserScheduledTasks();
        userScheduledTasks.setLastModifiedTime(new Date());
        userScheduledTasksMapper.update(userScheduledTasks, queryWrapper);
    }
}
