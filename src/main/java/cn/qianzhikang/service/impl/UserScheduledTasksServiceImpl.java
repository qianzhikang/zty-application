package cn.qianzhikang.service.impl;

import cn.qianzhikang.entity.Location;
import cn.qianzhikang.service.CityService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qianzhikang.entity.UserScheduledTasks;
import cn.qianzhikang.service.UserScheduledTasksService;
import cn.qianzhikang.mapper.UserScheduledTasksMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
* @author qianzhikang
* @description 针对表【user_scheduled_tasks】的数据库操作Service实现
* @createDate 2024-05-24 11:56:15
*/
@Service
public class UserScheduledTasksServiceImpl extends ServiceImpl<UserScheduledTasksMapper, UserScheduledTasks>
    implements UserScheduledTasksService{

    @Resource
    private UserScheduledTasksMapper userScheduledTasksMapper;

    @Resource
    private CityService cityService;

    /**
     * 根据邮箱查询定时任务
     * @param email
     * @return
     */
    @Override
    public List<UserScheduledTasks> getUserScheduledTasksByEmail(String email) {
        LambdaQueryWrapper<UserScheduledTasks> userScheduledTasksLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userScheduledTasksLambdaQueryWrapper.eq(UserScheduledTasks::getEmail,email);
        return userScheduledTasksMapper.selectList(userScheduledTasksLambdaQueryWrapper);
    }

    /**
     * 更新定时任务状态
     * @param id
     * @param status
     */
    @Override
    public void updateStatus(Integer id, String status) {
        // 根据id更新状态
        LambdaQueryWrapper<UserScheduledTasks> userScheduledTasksLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userScheduledTasksLambdaQueryWrapper.eq(UserScheduledTasks::getId,id);
        UserScheduledTasks userScheduledTasks = new UserScheduledTasks();
        userScheduledTasks.setStatus(status);
        userScheduledTasksMapper.update(userScheduledTasks,userScheduledTasksLambdaQueryWrapper);
    }

    /**
     * 插入定时任务
     * @param userScheduledTasks
     */
    @Override
    public void insert(UserScheduledTasks userScheduledTasks) {
        String cityName = userScheduledTasks.getCityName();
        Location location = cityService.queryCityInfo(cityName);
        userScheduledTasks.setLon(location.getLon());
        userScheduledTasks.setLat(location.getLat());
        userScheduledTasksMapper.insert(userScheduledTasks);
    }

    @Override
    public List<UserScheduledTasks> selectIntervalTasks(LocalDateTime now) {
        LambdaQueryWrapper<UserScheduledTasks> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserScheduledTasks::getTaskType,1);
        queryWrapper.le(UserScheduledTasks::getStartTime,now);
        queryWrapper.ge(UserScheduledTasks::getShutdownTime,now);
        return userScheduledTasksMapper.selectList(queryWrapper);
    }

    @Override
    public List<UserScheduledTasks> selectDailyTasks() {
        LambdaQueryWrapper<UserScheduledTasks> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserScheduledTasks::getTaskType,0);
        return userScheduledTasksMapper.selectList(queryWrapper);
    }
}




