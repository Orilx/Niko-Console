package com.orilx.database;

import com.orilx.utils.DBUtils;
import com.orilx.utils.TimeUtil;
import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * TODO 并入积分系统
 */
public class SignInService {
    String dbName = "SignIn";
    Connection connection = null;
    Statement stmt = null;
    ResultSet rs = null;

    /**
     * 初始化签到列表
     */
    public SignInService(){
        try {
            connection = DBUtils.getConnection(dbName);
            stmt = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS SignIn (" +
                    "id         INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT ," +
                    "qq         INTEGER NOT NULL ," +
                    "groupId    INTEGER NOT NULL ," +
                    "cnt        INTEGER NOT NULL    DEFAULT 0 ," +
                    "lastSTime  INTEGER NOT NULL );";
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 签到,返回当前签到次数
     */
    public int signIn(GroupMessageEvent event){
        long qq = event.getSender().getId();
        long groupId = event.getGroup().getId();

        int cnt = getCnt(qq, groupId);
        if(cnt != -1){
            if(isSigned(qq, groupId)){
                return -1;
            }
            plus(qq, groupId);
            return cnt + 1;
        }
        String sql = "INSERT INTO SignIn (qq, groupId, cnt, lastSTime) " +
                "VALUES ( " + qq + "," + groupId + ", 1, " + TimeUtil.getDate() + ");";
        try {
            stmt.executeUpdate(sql);
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 增加计数
     */
    private void plus(long qq, long groupId){
        String sql = "UPDATE SignIn SET cnt = cnt + 1 , lastSTime = " + TimeUtil.getDate() +
                " WHERE qq = " + qq + " AND groupId = " + groupId +";";
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查是否已经签到过
     */
    private boolean isSigned(long qq, long groupId){
        String sql = "SELECT lastSTime FROM SignIn WHERE qq = " + qq +
                " AND groupId = " + groupId + ";";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            int res = rs.getInt("lastSTime");
            rs.close();
            return Integer.parseInt(TimeUtil.getDate()) == res;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取签到次数，没有记录返回-1
     */
    public int getCnt(long qq, long groupId){
        String sql = "SELECT cnt FROM SignIn WHERE qq = " + qq +
                " AND groupId = " + groupId + ";";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.isClosed()){
                return -1;
            }
            int res = rs.getInt("cnt");
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

}
