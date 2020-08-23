package com.aurora.community.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

public class CommunityUtil {
    
    // generate a random string
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    // MD5 encryption hello->abc12345,
    // add salt: hello + 3e4b8 -> adfshn8913
    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    public static String getJSONString(int code, String message, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("message", message);
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                json.put(entry.getKey(), entry.getValue());
            }
        }
        return json.toJSONString();
    }
    // overload previous method
    public static String getJSONString(int code, String message) {
        return getJSONString(code, message, null);
    }
    public static String getJSONString(int code) {
        return getJSONString(code, null, null);
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "lisa");
        map.put("age", 23);
        System.out.println(getJSONString(0, "ok", map));
    }
}