package com.orilx.utils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBUtils {
    private DBUtils(){}

    private static Connection c = null;
    private static Statement stmt = null;

    /**
     * 初始化
     */
    private static void init(){
        File file= new File("data/databases");
        if(!file.exists()){
            file.mkdir();
        }
    }

    public static Connection getConnection(String dbName) throws Exception{
        init();
        String database = "jdbc:sqlite:data/databases/" + dbName + ".db";
        //加载驱动
        Class.forName("org.sqlite.JDBC");
        //连接数据库
        c = DriverManager.getConnection(database);
        //创建连接对象
        return c;
    }
}
