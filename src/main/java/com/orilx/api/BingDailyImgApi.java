package com.orilx.api;

import com.alibaba.fastjson.JSONObject;
import com.orilx.utils.ImageUtil;
import com.orilx.utils.NetworkUtil;

public class BingDailyImgApi {
    //Bing每日一图接口
    private static final String BING_IMG = "https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1";

    public static ImageUtil.ImageInfo getImg(){
        JSONObject jo = JSONObject.parseObject(getJSON());
        String name = jo.getString("startdate");
        String imgUrl = "https://cn.bing.com" + jo.getString("url");
        String info = jo.getString("copyright");
        return new ImageUtil.ImageInfo(imgUrl,info,name);
    }

    private static String getJSON(){
        //获取原始JSON
        String JSON = NetworkUtil.getJSON(BING_IMG);
        JSONObject jo = JSONObject.parseObject(JSON);
        String res = jo.getString("images");
        //去掉两边的"[]"
        return res.substring(1,res.length()-1);
    }
}
