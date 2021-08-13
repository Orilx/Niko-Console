package com.orilx.command.commands;

import com.orilx.command.Command;
import com.orilx.message.MessageManager;
import com.orilx.utils.ImageUtil;
import com.orilx.utils.ProfileUtil;
import com.orilx.utils.RollUtil;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.io.File;

/**
 * “来点色图”
 * 待完善
 */

public class SetuCommand extends Command {

    /**
     * 随机回复一张“色图”
     * @param event 消息事件
     */
    @Override
    public void runCommand(MessageEvent event) {
        //随机抽取一张图片
        String fileName = RollUtil.roll(ImageUtil.countFile() - 1) + ".png";
        sendImage(event,fileName);
    }

    /**
     * 回复指定的”色图“
     * @param event 消息事件
     * @param param 图片序号
     */
    @Override
    public void runCommandWithParam(MessageEvent event, String param) {
        String fileName = Integer.valueOf(param) + ".png";
        if(Integer.parseInt(param) > ImageUtil.countFile()){
            MessageManager.sendMessage(event, new MessageChainBuilder().append("没有找到这张图片~").build());
            return;
        }
        sendImage(event,fileName);
    }

    /**
     * 帮助文档
     * @param event 消息事件
     * @return 帮助文档内容
     */
    @Override
    public MessageChain getHelp(MessageEvent event) {
        return new MessageChainBuilder()
                .append("随机回复一张“色图”")
                .build();
    }

    /**
     * 发送图片
     * @param event 消息事件
     * @param fileName 图片序号
     */
    private void sendImage(MessageEvent event,String fileName){
        MessageChain msg = new MessageChainBuilder()
                .append(ImageUtil.uploadImage(ProfileUtil.IMAGE_PATH + File.separator + fileName, event))
                .build();
        MessageManager.sendMessage(event, msg);
        MessageManager.sendMessage(event, new MessageChainBuilder()
                .append(new At(event.getSender().getId()))
                .append(" 您点的一份色图\n喵~")
                .build());
    }
}
