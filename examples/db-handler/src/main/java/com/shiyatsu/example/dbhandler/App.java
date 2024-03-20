package com.shiyatsu.example.dbhandler;

import com.shiyatsu.lib.db.handler.DbConnector;
import com.shiyatsu.lib.db.handler.exception.DatabaseException;
import com.shiyatsu.lib.db.handler.mysql.MySQLHandler;
import com.shiyatsu.logger.ILoggerService;
import com.shiyatsu.logger.impl.LoggerService;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class App {
    private static final String DB = "";
    private static final String HOST = "";
    private static final Integer PORT = 3306;
    private static final String USER = "";
    private static final String PASSWORD = "";
    private static final String QUERY = "SELECT * from customers;";
    private static final ILoggerService logger = LoggerService.getLoggingService();

    public static void main(String[] args) {
        DbConnector dbConnector = new DbConnector();
        dbConnector.setDb(DB);
        dbConnector.setHost(HOST);
        dbConnector.setPort(PORT);
        dbConnector.setUser(USER);
        dbConnector.setPassword(PASSWORD);
        MySQLHandler dbHandler = new MySQLHandler();
        try (Connection con = dbHandler.getConnection(dbConnector); Statement st = con.createStatement()) {
            long start = System.currentTimeMillis();
            st.executeQuery(QUERY);
            ResultSet rs = st.getResultSet();
            long delta = System.currentTimeMillis() - start;
            logger.info(App.class, String.format("MYSQL Execute %s in %d ms ", QUERY, delta));
            while (rs.next()) {
                logger.info(App.class, "Customer found " + rs.getString(1));
            }
            rs.close();
        } catch (DatabaseException e) {
            logger.error(App.class, "Failed to connect to database", e);
        } catch (SQLException e) {
            logger.error(App.class, "Fail to execute query", e);
        }
    }

}
