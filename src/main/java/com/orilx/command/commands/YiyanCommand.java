package com.orilx.command.commands;

import com.orilx.api.HitokotoApi;
import com.orilx.command.Command;
import com.orilx.message.MessageManager;
import com.orilx.utils.ProfileUtil;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.util.TreeMap;

public class YiyanCommand extends Command {

    TreeMap<String,String> paramList = new TreeMap<>();
    String msg;

    public YiyanCommand(){
        paramList.put("a","a");
        paramList.put("b","b");
        paramList.put("c","c");
        paramList.put("d","d");
        paramList.put("e","i");
        paramList.put("wyy","j");
        paramList.put(ProfileUtil.HITOKOTO_DEFAULT_PARAMS,ProfileUtil.HITOKOTO_DEFAULT_PARAMS);
    }

    /**
     * 无参数获取一言，从a、b、c、d、i分类中选取
     * @param event 消息
     */
    @Override
    public void runCommand(MessageEvent event) {
//        if(event.getMessage().contentToString().contains("/精神小伙")){
//            String msg =HitokotoApi.getJSXH();
//            MessageManager.sendMessage(event,msg);
//            return;
//        }
        runCommandWithParam(event, ProfileUtil.HITOKOTO_DEFAULT_PARAMS);
    }

    /**
     * 传参获取一言
     * @param event 消息事件
     * @param param 参数
     */
    @Override
    public void runCommandWithParam(MessageEvent event, String param) {
        if(!paramList.containsKey(param)){
            msg = "参数有误~";
            MessageManager.sendMessage(event,msg);
            return;
        }
        HitokotoApi.setParams(paramList.get(param));
        msg = HitokotoApi.getHitokoto();
        MessageManager.sendMessage(event,msg);
    }

    /**
     * 帮助文档
     * @param event 消息事件
     * @return 帮助文档内容
     */
    @Override
    public MessageChain getHelp(MessageEvent event) {
        return new MessageChainBuilder()
                .append("发送“/一言”随机获取一句一言\n")
                .append("附加参数可以指定一言类型\n")
                .append("支持的参数有:\n")
                .append("a 动画 , b 漫画\nc 游戏 , d 文学\ne 诗词")
                .build();
    }

}
