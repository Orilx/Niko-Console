package com.orilx.utils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

/**
 * 和网络有关的工具集
 */
public class NetworkUtil {
    private NetworkUtil(){};

    /**
     * 下载文件，如果同名文件已存在，直接返回
     * @param urlConnect 链接地址
     * @param path 文件路径
     * @param name 文件名
     * @return 下载的文件
     */
    public static File downloadFile(String urlConnect, String path, String name){
        String filename = path + File.separator + name;
        File file = new File(filename);
        if(file.exists()){
            return file;
        }
        try {
            URL url =new URL(urlConnect);
            URLConnection connection = url.openConnection();
            InputStream is = connection.getInputStream();
            // 1K的数据缓冲
            byte[] bs =new byte[1024];
            // 读取到的数据长度
            int len;
            FileOutputStream os =new FileOutputStream(file,true);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs,0, len);
            }
            // 完毕，关闭所有链接
            os.close();
            is.close();
        } catch (Exception e){
            e.printStackTrace();
        }

        return file;
    }

    /**
     * 获取JSON文件
     * @param urlConnect 链接地址
     * @return 含JSON的字符串
     */
    public static String getJSON(String urlConnect){
        URL url;
        //请求的输入流
        BufferedReader reader = null;
        StringBuilder res = new StringBuilder();
        try {
            url = new URL(urlConnect);
            reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
            String temp;
            //
            while((temp = reader.readLine()) != null){
                res.append(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(reader!=null){
                try {
                    reader.close(); //关闭流
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return res.toString();
    }
}
