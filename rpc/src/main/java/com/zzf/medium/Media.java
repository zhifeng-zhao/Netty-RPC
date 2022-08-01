package com.zzf.medium;

import com.alibaba.fastjson.JSONObject;
import com.zzf.model.Response;
import com.zzf.model.ServerRequest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zzf
 * @date 2022/7/31 12:00 下午
 */
public class Media {

    public static Map<String, BaseMedia> beanMap;

    static {
        beanMap = new HashMap<>();
    }

    private static Media media;

    public static Media newInstance() {
        if (media == null) {
            media = new Media();
        }
        return media;
    }

    /**
     * 反射处理业务逻辑
     *
     * @param request
     * @return
     */
    public Response process(ServerRequest request) {
        Response result = null;
        try {
            String command = request.getCommand();
            BaseMedia media = beanMap.get(command);
            Object bean = media.getBean();
            Method method = media.getMethod();
            Class<?> parameterType = method.getParameterTypes()[0];
            Object content = request.getContent();
            Object o = JSONObject.parseObject(JSONObject.toJSONString(content), parameterType);
            result = (Response) method.invoke(bean, o);
            result.setId(request.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
