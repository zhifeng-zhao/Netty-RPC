package com.zzf.utils;

import com.zzf.model.Response;

/**
 * @author zzf
 * @date 2022/7/31 4:36 下午
 */
public class ResponseUtil {

    public static Response createSuccessResult() {
        return new Response();
    }

    public static Response createFailResult(String code, String msg) {
        Response response = new Response();
        response.setCode(code);
        response.setMsg(msg);
        return response;
    }

    public static Response createSuccessResult(Object content) {
        Response response = new Response();
        response.setContent(content);
        return response;
    }
}
