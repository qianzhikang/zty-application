package cn.qianzhikang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qianzhikang.entity.UserScheduledTasks;
import cn.qianzhikang.service.UserScheduledTasksService;
import cn.qianzhikang.mapper.UserScheduledTasksMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
        UserScheduledTasks userScheduledTasks = UserScheduledTasks.builder().status(status).build();
        userScheduledTasksMapper.update(userScheduledTasks,userScheduledTasksLambdaQueryWrapper);
    }
}




