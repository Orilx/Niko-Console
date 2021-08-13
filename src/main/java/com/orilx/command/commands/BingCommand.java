package com.orilx.command.commands;

import com.orilx.api.BingDailyImgApi;
import com.orilx.command.Command;
import com.orilx.message.MessageManager;
import com.orilx.utils.ImageUtil;
import com.orilx.utils.NetworkUtil;
import com.orilx.utils.ProfileUtil;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.io.File;

/**
 * 获取Bing每日一图
 * TODO 保存info
 */
public class BingCommand extends Command {
    String imgName;
    String imgInfo;
    String imgPath = ProfileUtil.BING_DAILY_PIC_PATH;
    /**
     * 无参运行指令
     * @param event 消息事件
     */
    @Override
    public void runCommand(MessageEvent event) {
        File img;
        ImageUtil.ImageInfo image = BingDailyImgApi.getImg();
        imgName = image.getName();
        imgInfo = image.getInfo();
        //去掉后面的版权信息
        String info = imgInfo.replaceAll("\\(([^\\)]*)\\)","");
        //下载图片
        img = NetworkUtil.downloadFile(image.getUrl(), imgPath ,imgName + ".jpg");
        //上传到服务器
        Image imgMessage = ImageUtil.uploadImage(img.getPath(),event);

        MessageChain msg = new MessageChainBuilder()
                .append("今日图片：\n")
                .append(imgMessage)
                .append("—— ").append(info)
                .build();
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
                .append("获取必应每日一图")
                .build();
    }
}
