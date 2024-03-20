package com.shiyatsu.lib.db.handler;

import com.shiyatsu.lib.db.handler.exception.DatabaseException;

import java.sql.Connection;

public interface IDbHandler {

    Connection getConnection(DbConnector con) throws DatabaseException;
    Connection connect(DbConnector con) throws DatabaseException;
}
