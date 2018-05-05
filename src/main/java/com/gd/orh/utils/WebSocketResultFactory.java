package com.gd.orh.utils;

import com.gd.orh.entity.WebSocketResult;

public class WebSocketResultFactory {

    public static WebSocketResult getWebSocketResult(String message, Object data) {
        return new WebSocketResult()
                .setMessage(message)
                .setData(data);
    }
}
