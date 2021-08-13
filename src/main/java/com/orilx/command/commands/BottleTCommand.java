package com.orilx.command.commands;

import com.orilx.command.Command;
import com.orilx.database.DriftBottleService;
import com.orilx.message.MessageManager;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;

public class BottleTCommand extends Command {
    DriftBottleService dbs;

    public BottleTCommand(){
        dbs = new DriftBottleService();
    }

    /**
     * 无参运行指令
     *
     * @param event 消息事件
     */
    @Override
    public void runCommand(MessageEvent event) {
        MessageManager.sendMessage(event,"扔出失败:你还没有写信呢~");
    }

    /**
     * 传参运行指令
     *
     * @param event 消息事件
     * @param param 参数
     */
    @Override
    public void runCommandWithParam(MessageEvent event, String param) {
        String para = event.getMessage().serializeToMiraiCode().replaceAll("/扔瓶子","").trim();
        if(para.length() == 0){
            MessageManager.sendMessage(event,"扔出失败:你还没有写信呢~");
            return;
        }
        dbs.add((GroupMessageEvent) event,para);
        int id = dbs.getMaxId();
        MessageChain msg = new MessageChainBuilder()
                .append(new QuoteReply(event.getMessage()))
                .append("扔出成功！(" + id +")")
                .build();
        MessageManager.sendMessage(event, msg);
    }

    /**
     * 帮助文档
     *
     * @param event 消息事件
     */
    @Override
    public MessageChain getHelp(MessageEvent event) {
        return new MessageChainBuilder()
                .append("抛出一个漂流瓶。内容支持文本和表情")
                .build();
    }
}
