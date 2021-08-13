package com.orilx.command.commands;

import com.orilx.command.Command;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;

public class NullCommand extends Command {

    @Override
    public void runCommand(MessageEvent event) {

    }

    @Override
    public void runCommandWithParam(MessageEvent event, String param) {

    }

    @Override
    public MessageChain getHelp(MessageEvent event) {

        return null;
    }


}
