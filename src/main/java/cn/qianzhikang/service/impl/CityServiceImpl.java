package cn.qianzhikang.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.qianzhikang.common.CommonData;
import cn.qianzhikang.entity.Location;
import cn.qianzhikang.service.CityService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

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
        Assert.hasText(res,"接口调用失败");
        JSONObject json = JSONUtil.parseObj(res);
        List<Location> locations = new ArrayList<>();
        List locationObjs = json.get("location", List.class);
        Assert.notEmpty(locationObjs,"未知地区");
        locationObjs.forEach(item->locations.add(BeanUtil.toBean(item, Location.class)));
        return locations.get(0);
    }
}
