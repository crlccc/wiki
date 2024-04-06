package com.example.wiki.service;

import com.example.wiki.websocket.WebSocketServer;
import jakarta.annotation.Resource;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Service
public class WsService {

    @Resource
    public WebSocketServer webSocketServer;

    public void sendInfo(String message,String logId){
        MDC.put("LOG_ID",logId);
        webSocketServer.sendInfo(message);
    }
}
