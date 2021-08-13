package com.orilx.function;

import com.orilx.database.NudgeService;
import com.orilx.utils.TimeUtil;
import net.mamoe.mirai.event.events.NudgeEvent;

/**
 * 戳一戳事件
 */
public class NudgeFunction extends Function{

    //最大回复次数，大于5
    private final int REPLY_TIMES = 8;
    private String rep = null;
    private Long id = null;
    NudgeService nudgeService;

    public NudgeFunction(){
        nudgeService = new NudgeService();
    }

    private enum replySets{
        NORMAL_REPLY("喵~"),
        SULK_REPLY("别再戳我了~\n再戳我就要生气啦！"),
        ANGRY_REPLY("嗷！"),
        SAD_REPLY("呜呜呜，不理你们了~");

        private final String reply;
        replySets(String reply) {
            this.reply = reply;
        }

        public String getReply(){
            return reply;
        }
    }

    /**
     * 计数器，超过一定次数后调用 sleep() 休眠
     * 超过五分钟重置计数器
     */
    public void counter(Long id) {
        //非生气状态下五分钟后重置计数器
        if(TimeUtil.diffTimeMilli(nudgeService.getInitTime(id)) > 60 * 5 && nudgeService.getCnt(id) < 5){
            nudgeService.setInitTime(id);
            rep = replySets.NORMAL_REPLY.getReply();
        }
        switch (nudgeService.getCnt(id)){
            case 0:
            case 1: rep = replySets.NORMAL_REPLY.getReply();
                break;
            case 4: rep = replySets.SULK_REPLY.getReply();
                break;
            case 5: rep = replySets.ANGRY_REPLY.getReply();
                break;
            case REPLY_TIMES:
                rep = replySets.SAD_REPLY.getReply();
                nudgeService.sleep(id);
            default:
                break;
        }
        nudgeService.cntPlus(id);
    }

    public void function(NudgeEvent nudgeEvent) {
        id = nudgeEvent.getSubject().getId();
        if(!nudgeService.hasGroup(id)){
            nudgeService.initGroup(id);
        }
        if(nudgeService.isSleep(id)){
            return;
        }
        counter(id);
        nudgeEvent.getSubject().sendMessage(rep);
    }

}
