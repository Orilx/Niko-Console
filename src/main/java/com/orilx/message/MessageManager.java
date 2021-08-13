package com.orilx.message;

import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;

/**
 * 包装消息链并发送
 */
public class MessageManager {
    /**
     * 发送消息链
     * @param event 触发消息事件
     * @param msg 消息链
     */
    public static void sendMessage(MessageEvent event, MessageChain msg){

        event.getSubject().sendMessage(msg);
    }

    /**
     * 发送文本消息
     * @param event 触发消息事件
     * @param msg 消息链
     */
    public static void sendMessage(MessageEvent event, String msg){
        event.getSubject().sendMessage(msg);
    }
}
