package cn.qianzhikang.schedule;

import cn.qianzhikang.cache.ConfigCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 定时刷新缓存中的配置
 * @author qianzhikang
 */
@Component
public class ConfigRefreshTask {
    @Resource
    private ConfigCache configCache;

    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void autoRefresh() {
        configCache.loadConfigs();
    }
}
