package com.orilx.command;

import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;

public abstract class Command {

    /**
     * 无参运行指令
     * @param event 消息事件
     */
    public abstract void runCommand(MessageEvent event);

    /**
     * 传参运行指令
     * @param event 消息事件
     * @param param 参数
     */
    public abstract void runCommandWithParam(MessageEvent event,String param);

    /**
     * 帮助文档
     * @param event 消息事件
     */
    public abstract MessageChain getHelp(MessageEvent event);
}

