package com.orilx.command.commands;

import com.orilx.command.Command;
import com.orilx.database.DriftBottleService;
import com.orilx.message.MessageManager;
import com.orilx.utils.ProfileUtil;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;

public class BottlePCommand extends Command {

    DriftBottleService dbs;

    public BottlePCommand(){
        dbs = new DriftBottleService();
    }

    /**
     * 无参运行指令
     * @param event 消息事件
     */
    @Override
    public void runCommand(MessageEvent event) {
        MessageChain msg = MiraiCode.deserializeMiraiCode(dbs.getBottle());
        MessageManager.sendMessage(event,msg);
    }

    /**
     * 传参运行指令
     * @param event 消息事件
     * @param param 参数
     */
    @Override
    public void runCommandWithParam(MessageEvent event, String param) {
        if(event.getSender().getId() != ProfileUtil.AdminId){
            MessageManager.sendMessage(event, "这个指令好像不需要参数捏~");
            return;
        }
        if(param.contains("d")){
            String msg = "Complete";
            int id = 0;
            param = param.replace("d","").trim();
            try {
               id = Integer.parseInt(param);
            } catch (Exception e){
                msg = "Error";
            }
            if(!dbs.deleteById(id)){
                msg = "删除失败";
            }
            MessageManager.sendMessage(event,msg);
        }
        else{
            MessageChain msg = MiraiCode.deserializeMiraiCode(dbs.getBottleById(Integer.parseInt(param)));
            MessageManager.sendMessage(event,msg);
        }
    }

    /**
     * 帮助文档
     * @param event 消息事件
     */
    @Override
    public MessageChain getHelp(MessageEvent event) {
        return new MessageChainBuilder()
                .append("从海里随机捞一个漂流瓶")
                .build();
    }
}
