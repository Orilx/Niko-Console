package com.orilx.listener;

import com.orilx.command.Command;
import com.orilx.command.CommandSets;
import com.orilx.command.commands.*;
import com.orilx.database.KickService;
import com.orilx.function.RepeatFunction;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.GroupMessagePostSendEvent;
import net.mamoe.mirai.event.events.MemberJoinEvent;
import net.mamoe.mirai.event.events.MemberLeaveEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.util.TreeMap;

public class GroupListener{

    String msg = null;
    String head = null;
    CommandSets command = null;
    KickService kickService;
    //指令集合
    public static TreeMap<CommandSets, Command> commandSets = new TreeMap<>();
    //复读姬
    RepeatFunction repeatFunction = new RepeatFunction();

    /**
     * 激活命令模块
     */
    public GroupListener(){
        commandSets.put(CommandSets.COMMAND_HELP,new HelpCommand());
        commandSets.put(CommandSets.COMMAND_FOR_FUN,new FunCommand());
        commandSets.put(CommandSets.COMMAND_SETU,new SetuCommand());
        commandSets.put(CommandSets.COMMAND_YIYAN,new YiyanCommand());
        commandSets.put(CommandSets.COMMAND_BING,new BingCommand());
        commandSets.put(CommandSets.COMMAND_MUSIC,new MusicCommand());
        commandSets.put(CommandSets.COMMAND_TBOTTLE,new BottleTCommand());
        commandSets.put(CommandSets.COMMAND_PBOTTLE,new BottlePCommand());
        commandSets.put(CommandSets.COMMAND_RECALL,new RecallCommand());
        commandSets.put(CommandSets.COMMAND_SIGNIN,new SignInCommand());

        kickService = new KickService();
    }
//    public void receiveMessage(GroupMessageEvent event, MessageChain message) {
//        event.getGroup().sendMessage(message.toString());
//    }

    /**
     * 接收并处理消息
     * @param event 群消息事件
     */
    public void receiveMessage(GroupMessageEvent event) {
        //消息传入复读姬
        repeatFunction.function(event);
        //进入正常命令执行阶段
        msg = event.getMessage().contentToString().trim();
        head = msg.split(" ")[0];
        command = CommandSets.getType(head);

        if(command.equals(CommandSets.COMMAND_NULL)){
            return;
        }

        if(msg.split(" ").length == 1){
            commandSets.get(command).runCommand(event);
            return;
        }

        msg = msg.replace(head,"").trim();
        commandSets.get(command).runCommandWithParam(event, msg);
    }

    /**
     * 新人入群欢迎
     * @param event 新人入群事件
     */
    public void MemberJoinGroupActive(MemberJoinEvent.Active event) {
        long id = event.getMember().getId();
        At at = new At(id);
        MessageChain msg = new MessageChainBuilder()
                .append(at)
                .append("\n欢迎大佬入群！群地位-1")
                .build();
        if(kickService.isExist(id, event.getGroupId())){
            msg = new MessageChainBuilder()
                    .append(at)
                    .append("\n被送过飞机票就不要再回来了喵~")
                    .append("\n(cnt = " + kickService.getCnt(id, event.getGroupId()) + ")")
                    .build();
        }
        event.getGroup().sendMessage(msg);
    }

    public void MemberJoinGroupInvite(MemberJoinEvent.Invite event) {
        long id = event.getMember().getId();
        At at = new At(id);
        MessageChain msg = new MessageChainBuilder()
                .append(at)
                .append("\n欢迎大佬入群！群地位-1")
                .build();
        if(kickService.isExist(id, event.getGroupId())){
            msg = new MessageChainBuilder()
                    .append(at)
                    .append("\n被送过飞机票就不要再回来了喵~")
                    .append("\n(cnt = " + kickService.getCnt(id, event.getGroupId()) + ")")
                    .build();
        }
        event.getGroup().sendMessage(msg);
        event.getGroup().sendMessage(
                new MessageChainBuilder()
                .append("由")
                .append(new At(event.getInvitor().getId()))
                .append("邀请入群")
                .build());
    }

    /**
     * 成员退群监听
     */
    public void MemberLeaveGroup(MemberLeaveEvent.Quit event){
        String nickName = event.getMember().getNick();
        long id = event.getMember().getId();
        MessageChain msg = new MessageChainBuilder().append(nickName).append("(").append(String.valueOf(id)).append(")")
                .append("\n离开了我们")
                .build();
        event.getGroup().sendMessage(msg);
    }

    public void MemberLeaveGroupKick(MemberLeaveEvent.Kick event) {
        String nickName = event.getMember().getNick();
        long id = event.getMember().getId();
        kickService.add(id, event.getGroupId());
        MessageChain msg = new MessageChainBuilder().append(nickName).append("(").append(String.valueOf(id)).append(")")
                .append("\n获赠飞机票一张~")
                .build();
        event.getGroup().sendMessage(msg);
    }

    /**
     * 记录发送过的消息
     */
    public void sendEventListener(GroupMessagePostSendEvent event){
        RecallCommand.buffer(event);
    }
}
