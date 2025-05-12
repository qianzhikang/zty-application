package cn.qianzhikang.controller;

import cn.qianzhikang.cache.ConfigCache;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/config")
public class ConfigRefreshController {
    @Resource
    private ConfigCache configCache;

    @PostMapping("/refresh")
    public String refresh() {
        configCache.loadConfigs();
        return "配置刷新完成";
    }
}
