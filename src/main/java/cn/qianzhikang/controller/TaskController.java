package cn.qianzhikang.controller;

import cn.qianzhikang.RestResponse.RestResult;
import cn.qianzhikang.entity.UserScheduledTasks;
import cn.qianzhikang.service.UserScheduledTasksService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
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
    public RestResult postTask(@Valid @RequestBody UserScheduledTasks userScheduledTasks){
        userScheduledTasksService.insert(userScheduledTasks);
        return new RestResult<>().success();
    }

    @DeleteMapping("delete")
    public RestResult deleteTask(@RequestParam Integer id){
        userScheduledTasksService.removeById(id);
        return new RestResult<>().success();
    }


    @PutMapping("put")
    public RestResult updateTask(@RequestParam Integer id,@RequestParam String status){
        userScheduledTasksService.updateStatus(id,status);
        return new RestResult<>().success();
    }
}
