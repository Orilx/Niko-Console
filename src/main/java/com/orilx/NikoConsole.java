package com.orilx;

import com.orilx.function.NudgeFunction;
import com.orilx.listener.GroupListener;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.*;

public final class NikoConsole extends JavaPlugin {
    public static final NikoConsole INSTANCE = new NikoConsole();
    public static GroupListener groupListener = null;
    public static NudgeFunction nudgeFunction = null;

    private NikoConsole() {
        super(new JvmPluginDescriptionBuilder("com.orilx.Niko-Console", "1.0-SNAPSHOT")
                .name("Niko")
                .author("Dr.Ink")
                .build());
    }

    @Override
    public void onEnable() {
        getLogger().info("Niko启动成功！");

        /*上线成功发送消息*/
        GlobalEventChannel.INSTANCE.subscribeOnce(BotOnlineEvent.class, event ->{
            InitBot();
        });

        /*群消息监听*/
        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, event -> {
            groupListener.receiveMessage(event);
        });

        /*新人入群事件监听*/
        GlobalEventChannel.INSTANCE.subscribeAlways(MemberJoinEvent.Active.class, event -> {
            groupListener.MemberJoinGroupActive(event);
        } );

        GlobalEventChannel.INSTANCE.subscribeAlways(MemberJoinEvent.Invite.class, event -> {
            groupListener.MemberJoinGroupInvite(event);
        });

        /*成员退群事件监听*/
        GlobalEventChannel.INSTANCE.subscribeAlways(MemberLeaveEvent.Quit.class, event -> {
            groupListener.MemberLeaveGroup(event);
        });

        GlobalEventChannel.INSTANCE.subscribeAlways(MemberLeaveEvent.Kick.class,event ->{
            groupListener.MemberLeaveGroupKick(event);
        });

        /*戳一戳事件监听*/
        GlobalEventChannel.INSTANCE.subscribeAlways(NudgeEvent.class, NudgeEvent -> {
            if(NudgeEvent.getTarget().equals(Bot.getInstances().get(0))){
                nudgeFunction.function(NudgeEvent);
            }
        });

        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessagePostSendEvent.class, event ->{
            groupListener.sendEventListener(event);
        });
    }

    public void InitBot(){
//        Bot.getInstances().get(0).getFriend(AdminQQ).sendMessage("上线成功！\n喵~");
        groupListener = new GroupListener();
        nudgeFunction = new NudgeFunction();
    }
}