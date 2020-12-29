package edu.northeastern.base.dao;

import edu.northeastern.base.condition.DatabaseCondition;

import java.sql.*;

/**
 * Created by Jim Z on 12/25/20 15:29
 */
public class DatabaseRetrieve {
    public static ResultSet executeQuery(String url, String query) {
        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery(query);
            return rs;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }
}
