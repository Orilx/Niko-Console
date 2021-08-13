package com.orilx.command.commands;

import com.orilx.command.Command;
import com.orilx.message.MessageManager;
import com.orilx.utils.ImageUtil;
import com.orilx.utils.ProfileUtil;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;

/**
 * ping和无聊的小工具
 */
public class FunCommand extends Command {
    private static String fileName = ProfileUtil.FUN_IMAGE;

    @Override
    public void runCommand(MessageEvent event) {
        MessageChain img = new MessageChainBuilder()
                .append(ImageUtil.uploadImage(fileName, event))
                .build();
        MessageManager.sendMessage(event,img);
        MessageManager.sendMessage(event,"科创部入股不亏!");
    }

    @Override
    public void runCommandWithParam(MessageEvent event, String param) {

    }

    @Override
    public MessageChain getHelp(MessageEvent event) {
        return new MessageChainBuilder()
                .append("pong!")
                .build();
    }
}
