package cn.qianzhikang.controller;

import cn.qianzhikang.RestResponse.RestResult;
import cn.qianzhikang.entity.UserScheduledTasks;
import cn.qianzhikang.service.UserScheduledTasksService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author qianzhikang
 */
@RestController
@RequestMapping("task")
public class TaskController {

    @Resource
    private UserScheduledTasksService userScheduledTasksService;

    @GetMapping("get")
    public RestResult getTaskInfo(@RequestParam String email){
        List<UserScheduledTasks> userScheduledTasksByEmail = userScheduledTasksService.getUserScheduledTasksByEmail(email);
        return new RestResult<>().success(userScheduledTasksByEmail);
    }
    @PostMapping("post")
    public RestResult putTask(@RequestBody UserScheduledTasks userScheduledTasks){
        userScheduledTasksService.save(userScheduledTasks);
        return new RestResult<>("success");
    }

    @DeleteMapping("delete")
    public RestResult deleteTask(@RequestParam Integer id){
        userScheduledTasksService.removeById(id);
        return new RestResult<>("success");
    }


    @PutMapping("put")
    public RestResult updateTask(@RequestBody UserScheduledTasks userScheduledTasks){
        userScheduledTasksService.updateById(userScheduledTasks);
        return new RestResult<>("success");
    }
}
