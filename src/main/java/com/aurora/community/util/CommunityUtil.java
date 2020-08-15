package com.aurora.community.util;

import java.util.UUID;

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
}