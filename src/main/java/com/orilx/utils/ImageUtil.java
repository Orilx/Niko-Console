package com.orilx.utils;

import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.Image;

import java.io.File;
import java.util.Objects;

public class ImageUtil {
    private ImageUtil(){};

    public static int countFile(){
        int num = 0;
        File img = new File(ProfileUtil.IMAGE_PATH);
        for(File f : Objects.requireNonNull(img.listFiles())){
            if(f.isFile()){
                if(f.getName().endsWith(".png")){
                    num++;
                }
            }
        }
       return num;
    }

    /**
     * 上传单张图片
     * @param imgPath 图片的物理地址
     * @param event 消息事件
     * @return Image消息
     */
    public static Image uploadImage(String imgPath, MessageEvent event){
        File file = new File(imgPath);
        return Contact.uploadImage(event.getSubject(), file);
    }

    /**
     * 储存图片信息的工具类
     */
    public static class ImageInfo{
        String url;
        String info;
        String name;

        public ImageInfo(String url, String info, String name) {
            this.url = url;
            this.info = info;
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public String getInfo() {
            return info;
        }

        public String getName() {
            return name;
        }
    }

}
