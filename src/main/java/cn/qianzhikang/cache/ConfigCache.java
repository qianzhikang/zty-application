package cn.qianzhikang.cache;

import cn.qianzhikang.entity.AppConfig;
import cn.qianzhikang.mapper.AppConfigMapper;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 启动时 从数据库中读取相关配置信息
 * @author qianzhikang
 */
@Component
public class ConfigCache implements ApplicationRunner {

    @Resource
    private AppConfigMapper appConfigMapper;

    private static final Map<String, String> CONFIG_MAP = new ConcurrentHashMap<>();
    @Override
    public void run(ApplicationArguments args) {
        loadConfigs();
    }

    public void loadConfigs() {
        List<AppConfig> list = appConfigMapper.selectList(null);
        CONFIG_MAP.clear();
        for (AppConfig config : list) {
            CONFIG_MAP.put(config.getConfigKey(), config.getConfigValue());
        }
        System.out.println("配置加载完成：" + CONFIG_MAP.keySet());
    }

    public static String get(String key) {
        return CONFIG_MAP.get(key);
    }

    public static Map<String, String> all() {
        return new HashMap<>(CONFIG_MAP);
    }
}
