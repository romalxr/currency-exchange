package org.example.currency.db;

import java.sql.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DBConnector {

    private static final String DB_URL = "jdbc:sqlite::resource:currency.db";

    static {
        loadDriver();
        DBInitializer.initialize();
    }

    private static void loadDriver() {
        try {
            Class.forName("org.sqlite.JDBC");
            log.info("JDBC sqlite loaded successfully!");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}