package cn.qianzhikang.schedule;

import cn.qianzhikang.entity.CitySetting;
import cn.qianzhikang.service.CitySettingService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @author qianzhikang
 */
@Component
public class DynamicScheduleTask {
    @Resource
    private CitySettingService citySettingService;
    @Resource
    private ThreadPoolTaskScheduler scheduler;

    @Resource
    private TaskRunner taskRunner;

    private final Map<Integer, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    public void startAllTasks() {
        List<CitySetting> tasks = citySettingService.list(
                new LambdaQueryWrapper<CitySetting>().eq(CitySetting::getStatus, 1)
        );
        tasks.forEach(this::scheduleTask);
    }

    public void scheduleTask(CitySetting task) {
        Runnable runnable = () -> taskRunner.run(task);
        CronTrigger trigger = new CronTrigger(task.getCron());
        ScheduledFuture<?> future = scheduler.schedule(runnable, trigger);
        scheduledTasks.put(task.getId(), future);
    }

    public void cancelTask(Integer id) {
        ScheduledFuture<?> future = scheduledTasks.get(id);
        if (future != null) {
            future.cancel(true);
        }
    }

    public void reloadTask(Integer id) {
        cancelTask(id);
        CitySetting task = citySettingService.getById(id);
        if (task != null && (task.getStatus() == 1)) {
            scheduleTask(task);
        }
    }
}
