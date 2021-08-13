package com.orilx.command.commands;

import com.orilx.command.Command;
import com.orilx.command.CommandSets;
import com.orilx.listener.GroupListener;
import com.orilx.message.MessageManager;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;

/**
 * 帮助文档
 */
public class HelpCommand extends Command {

    @Override
    public void runCommand(MessageEvent event) {
        MessageChain msg = new MessageChainBuilder()
                .append("支持的功能：\n" +
                        "* 拍一拍自动回复\n" +
                        "* 复读姬\n" +
                        "* /一言\n" +
                        "* /点歌\n" +
                        "* /来点色图\n" +
                        "* /bing\n" +
                        "* 漂流瓶(/扔瓶子&/捞瓶子)\n" +
                        "* 精神小伙 \n")
//                        "* /ping \n")
                .append("添加指令作为参数以获得更多帮助")
                .build();
        MessageManager.sendMessage(event, msg);
    }

    /**
     * 有参执行指令
     * @param event 消息事件
     * @param param 参数
     */
    @Override
    public void runCommandWithParam(MessageEvent event, String param) {
        CommandSets command = CommandSets.getType("/" + param);
        if(command.equals(CommandSets.COMMAND_NULL)){
            MessageManager.sendMessage(event,"参数有误~");
            return;
        }
        MessageChain msg = GroupListener.commandSets.get(command).getHelp(event);
        MessageManager.sendMessage(event,msg);
    }

    @Override
    public MessageChain getHelp(MessageEvent event) {
        return new MessageChainBuilder().append("您输入的参数有误~").build();
    }


}
