package com.orilx.command;

public enum CommandSets {
    COMMAND_NULL("null"),
    COMMAND_HELP("/帮助"),
    COMMAND_SETU("/来点色图"),
    COMMAND_YIYAN("/一言"),
    COMMAND_MUSIC("/点歌"),
//    COMMAND_TRANSLATE("/翻译"),
    COMMAND_FOR_FUN("/营业"),
    COMMAND_BING("/bing"),
    COMMAND_RECALL("/撤回"),
    COMMAND_TBOTTLE("/扔瓶子"),
    COMMAND_PBOTTLE("/捞瓶子"),
    COMMAND_SIGNIN("/签到")
    ;

    private final String command;

    CommandSets(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    /**
     * 遍历以匹配命令，无匹配则返回空命令
     * @param id 命令前缀
     * @return type.
     */
    public static CommandSets getType(String id){
        for(CommandSets type : CommandSets.values()){
            if(type.command.equals(id)){
                return type;
            }
        }
        return COMMAND_NULL;
    }
}
