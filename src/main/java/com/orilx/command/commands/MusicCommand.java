package com.orilx.command.commands;

import com.orilx.api.NeteaseMusicApi;
import com.orilx.command.Command;
import com.orilx.message.MessageManager;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.*;

/**
 * 点歌指令
 */
public class MusicCommand extends Command {

    @Override
    public void runCommand(MessageEvent event) {
        MessageManager.sendMessage(event,"没有参数，Niko什么也没找到~");
    }

    @Override
    public void runCommandWithParam(MessageEvent event, String param) {
        String t = NeteaseMusicApi.getSong(param);
        String msg;
        String[] tem = t.split("_!");
        if(tem.length == 1){
            msg = t;
        }
        else {
            msg = MessageUtils.newChain(new MusicShare(MusicKind.NeteaseCloudMusic,tem[0],
                    tem[1],tem[2],tem[3],tem[4],"[@" + event.getSenderName() + "]" + " 点歌：" + tem[0]
            )).serializeToMiraiCode();
        }
        MessageManager.sendMessage(event,MiraiCode.deserializeMiraiCode(msg));
//        MessageManager.sendMessage(event,NeteaseMusicApi.getSongList(param));
    }

    @Override
    public MessageChain getHelp(MessageEvent event) {
        return new MessageChainBuilder()
                .append("* 在网易云曲库中搜索\n")
                .append("* 附加歌名作为参数，返回搜索列表中的第一首歌\n")
                .append("(咱也不知道是哪个搜索列表~)")
                .build();
    }
}
