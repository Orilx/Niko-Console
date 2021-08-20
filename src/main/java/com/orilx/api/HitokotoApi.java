package com.orilx.api;

import com.alibaba.fastjson.JSONObject;
import com.orilx.utils.NetworkUtil;
import com.orilx.utils.ProfileUtil;

public class HitokotoApi {

    private static final String Hitokoto_Url = ProfileUtil.HITOKOTO_URL;
    private static String params = ProfileUtil.HITOKOTO_DEFAULT_PARAMS;

    public static void setParams(String params){
        HitokotoApi.params = "?c=" + params;
    }

    private static String getUrl(){
        return Hitokoto_Url + params;
    }


    public static String getJSXH(){
        String JSON = NetworkUtil.getJSON("https://cdn.ipayy.net/says/api.php?type=shehui&encode=json");
        JSONObject jo = JSONObject.parseObject(JSON);
        if(JSON.isEmpty()){
            return "出错了！不是Niko的问题！";
        }
        return jo.getString("say");
    }

    public static String getHitokoto(){
        String JSON = NetworkUtil.getJSON(getUrl());

//        if(JSON == null){
//            return "出错了，等会再试试吧~";
//        }

        JSONObject jo = JSONObject.parseObject(JSON);
        String hitokoto = jo.getString("hitokoto");
        String from = jo.getString("from");
        return "『" + hitokoto + "』" + "\n—— " + from;
    }
}
