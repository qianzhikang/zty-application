package cn.qianzhikang.service;

import cn.qianzhikang.entity.UserScheduledTasks;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
* @author qianzhikang
* @description 针对表【user_scheduled_tasks】的数据库操作Service
* @createDate 2024-05-24 11:56:15
*/
public interface UserScheduledTasksService extends IService<UserScheduledTasks> {
    List<UserScheduledTasks> getUserScheduledTasksByEmail(String email);

    void updateStatus(Integer id, String status);

    void insert(UserScheduledTasks userScheduledTasks);

    List<UserScheduledTasks> selectDailyOrIntervalTasks(Integer taskType, LocalDateTime now);
}
