package com.orilx.database;

import com.orilx.utils.DBUtils;
import com.orilx.utils.RollUtil;
import com.orilx.utils.TimeUtil;
import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.sql.*;

public class DriftBottleService {
    Connection connection = null;
    Statement stmt = null;

    public DriftBottleService(){
        try {
            connection = DBUtils.getConnection("driftBottle");
            stmt = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS DriftBottle (" +
                    "id         INTEGER     NOT NULL PRIMARY KEY AUTOINCREMENT ," +
                    "groupId    INTEGER     NOT NULL ," +
                    "groupName  TEXT        NOT NULL ," +
                    "author     TEXT        NOT NULL ," +
                    "msg        TEXT        NOT NULL ," +
                    "addTime    TEXT        NOT NULL );";
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getBottleById(int id){
        String res = null;

        String sql = "SELECT * FROM DriftBottle WHERE  id = " + id + ";";

        try {
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.isClosed()){
                return "海里已经没有瓶子啦~";
            }
            res = "来自群聊[" + rs.getString("groupName") + "]的" + rs.getString("author")
                    +"的漂流瓶:\n\n『" + rs.getString("msg") + "』\n\n写于"
                    + rs.getString("addTime");
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    /**
     * 在数据库里随机抽取一条信息返回并删除
     * @return 随机抽取的信息
     */
    public String getBottle(){
        String[] m= getMaxMinId().split(" ");
        String res = null;
        int r;
        while(true){
            try {
                r = RollUtil.roll(Integer.parseInt(m[1]),Integer.parseInt(m[0]));
            } catch (Exception e){
                return "海里已经没有瓶子啦~";
            }
            String sql = "SELECT * FROM DriftBottle WHERE  id = " + r + ";";

            try {
                ResultSet rs = stmt.executeQuery(sql);
                if(!rs.next()){
                    continue;
                }
                if(rs.isClosed()){
                    return "海里已经没有瓶子啦~";
                }
                res = "来自群聊[" + rs.getString("groupName") + "]的" + rs.getString("author")
                        +"的漂流瓶:\n\n『" + rs.getString("msg") + "』\n\n写于"
                        + rs.getString("addTime");
                rs.close();
                stmt.executeUpdate("DELETE FROM DriftBottle WHERE id = " + r + ";");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return res;
        }
    }

    /**
     * 获取表中最大和最小的id
     * @return 格式: Max Min
     */
    private String getMaxMinId(){
        String sql = "SELECT max(id), min(id) FROM DriftBottle;";
        String res = null;
        try {
            ResultSet rs = stmt.executeQuery(sql);
            res = rs.getString(1)+ " " +rs.getString(2);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public boolean deleteById(int id){
        try {
            stmt.executeUpdate("DELETE FROM DriftBottle WHERE id = " + id + ";");
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public int getMaxId(){
        String sql = "SELECT max(id)FROM DriftBottle;";
        int res = 0;
        try {
            ResultSet rs = stmt.executeQuery(sql);
            res = rs.getInt(1);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 扔瓶子
     * @param event 群消息事件 用于获取群名、群号和昵称
     * @param msg 要存进数据库的信息
     */
    public void add(GroupMessageEvent event, String msg){
        String sql = "INSERT INTO DriftBottle (groupId, groupName, author, msg, addTime) " +
                "VALUES (?,?,?,?,?)";
        try {
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setObject(1,event.getGroup().getId());
            pstm.setObject(2,event.getGroup().getName());
            pstm.setObject(3,event.getSenderName());
            pstm.setObject(4,msg);
            pstm.setObject(5, TimeUtil.getTime());
            pstm.execute();
            pstm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
