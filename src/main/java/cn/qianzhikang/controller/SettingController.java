package cn.qianzhikang.controller;
import cn.qianzhikang.RestResponse.RestResult;
import cn.qianzhikang.entity.CitySetting;
import cn.qianzhikang.service.CitySettingService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

@RestController
@RequestMapping("setting")
public class SettingController {

    @Resource
    private CitySettingService citySettingService;

    /**
     * 获取所有城市设置列表
     */
    @GetMapping("/list")
    public RestResult list() {
        return new RestResult<>().success(citySettingService.list());
    }

    /**
     * 添加城市设置
     */
    @PostMapping("/add")
    public RestResult add(@RequestBody CitySetting citySetting) {
        boolean save = citySettingService.save(citySetting);
        return new RestResult<>().success();
    }


    /**
     * 删除城市设置
     */
    @DeleteMapping("/delete")
    public RestResult delete(@RequestParam Integer id) {
        boolean remove = citySettingService.removeById(id);
        return new RestResult<>().success();
    }

    /**
     * 修改城市设置
     */
    @PutMapping("/update")
    public RestResult update(@RequestBody CitySetting citySetting) {
        boolean update = citySettingService.updateById(citySetting);
        return new RestResult<>().success();
    }

}
