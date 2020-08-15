package com.aurora.community.util;

public interface CommunityConstant {
    // activate successfully
    static final int ACTIVATION_SUCCESS = 0;

    // activate repeatly
    static final int ACTIVATION_REPEAT = 1;

    // activate failure - invalid url
    static final int ACTIVATION_FAIL = 2;
    
    // default expired seconds
    static final int DEFAULT_EXPIRED_SECONDS = 3600 * 12;
    // remember me expired second
    static final int REMEMBER_EXPIRED_SECONDS = 100 * 24 * 3600;

}