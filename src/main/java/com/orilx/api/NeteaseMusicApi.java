package com.orilx.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class NeteaseMusicApi {

    public static String getSong(String musicName){
        String JSON = getSongList(musicName);

        if(JSON == null){
            return "null";
        }

        JsonArray ja =  JsonParser.parseString(JSON).getAsJsonObject()
                .get("result").getAsJsonObject().get("songs").getAsJsonArray();

        if(validSongList(ja)){
            return "null";
        }

        JsonObject song = ja.get(0).getAsJsonObject();
        String id = song.get("id").getAsString();
        String copyrightId = song.get("copyrightId").getAsString();
        //检测是否需要会员
//        if(!copyrightId.equals("0")){
//            return "这首歌貌似需要VIP呢，Niko没有办法分享过来了~";
//        }
        if(!canAccess(id)){
            return "这首歌貌似需要VIP呢，Niko没有办法分享过来了~";
        }

        String name = song.get("name").getAsString();
        String url = "https://music.163.com/#/song?id=" + id;
        String artist = song.get("artists").getAsJsonArray().get(0).getAsJsonObject().get("name").getAsString();
        String picUrl = song.get("album").getAsJsonObject().get("picUrl").getAsString();
        String musicUrl = "https://music.163.com/song/media/outer/url?id=" + id;


        return name + "_!" + artist + "_!" + url + "_!" + picUrl + "_!" + musicUrl;
    }

    /**
     * 获取歌曲列表JSON
     * @param name 歌名
     * @return 含歌曲列表的JSON字符串
     */
    private static String getSongList(String name){
        String JSON = null;
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder().add("s", name)
                .add("type", "1").add("offset", "0").add("total", "true")
                .add("limit", "1").build();

        Request request = new Request.Builder()
                .url("https://music.163.com/api/search/pc")
                .post(body)
                .addHeader(
                        "User-Agent",
                        "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36")
                .build();
        Response response = null;

        try {
            response = client.newCall(request).execute();
            JSON = response.body().string();

        } catch (IOException e) {
           e.printStackTrace();
        }
        /* 关闭Response的body **/
        response.body().close();

        return JSON;
    }

    private static boolean validSongList(JsonArray ja){
        return ja != null && ja.get(0).getAsJsonObject().get("songs") != null;
    }

    private static boolean canAccess(String id){
        String s = "http://music.163.com/song/media/outer/url?id=" + id + ".mp3";

        try {
            URL url = new URL(s);
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36");
            huc.setRequestMethod("HEAD");
            huc.connect();
            return huc.getResponseCode() == 200;
        } catch (Exception ex) {
            return false;
        }
    }

}
