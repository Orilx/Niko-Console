package com.orilx.database;

import com.orilx.utils.DBUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class KickService {
    Connection connection = null;
    Statement stmt = null;

    public KickService(){
        try {
            connection = DBUtils.getConnection("KickList");
            stmt = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS KickList (" +
                    "id         INTEGER     NOT NULL PRIMARY KEY AUTOINCREMENT ," +
                    "QQ         INTEGER     NOT NULL ," +
                    "groupId    INTEGER     NOT NULL ," +
                    "cnt        INTEGER     NOT NULL );";
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getCnt(long qq, long groupId){
        String sql = "SELECT cnt FROM KickList WHERE QQ = " + qq +
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

    public void add(long qq,long groupId){
        if(isExist(qq, groupId)){
            plus(qq, groupId);
            return;
        }
        String sql = "INSERT INTO KickList (QQ, groupId, cnt) " +
                     "VALUES ("+ qq + "," + groupId  + "," + 1 +")";
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void plus(long qq, long groupId){
        String sql = "UPDATE KickList SET cnt = cnt + 1 WHERE QQ = " + qq +
                     " AND groupId = " + groupId +";";
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isExist(long qq, long groupId){
        String sql = "SELECT * FROM KickList WHERE QQ = " + qq +
                " AND groupId = " + groupId +";";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.isClosed()){
                return false;
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

}
