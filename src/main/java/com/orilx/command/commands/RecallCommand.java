package com.orilx.command.commands;

import com.orilx.command.Command;
import com.orilx.message.MessageManager;
import com.orilx.utils.ProfileUtil;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.event.events.MessagePostSendEvent;
import net.mamoe.mirai.message.MessageReceipt;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.util.TreeMap;

public class RecallCommand extends Command {
    private static final TreeMap<Long,MessageReceipt> groupMap = new TreeMap<>();

    public static void buffer(MessagePostSendEvent event){
        Long id = event.getTarget().getId();
        if(!groupMap.containsKey(id)){
            groupMap.put(id,event.getReceipt());
        }
        else{
            groupMap.replace(id, event.getReceipt());
        }
    }

    /**
     * 无参运行指令
     *
     * @param event 消息事件
     */
    @Override
    public void runCommand(MessageEvent event) {
        if(!(event.getSender().getId() == ProfileUtil.AdminId)){
            return;
        }
        Long id = event.getSubject().getId();
        try {
            groupMap.get(id).recall();
        } catch (NullPointerException e1){
            MessageManager.sendMessage(event,"ERROR");
        } catch (IllegalStateException e2) {
            MessageManager.sendMessage(event,"消息已经被撤回了~");
        }
    }

    /**
     * 传参运行指令
     * @param event 消息事件
     * @param param 参数
     */
    @Override
    public void runCommandWithParam(MessageEvent event, String param) {
        if(!(event.getSender().getId() == ProfileUtil.AdminId)){
            return;
        }
        Long id = Long.valueOf(param);
        try {
            groupMap.get(id).recall();
        } catch (NullPointerException e1){
            MessageManager.sendMessage(event,"ERROR");
        } catch (IllegalStateException e2) {
            MessageManager.sendMessage(event,"消息已经被撤回了~");
        }
    }

    /**
     * 帮助文档
     *
     * @param event 消息事件
     */
    @Override
    public MessageChain getHelp(MessageEvent event) {
        return new MessageChainBuilder()
                .append("撤回一条消息(*仅限Niko的管理员使用*)")
                .build();
    }
}
