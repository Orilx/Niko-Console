package com.orilx.function;

import com.orilx.command.CommandSets;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.code.MiraiCode;

import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 复读姬
 */
public class RepeatFunction extends Function{

    private long fromGroup;
    private String message;
    //计数器
    private int cnt = 0;
    private final int REPEAT_TIME = 2;
    //存放不同群的消息记录
    TreeMap<Long,String> repeatCollection = new TreeMap<>();
    //存放已经回复过的消息，防止多次复读
    TreeMap<Long,String> repeatAlreadyCollection = new TreeMap<>();
    //过滤规则
    private final Pattern miraiCode_1 = Pattern.compile("^\\[mirai:((?!face).)*$");
    private final Pattern miraiCode_2 = Pattern.compile("^\\[mirai:((?!image).)*$");
    private final Pattern supposedMessage = Pattern.compile("(请升级到最新版本)|(使用最新版手机QQ)");

    /**
     * 检测是否可以复读
     * @param cnt 消息记录次数
     * @return ToF
     */
    public boolean canReply(int cnt){
        if(cnt == REPEAT_TIME){
            if(!repeatAlreadyCollection.containsKey(fromGroup)){
                return true;
            }
            return !repeatAlreadyCollection.get(fromGroup).equals(message);
        }
        return false;
    }

    /**
     * 检查消息是否符合要求
     * @param msg 群消息转换成mirai码
     */
    public boolean isText(String msg){
        //检测是否是指令，防止连续调用指令时复读
        CommandSets command = CommandSets.getType(msg.split(" ")[0]);
        if(!command.equals(CommandSets.COMMAND_NULL)){
            return false;
        }
        Matcher matcher = supposedMessage.matcher(msg);
        if(matcher.find()){
            return false;
        }
        //检测是否含有mirai码
        Matcher matcher_1 = miraiCode_1.matcher(msg);
        Matcher matcher_2 = miraiCode_2.matcher(msg);
        return !matcher_1.find() || !matcher_2.find();
    }

    public void function(GroupMessageEvent event){
        fromGroup = event.getGroup().getId();
        //过滤除纯文本外的所有类型消息
        if(!isText(event.getMessage().serializeToMiraiCode())){
            cnt = 0;
            return;
        }
        message = event.getMessage().serializeToMiraiCode();
        //如果此前群中没有发言，则将该群加入Map
        if(!repeatCollection.containsKey(fromGroup)){
            repeatCollection.put(fromGroup, message);
            cnt++;
            return;
        }
        //检查每条消息，如果和前一条消息一样，计数器+1，否则更新信息,计数器归零
        if(repeatCollection.get(fromGroup).equals(message)){
            cnt ++;
        }
        else{
            repeatCollection.replace(fromGroup, message);
            cnt = 1;
        }
        if(canReply(cnt)){
            repeatAlreadyCollection.put(fromGroup, message);
            event.getGroup().sendMessage(MiraiCode.deserializeMiraiCode(message));
            cnt = 0;
        }
    }
}
