package com.shiyatsu.lib.db.handler.mysql;

import com.shiyatsu.lib.db.handler.DbConnector;
import com.shiyatsu.lib.db.handler.DbHandler;
import com.shiyatsu.lib.db.handler.exception.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MySQLHandler extends DbHandler {

    @Override
    public Connection connect(DbConnector con) throws DatabaseException {
        String url = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            url = "jdbc:mysql://" + con.getHost() + ":" + con.getPort() + "/" + con.getDb();
            Properties prop = new Properties();
            prop.put("user", con.getUser());
            prop.put("password", con.getPassword());
            prop.put("connectTimeout", 30000);
            prop.putAll(con.getProperties());
            return DriverManager.getConnection(url, prop);
        } catch (ClassNotFoundException e) {
            throw new DatabaseException("Fail to load database driver: com.mysql.cj.jdbc.Driver", e);
        } catch (SQLException e) {
            throw new DatabaseException(String.format("Fail to connect to MYSQL Database [ %s ] ", url), e);
        }
    }

    protected String escape(String s) {
        return s.replace("\\", "\\\\").replace("'", "\\'");
    }
}
