package com.shiyatsu.lib.db.handler;

import com.shiyatsu.lib.db.handler.exception.DatabaseException;
import com.shiyatsu.logger.ILoggerService;
import com.shiyatsu.logger.impl.LoggerService;

import java.sql.*;

public abstract class DbHandler implements IDbHandler {

    protected static final ILoggerService logger = LoggerService.getLoggingService();

    public Connection getConnection(DbConnector con) throws DatabaseException {
        return connect(con);
    }

    public ResultSet execute(Connection con, String query) throws SQLException {
        long start = System.currentTimeMillis();
        Statement st = con.createStatement();
        st.executeQuery(query);
        ResultSet rs = st.getResultSet();
        st.close();
        long delta = System.currentTimeMillis() - start;
        logQuery(query, delta);
        return rs;
    }

    public int executeUpdatePreparedStatement(PreparedStatement statement, String query) throws SQLException {
        long start = System.currentTimeMillis();
        statement.executeUpdate();
        int count = statement.getUpdateCount();
        statement.close();
        long delta = System.currentTimeMillis() - start;
        logQuery(query, delta);
        return count;
    }

    protected void logQuery(String query, long delta) {
        logger.info(this, String.format("MYSQL Execute %s in %d ms ", query, delta));
    }

}
