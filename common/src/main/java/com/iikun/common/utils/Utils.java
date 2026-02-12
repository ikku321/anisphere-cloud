package com.iikun.common.utils;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

/**
 * author iikun
 * time 2025/9/19 22:29
 * version 1.0.0
 * msg:
 */
public class Utils{

    // 生成用户uuid
    public static String shortUUID() {
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(ByteBuffer.wrap(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8)).array())
                .substring(0, 22);
    }


}

