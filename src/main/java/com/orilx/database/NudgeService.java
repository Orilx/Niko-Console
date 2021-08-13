package com.orilx.database;

import com.orilx.utils.DBUtils;
import com.orilx.utils.TimeUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class NudgeService {
    String dbName = "NudgeList";
    Connection connection = null;
    Statement stmt = null;
    ResultSet rs = null;

    /**
     * 初始化戳一戳列表
     */
    public NudgeService(){
        try {
            connection = DBUtils.getConnection(dbName);
            stmt = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Nudge (" +
                    "groupId    INTEGER NOT NULL    PRIMARY KEY ," +
                    "cnt        INTEGER NOT NULL    DEFAULT 0 ," +
                    "isSleep    INTEGER NOT NULL ," +
                    "initTime   INTEGER NOT NULL ," +
                    "sleepTime  INTEGER NOT NULL );";
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置对应群的初始时间
     * @param id QQ群号
     */
    public void setInitTime(Long id){
        String sql = "Update Nudge SET initTime = " + TimeUtil.getTimeMilli() + " WHERE groupId = " + id + ";";
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回对应群的初始化时间
     * @param id QQ群号
     * @return initTime
     */
    public Long getInitTime(Long id){
        Long l = null;
        try {
            rs = stmt.executeQuery("SELECT initTime FROM Nudge WHERE groupId = " + id + ";");
            l = rs.getLong("initTime");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return l;
    }

    /**
     * 获取对应群的戳一戳计数
     * @param id QQ群号
     * @return 戳一戳计数
     */
    public int getCnt(Long id){
        try {
            ResultSet rs = stmt.executeQuery("SELECT cnt FROM Nudge WHERE groupId = " + id + "; ");
            int res = rs.getInt("cnt");
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 戳一戳计数+1
     * @param id QQ群号
     */
    public void cntPlus(Long id){
        try {
            stmt.execute("Update Nudge SET cnt = cnt + 1 WHERE groupId = " + id + ";");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化群组
     * @param id QQ群号
     */
    public void initGroup(Long id){
        String sql = "INSERT INTO Nudge (groupId, cnt, isSleep, initTime, sleepTime) " +
                     "VALUES (" + id + ",0,0,"+ TimeUtil.getTimeMilli() +","+
                             (TimeUtil.getTimeMilli() - 1000 * 24 * 60 * 60) +")";
        try {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查表中是否有该群组
     * @param id QQ群号
     * @return ToF
     */
    public boolean hasGroup(Long id){
        try {
             rs = stmt.executeQuery("SELECT groupId FROM Nudge WHERE groupId = " + id + ";");
            return id == rs.getLong("groupId");
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * 使对应群组机器人休眠
     * @param id QQ群号
     */
    public void sleep(Long id){
        String sql = "Update Nudge SET isSleep = 1 WHERE groupId = " + id + ";" +
                     "Update Nudge SET cnt = 0 WHERE groupId = " + id + ";" +
                     "Update Nudge SET sleepTime = " + TimeUtil.getTimeMilli() +
                     " WHERE groupId = " + id + ";";
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 唤醒对应群组的QQ机器人
     * @param id QQ群号
     * @return 唤醒成功返回false
     */
    public boolean awake(Long id){
        String sql = "Update Nudge SET isSleep = 0 WHERE groupId = " + id + ";" +
                "Update Nudge SET cnt = 0 WHERE groupId = " + id + ";" +
                "Update Nudge SET initTime = " + TimeUtil.getTimeMilli() +
                " WHERE groupId = " + id + ";";
        try {
            stmt.executeUpdate(sql);
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 检查对应群组的机器人是否可以唤醒
     * @param id QQ群号
     * @return ToF
     */
    public Boolean isSleep(Long id){
        try {
            rs = stmt.executeQuery("SELECT sleepTime , isSleep FROM Nudge WHERE groupId = " + id + "; ");
            Long sleepTime = rs.getLong("sleepTime");
            boolean isSleep = rs.getBoolean("isSleep");
            if(isSleep){
                if (TimeUtil.diffTimeMilli(sleepTime) > 60 * 20){
                    return awake(id);
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
