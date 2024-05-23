package cn.qianzhikang.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.qianzhikang.common.CommonData;
import cn.qianzhikang.entity.Location;
import cn.qianzhikang.service.CityService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qianzhikang
 */
@Service
public class CityServiceImpl implements CityService {
    /**
     * 查询城市信息
     * @param cityName 城市名
     * @return 同一返回
     */
    @Override
    public Location queryCityInfo(String cityName) {
        String URL = CommonData.cityInfoHost + "location=" + cityName + "&key=" + CommonData.key;
        String res = HttpUtil.get(URL);
        JSONObject json = JSONUtil.parseObj(res);
        List<Location> locations = new ArrayList<>();
        List locationObjs = json.get("location", List.class);
        locationObjs.forEach(item->locations.add(BeanUtil.toBean(item, Location.class)));
        if (locationObjs.isEmpty()) {
            return null;
        }
        return locations.get(0);
    }
}
