package com.orilx.command.commands;

import com.orilx.command.Command;
import com.orilx.database.SignInService;
import com.orilx.message.MessageManager;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;

public class SignInCommand extends Command {
    SignInService sign = new SignInService();

    /**
     * 无参运行指令
     * @param event 消息事件
     */
    @Override
    public void runCommand(MessageEvent event) {
        int score = sign.signIn((GroupMessageEvent) event);
        MessageChain msg = new MessageChainBuilder()
                .append(new QuoteReply(event.getMessage()))
                .append("签到成功！\n当前积分: ").append(String.valueOf(score))
                .build();

        if(score == -1){
            msg = new MessageChainBuilder()
                    .append(new QuoteReply(event.getMessage()))
                    .append("你今天已经签到过啦！")
                    .build();
        }

        MessageManager.sendMessage(event,msg);
    }

    /**
     * 传参运行指令
     * @param event 消息事件
     * @param param 参数
     */
    @Override
    public void runCommandWithParam(MessageEvent event, String param) {
        MessageManager.sendMessage(event, "这个指令好像不需要参数捏~");
    }

    /**
     * 帮助文档
     * @param event 消息事件
     */
    @Override
    public MessageChain getHelp(MessageEvent event) {
        return new MessageChainBuilder()
                .append("签到获取积分，目前还没啥用")
                .build();
    }
}
